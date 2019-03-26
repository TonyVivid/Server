

	import java.io.*;
import java.net.ServerSocket;
	import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import trans_data.carriage;
import trans_data.carriageList;
import trans_data.chatInfo;
import trans_data.chatInfoList;
import trans_data.hashkey;
import trans_data.loginInfo;
import trans_data.picture;
import trans_data.stuffInfo;
import trans_data.stuffInfoList;
import trans_data.transSign;
	public class Server {	
		List<userinfo> users=new ArrayList<>();
		Server(){
			
		}
		public  void startService() {
		    try{
		    	//new checkUser().start();
		        ServerSocket server =null;
		       
		        Socket client=null;
		        server=new ServerSocket(12345);
		        while(true){
		            
		            System.out.println("wait to be connected");
		            client=server.accept();		            		            
		            if(client!=null){		            	
		                System.out.println(client.getInetAddress().toString()+"  "+client.getPort()+"+connect");
		                new ServerThread(client).start();}	            
		        }

		    }
		    catch(Exception e){		        
		        System.out.println("Service start: "+e.getMessage());

		    }
		}
		
	    
	
	public static  void main (String args[]){
		
	    //stuffHelper.getCarriageInfo();
		new Server().startService();
	
	}

	
}
	class DataBaseHandler {
		DataBase db;
		DataBaseHandler(){
			db=new DataBase();
		}
		public boolean selectQuery(String number,String password) {
			String query= "select password from user where number ="+"'"+number+"'";
			Statement statement=db.createsql();
			ResultSet rset=db.getresult(statement, query);
			
			String db_password=null;
			try {
				while(rset.next()) {
				db_password = rset.getString(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.close();
			return (password.equals(db_password));
			
		}
		
	}

	class ServerThread extends  Thread{
		public static HashMap<String,List<chatInfo>> chatmap=new HashMap<String,List<chatInfo>>();
		public static HashMap<String,Socket> netInfo=new HashMap<String,Socket>();
		int MSG_LOGIN=1111;
		int MSG_INITIAL=1112;
	    Socket socket;
	    DataOutputStream out=null;
	    DataInputStream in=null;
	    ObjectInputStream objin=null;
	    ObjectOutputStream objout=null;
	    loginInfo user=null;
	    int type=0;
	    ServerThread(Socket t){
	        socket=t;
	        try {
	            out = new DataOutputStream((socket.getOutputStream()));
	            in = new DataInputStream(socket.getInputStream());
	            objin=new ObjectInputStream(in);
	            objout=new ObjectOutputStream(out);
	            //user=(loginInfo)objin.readObject();
	        }
	        catch(Exception e){
	        	System.out.println("create inputStream:"+e.getMessage());

	        }

	    }
	    public void run(){
	    	
	    	System.out.println("connect success");
	    	
	        while(socket.isConnected()){
	        	
	            try{
	            	type=in.readInt();
	            	
	            	if(type==32) {
	            	user=(loginInfo)objin.readObject();
	            	
	            	System.out.println("connection type:"+user.getNumber()+user.getPassword());
	            	
	            	Socket temp=netInfo.get(user.getNumber());
	            	if(temp!=null) {
	            		out.writeBoolean(false);
	            		System.out.println("已经登陆");
	            		user.setMsgInfo(9999);
	            	}
	            	else {
	            	
	            	if(user.getMsgInfo()==MSG_LOGIN) {
	            		if(new DataBaseHandler().selectQuery(user.getNumber(),user.getPassword())){	            			
	                        user.setMsgInfo(9999);
	                        
	            			out.writeBoolean(true);
	            			System.out.println("login success");
	            			
	            		}	            		
	            		 
	            	}	            	
	            	if(user.getMsgInfo()==1114) {
	            		System.out.println(user.getMsgInfo());
            			System.out.println("begin to init");
            			user.setMsgInfo(9999);            			
            			initStuff();
            			netInfo.put(user.getNumber(), socket);
            		}
	            	
	            	type=0;
	            	}
	            	}
	            	if(type==33) {
	            		System.out.println("connection type:"+type);
	            		chatInfo chatinfo=(chatInfo)objin.readObject();
	            	    
	            		Socket socket=netInfo.get(chatinfo.getReceiver());
	            		if(socket==null) {
	            			 System.out.println("the user is offline");
	            			String receiver=chatinfo.getReceiver();
	            			List<chatInfo> oldchat=null;
	            			oldchat=chatmap.get(receiver);
	            			 if(oldchat!=null) {
	            				 oldchat.add(chatinfo);
	            			     chatmap.put(chatinfo.getReceiver(),oldchat);
	            			 }
	            			 else {
	            				 System.out.println("build up the new chatmap");
	            				 List<chatInfo> newchat=new ArrayList<>();
	            				 newchat.add(chatinfo);
	            				 chatmap.put(chatinfo.getReceiver(),newchat);
	            			 }
	            		}
	            		if(socket.isConnected()) {
	            			DataOutputStream newout=new DataOutputStream(socket.getOutputStream());
	            			ObjectOutputStream newobjout= new ObjectOutputStream(newout);
	            			newout.writeInt(445);
	            			newobjout.writeObject(chatinfo);
	            			newobjout.flush();
	            			System.out.println(chatinfo.getSender()+":"+"send the info to"+" "+chatinfo.getReceiver());
	            		}
	            		
	            		
	            		type=0;
	            	}
	            	if(type==35) {
	            		System.out.println("send the offline Message");
	            		String name=in.readUTF();
	            		System.out.println("this is"+name+"require the offline info");
	            		
	            		List<chatInfo> list=chatmap.get(name);
	            		if(list!=null) {
	            			out.writeInt(446);
	            			System.out.println("here is the offline info for  "+name+":"+chatmap.get(name).get(0).getInfo());
	            			chatInfoList offlinelist=new chatInfoList();
	            			
	            			offlinelist.setList(chatmap.get(name));
	            			System.out.println("send the info");
	            			objout.writeObject(offlinelist);
	            			objout.flush();
	            			System.out.println("send the info");
	            		}
	            		else {
	            			System.out.println("no offline info for  "+name);
	            			out.writeInt(447);
	            			out.flush();
	            			
	            		}
	            		type=0;
	            	}
	            	if(type==36) {
	            		carriage uploadcarriage=(carriage)objin.readObject();
	            		stuffHelper.setCarriageInfo(uploadcarriage);
	            		System.out.println(uploadcarriage.getText());
	           
	            		carriageList mycarriageList=new carriageList();
	            		mycarriageList.setList(stuffHelper.getCarriageInfo());
	            		 out.writeInt(123);
	                     objout.writeObject(mycarriageList);
	            		
	            	/*
	            		for(Map.Entry<String,Socket> entry : netInfo.entrySet()){
	           
	                       Socket onesocket=entry.getValue();
	                       DataOutputStream tempout=new DataOutputStream(onesocket.getOutputStream());
	                       ObjectOutputStream tempobjout =new ObjectOutputStream(tempout);
	                       System.out.println(onesocket.getInetAddress().toString()+"  "+onesocket.getPort());
	                       
	                       carriageList mycarriageList=new carriageList();
	                       System.out.println("654321");
	                       mycarriageList.setList(stuffHelper.getCarriageInfo());
	                       System.out.println("123456");
	                       tempout.writeInt(123);
	                       tempobjout.writeObject(mycarriageList);
	                       tempobjout.flush();
	                       
	                    }
	                    */
	            		type=0;
	            		System.out.println("finish brocasting the latest carriageList ");	            		
	            		
	            	}
	            	
	               
	            }
	           
	            catch(Exception e){
	            	
	                //System.out.println(e.getMessage());

	            }
               if(!socket.isConnected()) {
            	   System.out.println(socket.getInetAddress().toString()+"  "+socket.getPort()+"+close");
	            }
	        }
	    }

	    public void initStuff() {
	    	
	    	List<carriage> carriage=stuffHelper.getCarriageInfo();
	    	carriageList list=new carriageList();
	    	list.setList(carriage);
	    	try {
				objout.writeObject(list);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	//stuffHelper.setCarriageInfo(c, infoPath);
	    	
	    	
	    
	    }
	   
		  
	}
	class stuffHelper{
		public static picture setData(String path) {
			picture pic= new picture();
			File file=new File(path);
			 try {
				InputStream in =new FileInputStream(file);
				 byte[] data=new byte[(int) (file.length())];
				 in.read(data);
				 pic.setPicData(data);
				 pic.setPicName(getFileName(path));
				 pic.setPicSize(file.length());
				 pic.setType(getFileType(path));
				 in.close();
				 return pic;
				 
			} catch (IOException e) {
				System.out.println("file problem:"+e.getMessage());
						
				e.printStackTrace();
				return null;
				
			}  	
		}
		public static String getFileName(String Path) {
			String fileName[]=Path.split("[.]");
			return fileName[0];
			
		}
		public static String getFileType(String Path) {
			String fileName[]=Path.split("[.]");
			return fileName[1];
			
		}
		public static void setCarriageInfo(carriage c) {
			
			try {
		    File file=new File("stuff_info.txt");
		    List<String> pathlist=new ArrayList<>();
			FileInputStream txtin=new FileInputStream(file);
			stuffInfoList list=new stuffInfoList();
			if(file.length()!=0) {
				ObjectInputStream objin=new ObjectInputStream(txtin);	
				list=(stuffInfoList)objin.readObject();
				objin.close();
			}													
			txtin.close();
			File fileout=new File("stuff_info.txt"); 
			FileOutputStream txtout= new FileOutputStream(fileout);
			ObjectOutputStream objout= new ObjectOutputStream(txtout);
			stuffInfo info=new stuffInfo();
			info.setName(c.getName());
			Random random=new Random();
			info.setNubmer(c.getName()+String.valueOf(random.nextInt(1000)));
			info.setOwner(c.getOwner());
			info.setPrice(c.getPrice());
			info.setState("ON_SALE");
			info.setText(c.getText());
			for(int i=0;i<c.getPicture().size();i++) {
				String imgname=c.getNumber()+"_"+String.valueOf(random.nextInt(1000))+c.getPicture().get(i).getType();
				File image=new File(imgname);
				FileOutputStream imageout= new FileOutputStream(image);
				imageout.write(c.getPicture().get(i).getPicData());
				pathlist.add(imgname);
				imageout.close();
				}
			String [] infopath=pathlist.toArray(new String[pathlist.size()]);
			info.setPath(infopath);
			List<stuffInfo> infolist=new ArrayList<>();
			if(list.getList()!=null) {
			 infolist= list.getList();
			}			
			infolist.add(info);
			list.setList(infolist);
			objout.writeObject(list);
			objout.close();
			txtout.close();
	
			}
			
			
			
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		public static List<carriage> getCarriageInfo() {
			File file=new File("stuff_info.txt");
			try {
			FileInputStream txtin=new FileInputStream(file);
			ObjectInputStream objin=new ObjectInputStream(txtin);
			List<carriage> list=new ArrayList<>();	
			stuffInfoList stuff=new stuffInfoList();
			stuff=(stuffInfoList)objin.readObject();
			for(int i=0;i<stuff.getList().size();i++) {
				List<picture> piclist= new ArrayList<>();
				carriage my_carriage= new carriage();
				my_carriage.setName(stuff.getList().get(i).getName());
				my_carriage.setNumber(stuff.getList().get(i).getNumber());
				my_carriage.setOwner(stuff.getList().get(i).getOwner());
				my_carriage.setPrice(stuff.getList().get(i).getPrice());
				my_carriage.setText(stuff.getList().get(i).getText());
				for(int j=0; j<stuff.getList().get(i).getPath().length;j++) {
					picture picdata=setData(stuff.getList().get(i).getPath()[j]);
					piclist.add(picdata);
					System.out.println("第"+i+"个: "+stuff.getList().get(i).getPath()[j]);
				}
				my_carriage.setPicture(piclist);
				list.add(my_carriage);
			}
			objin.close();
			txtin.close();
			return list;
			}catch(Exception e) {
				e.printStackTrace();
				return null;
				
			}
			
			
			
		}
		
	}


