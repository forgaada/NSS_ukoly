package sebekji1.cvi2.compute;

import java.io.Serializable;

/**
 * Jeden db zaznam.
 */
public class DBRecord implements Serializable {

	// pro potrebu serializace (marshalingu)
	private static final long serialVersionUID = -1937161867341487386L;
	
	private String tscreate;
	private String tsmodify;
	private Integer key;
	private String message;

	public DBRecord(String tscreate, String tsmodify, int key, String message) {
		this.tscreate = tscreate;
		this.tsmodify = tsmodify;
		this.key = key;
		this.message = message;
	}

	public DBRecord() {
	}

	public void setTscreate(String tscreate) {
		this.tscreate = tscreate;
	}
	
	public String getTscreate() {
		return tscreate;
	}
	
	public void setTsmodify(String tsmodify) {
		this.tsmodify = tsmodify;
	}
	
	public String getTsmodify() {
		return tsmodify;
	}
	
	public void setKey(int key) {
		this.key = key;
	}
	
	public int getKey() {
		return key;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}



}
