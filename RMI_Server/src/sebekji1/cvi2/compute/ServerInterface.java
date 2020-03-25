package sebekji1.cvi2.compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

import sebekji1.cvi2.server.DBExistException;
import sebekji1.cvi2.server.DBNotFoundException;
import sebekji1.cvi2.server.DuplicateKeyException;
import sebekji1.cvi2.server.KeyNotFoundException;


public interface ServerInterface extends Remote {

    /**
     * vyp�e existuj�c� datab�ze
     */
    public String[] listDB() throws RemoteException;

    /**
     * vytvo�� datab�zi dan�ho jm�na
     */
    public boolean createDB(String dbname) throws DBExistException, RemoteException;

    /**
     * vytvo�� v datab�zi nov� z�znam
     */
    public Integer insert(String dbname, Integer key, String message) throws DBNotFoundException, DuplicateKeyException, RemoteException;

    /**
     * aktualizuje z�znam dan� kl��em key na hodnotu message
     */
    public Integer update(String dbname, Integer key, String message) throws DBNotFoundException, KeyNotFoundException, RemoteException;

    /**
     * vr�t� zpr�vu p��slu�ej�c� dan�mu kl��i
     */
    public DBRecord get(String dbname, Integer key) throws DBNotFoundException, KeyNotFoundException, RemoteException;

    /**
     * vr�t� pole z�znam� p��slu�ej�c� dan�m kl���m, operace se povede bez chyby
     * pouze tehdy, pokud se povedou naj�t v�echny odpov�di
     */
    public DBRecord[] getA(String dbname, Integer[] key) throws DBNotFoundException, KeyNotFoundException, RemoteException;

    /**
     * zap�e zm�ny na disk
     */
    public void flush() throws RemoteException;
}
