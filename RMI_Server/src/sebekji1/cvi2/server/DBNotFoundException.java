package sebekji1.cvi2.server;

import java.rmi.RemoteException;

public class DBNotFoundException extends RemoteException {

	private static final long serialVersionUID = -1937161867341487386L;

	public DBNotFoundException(String message) {
		super(message);
	}
    
}
