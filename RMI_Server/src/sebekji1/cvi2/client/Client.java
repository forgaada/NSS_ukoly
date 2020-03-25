package sebekji1.cvi2.client;

import java.io.FileNotFoundException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sebekji1.cvi2.server.CsvReader;
import sebekji1.cvi2.server.DBExistException;
import sebekji1.cvi2.server.DBNotFoundException;
import sebekji1.cvi2.server.DuplicateKeyException;
import sebekji1.cvi2.server.KeyNotFoundException;
import sebekji1.cvi2.compute.*;

public class Client {
/*
	private static final String K_listDB = "listdb";
	private static final String K_createDB = "createdb";
	private static final String K_insert = "insert";
	private static final String K_update = "update";
	private static final String K_get = "get";
	private static final String K_getA = "geta";
	private static final String K_flush = "flush";
*/
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		String host;
		int PORT;
		String conf_file;

		if (args.length != 3) {
			// <IP_adresa> <cislo_portu> <konf_soubor>

			host = "localhost"; //host = "127.0.0.1";
			PORT = 7777; 
			conf_file = "./data/conf.csv"; // konfig. soubor
			// System.exit(0);
		} else {
			host = args[0]; 
			PORT = Integer.valueOf(args[1]); 
			conf_file = args[2]; 
		}

		try {
			// vyhledani vzdaleneho objektu
			ServerInterface serveri;
			String name = "muj_server";
			Registry registry = LocateRegistry.getRegistry(host, PORT);

			 serveri = (ServerInterface) registry.lookup(name);
			 //serveri = (ServerInterface) Naming.lookup("//"+host+":"+PORT+"/"+name);
			 CsvReader reader = null;
			 try{
				 reader = new CsvReader(conf_file, ';');

			 }catch (Exception e) {
					System.out.println("Cannot find a config file.");
					System.exit(1);
			 }

				while (reader.readRecord()) {
					int index = 0;
					if (reader.getValues().length>0) {
						switch (reader.getValues()[index]) {
							case "listdb": {
								System.out.println("listdb");
								System.out.print("Databases: ");
								for (String dbname: serveri.listDB()) {
									System.out.print(dbname+" ");
								}
								System.out.println();
								break;
							}
							case "get": {
								String dbname = reader.getValues()[++index];
								Integer key =  Integer.parseInt(reader.getValues()[++index]);
								try {
									System.out.println("Getting from database "+dbname+" record "+key);
									serveri.get(dbname, key);
								} catch (Exception e) {
									System.out.println(e.getCause().getMessage());
								}
								break;
							}
							case "geta": {
								System.out.println("GetA");
								String dbname = reader.getValues()[++index];
								List<Integer> keys = new ArrayList<>();
								for (int i = ++index; i<reader.getValues().length; i++) {
									keys.add(Integer.parseInt(reader.getValues()[i]));
								}
								try {
									serveri.getA(dbname, keys.toArray(Integer[]::new));
								} catch (Exception e) {
									System.out.println(e.getCause().getMessage());
								}
								break;
							}
							case "insert":{
								String dbname = reader.getValues()[++index];
								Integer key = Integer.parseInt(reader.getValues()[++index]);
								String message = reader.getValues()[++index];
								try {
									System.out.println("Inserting into database "+dbname+" record ["+key+";"+message+"]");
									serveri.insert(dbname,key,message);
								} catch (RemoteException e) {
									System.out.println(e.getCause().getMessage());
								}
								break;
							}
							case "update": {
								String dbname = reader.getValues()[++index];
								Integer key = Integer.parseInt(reader.getValues()[++index]);
								String message = reader.getValues()[++index];
								try {
									System.out.println("Updating record "+key+" in database "+dbname+" with "+message);
									serveri.update(dbname,key,message);
								} catch (Exception e) {
									System.out.println(e.getCause().getMessage());
								}
								break;
							}
							case "createdb":
							{
								String dbname = reader.getValues()[++index];
								try {
									System.out.println("Creating database "+dbname);
									serveri.createDB(dbname);
								} catch (Exception e) {
									System.out.println(e.getCause().getMessage());
								}
								break;
							}
							case "flush":
							{
								try {
									serveri.flush();
								} catch (Exception e){
									System.out.println("ERROR - flush did not succeeded");
								}
								break;
							}
							default: {
								System.out.println("ERROR – command "+reader.getValues()[index]+" not implemented");
								break;
							}
						}
					}
				}
		} catch (Exception e) {
			System.err.println("Data exception: " + e.getMessage());
		}
	}
}
