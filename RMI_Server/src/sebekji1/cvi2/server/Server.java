package sebekji1.cvi2.server;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import sebekji1.cvi2.compute.ServerInterface;

public class Server {

	public static void main(String[] args) {
		//System.out.println(System.getProperty("user.home")+ System.getProperty("file.separator")+"data"+System.getProperty("file.separator"));

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}
		
		String host;
		int PORT;

		if (args.length != 2) {
			// <IP_adresa> <cislo_portu> neni zadano
			host = "127.0.0.1";
			PORT = 7777; 
		} else {
			host = args[0];
			PORT = Integer.valueOf(args[1]);
			
		}
		// sluzba
		String name = "muj_server";

		try {
			// vytvoreni objektu a jeho stubu
			ServerInterface serveri = new ServerImpl();

			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(serveri, PORT);

			// zaregistrovani jmena u objektu
			Registry registry = LocateRegistry.createRegistry(PORT);
			registry.rebind(name, stub);
			
			//Naming.rebind("//"+host+":"+PORT+"/"+name, stub);


			System.out.println("Server bezi na adrese a portu: "+ host +"/"+ PORT);
		} catch (Exception e) {
			System.err.println("Data exception: " + e.getMessage());
		}
	}
}
