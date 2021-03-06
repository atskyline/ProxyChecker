
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyPool 
{
    private LinkedList<ZProxy> uncheckQueue;
    private LinkedList<ZProxy> failedList;
    private LinkedList<ZProxy> successfulList;
    private LinkedList<ZProxy> checkingList;
    private Boolean isEnable;

    public ProxyPool()
    {
        uncheckQueue    = new LinkedList<>();
        failedList      = new LinkedList<>();
        successfulList  = new LinkedList<>();
        checkingList    = new LinkedList<>();
        isEnable = true;
    }

    public synchronized Boolean getIsEnable()
    {
        return isEnable;
    }

    public synchronized void setIsEnable(Boolean isEnable)
    {
        this.isEnable = isEnable;
    }
    
    public synchronized Boolean canGetNext()
    {
        return (isEnable && uncheckQueue.size() > 0);
    }
    
    public synchronized ZProxy getNext()
    {
        //用了synchronized,可以用if而不需要使用while
        if (canGetNext())
        {
            ZProxy proxy = uncheckQueue.removeFirst();
            proxy.setState(ZProxy.CHECKING);
            add(proxy);
            return proxy;
        }
        return null;
    }
    
    public synchronized void add(ZProxy proxy)
    {
        //每次添加前先试图把他从所有列表中中移除,来维护所有列表不会有重复项
        remove(proxy);
        if (proxy.getState() == ZProxy.CHECKING)
        {
            checkingList.add(proxy);
        }
        else if (proxy.getState() == ZProxy.FAILED)
        {
            failedList.add(proxy);
        }
        else if (proxy.getState() == ZProxy.UNCHECK)
        {
            uncheckQueue.add(proxy);
        }
        else
        {
            successfulList.add(proxy);
        }
    }

    public void add(String begIp, String endIp, int port, Proxy.Type type)
    {
        try
        {
            byte[] begBytes = InetAddressHelper.getInetAddress(begIp).getAddress();
            byte[] endBytes = InetAddressHelper.getInetAddress(endIp).getAddress();
            
            for (byte ip0 = begBytes[0]; ip0 <= endBytes[0]; ip0++)
            {
                for (byte ip1 = begBytes[1]; ip1 <= endBytes[1]; ip1++)
                {
                    for (byte ip2 = begBytes[2]; ip2 <= endBytes[2]; ip2++)
                    {
                        for (byte ip3 = begBytes[3]; ip3 <= endBytes[3]; ip3++)
                        {
                            byte[] addBytes = new byte[]{ip0,ip1,ip2,ip3};
                            add(new ZProxy(addBytes, port, type, ZProxy.UNCHECK));
                        }
                    }
                }
            }
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(ProxyPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void remove(ZProxy proxy)
    {
        while(uncheckQueue.remove(proxy));
        while(failedList.remove(proxy));
        while(successfulList.remove(proxy));
        while(checkingList.remove(proxy));
    }
    
    public synchronized void cleanUncheck()
    {
        uncheckQueue.clear();
    }
    
    public synchronized void cleanFailed()
    {
        failedList.clear();
    }
    
    public synchronized void cleanSuccessful()
    {
        successfulList.clear();
    }
    
    public synchronized void cleanChecking()
    {
        checkingList.clear();
    }
    
    public synchronized ZProxy[] getAllProxyArray()
    {
        LinkedList<ZProxy> list = new LinkedList<>();
        list.addAll(successfulList);
        list.addAll(uncheckQueue);
        list.addAll(failedList);
        list.addAll(checkingList);

        return list.toArray(new ZProxy[0]);
    }
    
    public synchronized LinkedList<ZProxy> getAllProxyList()
    {
        LinkedList<ZProxy> list = new LinkedList<>();
        list.addAll(successfulList);
        list.addAll(uncheckQueue);
        list.addAll(failedList);
        list.addAll(checkingList);

        return list;
    }
}
