
package sebekji1.cvi2.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import sebekji1.cvi2.compute.DBRecord;
import sebekji1.cvi2.compute.ServerInterface;

//import sebekji1.cvi2.server.DBExistException;
//import sebekji1.cvi2.server.DBNotFoundException;
//import sebekji1.cvi2.server.DuplicateKeyException;
//import sebekji1.cvi2.server.KeyNotFoundException;

public class ServerImpl implements ServerInterface {

	CsvWriter writer;
	CsvReader reader;
	static final String data_directory = System.getProperty("user.dir")+ System.getProperty("file.separator")+"data"+System.getProperty("file.separator");
	HashMap<String, DB> map;
	DBRecord[] zaznamy;
	File dbfile;
	

	public ServerImpl() throws IOException {
    //todo here
    //zde napl�te mapu - eager init
		dbfile = new File(data_directory);
		map = new HashMap<>();
		File[] files = dbfile.listFiles();
        assert files != null;
		List<DBRecord> records = new ArrayList<>();

		for (File file : files) {

            if(!file.getName().toLowerCase().endsWith(".dbcsv")){
                continue;
            }

            reader = new CsvReader(data_directory + file.getName(), ';');

            while (reader.readRecord()) {
				DBRecord record = new DBRecord();

				record.setTscreate(reader.getValues()[0]);
				record.setTsmodify(reader.getValues()[1]);
				record.setKey(Integer.parseInt(reader.getValues()[2]));

				record.setMessage(reader.getValues()[3]);

                records.add(record);
            }

            //vlozeni do mapy
            String nameOfDatabase = file.getName().split(".dbcsv")[0];
			DB db = new DB(nameOfDatabase);
            zaznamy = records.toArray(DBRecord[]::new);
            db.setRecordList(zaznamy);
            map.put(nameOfDatabase, db);
        }
	}


	@Override
	public String[] listDB() {
        return map.keySet().toArray(new String[0]);
	}


	@Override
	public boolean createDB(String dbname) throws DBExistException {
		if (map.containsKey(dbname)) {
			throw new DBExistException("ERROR - Database "+dbname+" exists");
		}
    	DB db = new DB(dbname);
		map.put(dbname,db);
		return true;
	}

	@Override
	public Integer insert(String dbname, Integer key, String message)
			throws DBNotFoundException, DuplicateKeyException {

		if(!map.containsKey(dbname)) {
			throw new DBNotFoundException("ERROR - Database "+dbname+" does not exists");
		}
		DB db = map.get(dbname);

		if (db.foundKey(key)) {
			throw new DuplicateKeyException("Insert failed. Record with given key already exists!");
		} else {
			db.insert(key,message);
			return 0;
		}
	}

	@Override
	public Integer update(String dbname, Integer key, String message)
			throws DBNotFoundException, KeyNotFoundException {

		if(!map.containsKey(dbname)) {
            throw new DBNotFoundException("ERROR - Database "+dbname+" does not exists");
		}
		DB db = map.get(dbname);
		if (db.foundKey(key)) {
			db.update(key,message);
			return 0;
		}

		throw new KeyNotFoundException("Update failed. Key "+key+" not found!");
	}

	@Override
	public DBRecord get(String dbname, Integer key) throws DBNotFoundException,
			KeyNotFoundException {

		if(!map.containsKey(dbname)) {
            throw new DBNotFoundException("ERROR - Database "+dbname+" does not exists");
		}
        DB db = map.get(dbname);
		if (db.foundKey(key)){
			return db.get(key);
		} else {
			throw new KeyNotFoundException("Get failed. Key "+key+" not found!");
		}
//        DBRecord[] records = db.getRecordList();
//        for (DBRecord record: records) {
//            if (record.getKey() == key) {
//                return record;
//            }
//        }
	}

	@Override
	public DBRecord[] getA(String dbname, Integer[] keys)
			throws DBNotFoundException, KeyNotFoundException {

		if(!map.containsKey(dbname)) {
            throw new DBNotFoundException("ERROR - Database "+dbname+" does not exists");
		}
		DB db = map.get(dbname);

		List<DBRecord> resultRecords = new ArrayList<>();
		List<Integer> keyList = Arrays.asList(keys);

		for (DBRecord record: db.getRecordList()) {
			if (keyList.contains(record.getKey())) {
				resultRecords.add(record);
			}
		}

		if (resultRecords.size() != keyList.size()) {
			throw new KeyNotFoundException("GetA failed. Key not found!");
		}

//		for (int key: keys) {
//			if (db.foundKey(key)) {
//				resultRecords.add(db.get(key));
//				continue;
//			}
//			throw new KeyNotFoundException("GetA failed. Key not found!");
//		}

        return resultRecords.toArray(DBRecord[]::new);
	}

	/**
	 * Zap� zm�ny na disk
	 * 
	 */
	@Override
	public void flush() {
        for (String key: map.keySet()) {
            writer = new CsvWriter("./data/"+key+".dbcsv");
            writer.setDelimiter(';');
            //String[] values = new String[map.get(key).getRecordList().length];
			String[] recordString = new String[4];
            for (int i = 0; i<map.get(key).getRecordList().length; i++) {
            	recordString[0] = map.get(key).getRecordList()[i].getTscreate();
				recordString[1] = map.get(key).getRecordList()[i].getTsmodify();
				recordString[2]	= String.valueOf(map.get(key).getRecordList()[i].getKey());
				recordString[3]	= map.get(key).getRecordList()[i].getMessage();
				try {
					writer.writeRecord(recordString);
				} catch (Exception e) {
					System.out.println("ERROR - flush not succeeded");
				}
			}
            writer.close();
        }
		}
	}
