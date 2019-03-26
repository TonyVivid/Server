package trans_data;

import java.io.Serializable;
import java.util.List;

public class stuffInfoList implements Serializable{
	private List<stuffInfo> list;
	public List<stuffInfo> getList() {
		return list;
	}
    public void setList(List<stuffInfo> list) {
    	this.list=list;
    }
}
