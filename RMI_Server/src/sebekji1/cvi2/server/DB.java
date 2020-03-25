package sebekji1.cvi2.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sebekji1.cvi2.compute.DBRecord;

public class DB {
	private String dbname;
	private List<DBRecord> recordList = new ArrayList<DBRecord>();

	public DB(String name) {
		this.dbname = name;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public void setRecordList(DBRecord[] list) {
		for (int i = 0; i < list.length; i++) {
			this.recordList.add(list[i]);
		}

	}

	public String getDbname() {
		return dbname;
	}

	public DBRecord[] getRecordList() {
		int count = recordList.size();
		DBRecord[] radky = new DBRecord[count];
		for (int i = 0; i < count; i++) {
			radky[i] = (DBRecord) recordList.get(i);
		}
		return radky;
	}

	public void addRecord(DBRecord radek) {
		recordList.add(radek);
	}

	public boolean foundKey(int key) {
		boolean found = false;
		int count = recordList.size();
		for (int i = 0; i < count; i++) {
			if (recordList.get(i).getKey() == key) {
				found = true;
			}
		}
		return found;
	}

	public void insert(Integer key, String message) {
		Timestamp t = new Timestamp((new Date()).getTime());
		recordList.add(new DBRecord(t.toString(), t.toString(), key, message));
	}

	public void update(Integer key, String message) {
		Timestamp t = new Timestamp((new Date()).getTime());
		int count = recordList.size();
		for (int i = 0; i < count; i++) {
			DBRecord record = recordList.get(i);
			if (record.getKey() == key) {
				record.setMessage(message);
				record.setTsmodify(t.toString());
			}

		}
	}

	public DBRecord get(Integer key) {
		boolean bool=false;
		DBRecord record=null;
		int count = recordList.size();
		for (int i = 0; i < count; i++) {
			if (recordList.get(i).getKey() == key) {
				record = ((DBRecord) recordList.get(i));
				bool = true;
			}
		}
		if(bool){
			return record;
		}else{
			return null;
		}
		
	}

}
