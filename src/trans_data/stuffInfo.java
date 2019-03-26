package trans_data;

import java.io.Serializable;

public class stuffInfo implements Serializable{
	private String number; //货物编号
	private String name;    //货物名称
	private String owner;   //货物拥有者
	private int price;      //货物价格
	private String text;   //货物信息
    private String state;
    private String []picpath;
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

    public void setPath(String[] path) {
    	picpath=path;
    }
    public void setNubmer(String number) {
    	this.number=number;
    }
    public void setState(String state) {
    	this.state=state;
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
    public String getState() {
    	return state;
    }
    public String[] getPath() {
    	return picpath;
    }
}
