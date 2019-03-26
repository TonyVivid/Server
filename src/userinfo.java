
import java.net.Socket;

public class userinfo {
	  String number;
	 
	    String ip;
	    int port;
	    Socket socket;
	    userinfo(Socket scoket, String ip, int port){
	        this.socket=scoket;
	       
	        this.ip=ip;
	        this.port=port;
	    }
}
