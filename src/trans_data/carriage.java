package trans_data;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public class carriage implements Serializable {
    private String number; //������
	private String name;    //��������
	private String owner;   //����ӵ����
	private int price;      //����۸�
	private String text;   //������Ϣ
	private List<picture> mypicture; //����ͼƬ
	public void setName(String name) {
		this.name=name;
	}
	public void setOwner(String owner) {
		this.owner=owner;
	}
    public void setPrice(int price) {
    	this.price=price;
    	
    }
    public void setText(String text) {
    	this.text=text;
    	
    }

    public void setPicture(List<picture> list) {
    	this.mypicture=list;
    }
    public void setNumber(String number) {
    	this.number=number;
    }
    public String getNumber() {
    	return this.number;
    }
    public String  getName(){
        return name;
    }
    public String getOwner(){
        return owner;
    }
    public String getText(){
        return text;
    }
    public int getPrice(){
        return price;
    }
    public  List<picture> getPicture(){
        return this.mypicture;
    }
}
