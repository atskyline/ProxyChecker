package zhu.ProxyChecker;


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
    private LinkedList<ZProxy> allProxyList;
    
    private Boolean isEnable;

    public ProxyPool()
    {
        uncheckQueue    = new LinkedList<>();
        failedList      = new LinkedList<>();
        successfulList  = new LinkedList<>();
        checkingList    = new LinkedList<>();
        allProxyList    = new LinkedList<>();
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
    
    public synchronized Boolean isExist(ZProxy proxy)
    {
        if (allProxyList.indexOf(proxy) == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    public synchronized void add(ZProxy proxy)
    {
        if (isExist(proxy))
        {
            //已经存在的就从原来所在的队列中删去,但是不修改allProxyList
            while(uncheckQueue.remove(proxy));
            while(failedList.remove(proxy));
            while(successfulList.remove(proxy));
            while(checkingList.remove(proxy));
        }
        else
        {
            allProxyList.add(proxy);
        }
        
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
            
            for (int ip0 = (begBytes[0] + 256) % 256; ip0 <= (endBytes[0] + 256) % 256; ip0++)
            {
                for (int ip1 = (begBytes[1] + 256) % 256; ip1 <= (endBytes[1] + 256) % 256; ip1++)
                {
                    for (int ip2 = (begBytes[2] + 256) % 256; ip2 <= (endBytes[2] + 256) % 256; ip2++)
                    {
                        for (int ip3 = (begBytes[3] + 256) % 256; ip3 <= (endBytes[3] + 256) % 256; ip3++)
                        {
                              add(new ZProxy(
                                      InetAddressHelper.getInetAddress(
                                                        Integer.valueOf(ip0)
                                                + "." + Integer.valueOf(ip1)
                                                + "." + Integer.valueOf(ip2)
                                                + "." + Integer.valueOf(ip3)),
                                      port,
                                      type,
                                      ZProxy.UNCHECK
                                      ));
                        }
                    }
                }
            }
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(ProxyPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void add(String ip,int port,Proxy.Type type)
    {
        try
        {
            add(new ZProxy(InetAddressHelper.getInetAddress(ip),
                            port,
                            type,
                            ZProxy.UNCHECK));
        } catch (UnknownHostException ex)
        {
            System.out.println("非法IP地址");
        }
    }
    
    public void add(LinkedList<ZProxy> list)
    {
        for (ZProxy proxy : list)
        {
            add(proxy);
        }
    }
    
    public synchronized void moveAllToUncheck()
    {
        for (ZProxy proxy : allProxyList)
        {
            proxy.setState(ZProxy.UNCHECK);
            uncheckQueue.add(proxy);
        }
        failedList.clear();
        successfulList.clear();
        checkingList.clear();
    }
    
    public synchronized void remove(ZProxy proxy)
    {
        while(uncheckQueue.remove(proxy)){}
        while(failedList.remove(proxy)){}
        while(successfulList.remove(proxy)){}
        while(checkingList.remove(proxy)){}
        while(allProxyList.remove(proxy)){}
        
        
    }
    
    public synchronized void remove(int rowIndex)
    {
        ZProxy proxy = allProxyList.get(rowIndex);
        remove(proxy);
    }
    
    public synchronized ZProxy getProxy(int rowIndex)
    {
        return allProxyList.get(rowIndex);
    }
    
    public synchronized void removeAll()
    {
        uncheckQueue.clear();
        checkingList.clear();
        failedList.clear();
        successfulList.clear();
        allProxyList.clear();
    }
    
    public synchronized void cleanUncheck()
    {
        for(ZProxy proxy : uncheckQueue)
        {
            remove(proxy);
        }
    }
    
    public synchronized void cleanFailed()
    {
        for(ZProxy proxy : failedList)
        {
            remove(proxy);
        }
    }
    
    public synchronized ZProxy[] getAllProxyArray()
    {
       return allProxyList.toArray(new ZProxy[0]);
    }
    
    public synchronized LinkedList<ZProxy> getAllProxyList()
    {
        return allProxyList;
    }
    
    public synchronized int getSize()
    {
        return (allProxyList.size());
    }
    
    //用于和ProxyTableModel的数据提取
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        ZProxy proxy = allProxyList.get(rowIndex);
        
        if (columnIndex == 0)
        {
            //IP
            return proxy.getAddressString();
        } else if (columnIndex == 1)
        {
            //prot
            return proxy.getPort();
        } else if (columnIndex == 2)
        {
            //protocol
            return proxy.type().toString();
        } else if (columnIndex == 3)
        {
            //result
            if (proxy.getState() == -1)
            {
                return "验证失败";
            } else if(proxy.getState() == -2)
            {
                return "验证中...";
            } else if(proxy.getState() == -3)
            {
                return "未验证";
            }
            return proxy.getState();
        }
        return null;
    }
}
