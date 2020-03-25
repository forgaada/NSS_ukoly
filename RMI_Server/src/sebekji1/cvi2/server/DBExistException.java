/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sebekji1.cvi2.server;

import java.rmi.RemoteException;


public class DBExistException extends RemoteException {

	private static final long serialVersionUID = -1937161867341487386L;

	public DBExistException(String message) {
		super(message);
	}
    
}
