package trans_data;

import java.io.Serializable;
import java.util.List;

public class offLineChatInfo implements Serializable{
    String receiver;
    String sender;
    List<String> info;

    public void setReceiver(String receiver){
        this.receiver=receiver;
    }
    public void setSender(String sender) {
    	this.sender=sender;
    }
    public void setInfo(List<String> info){
        this.info=info;
    }
    public String getReceiver(){
        return this.receiver;
    }
    public List<String> getInfo(){
        return this.info;
    }
    public String getSender() {
    	return this.sender;
    }

}
