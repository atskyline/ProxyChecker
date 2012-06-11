import java.net.Proxy;
import java.util.LinkedList;

public class ProxyPool 
{
    LinkedList<Proxy> checkQueue;
    LinkedList<ProxyCheckResult> resultQueue;
    private volatile Boolean isCancle;
    
    public Boolean getIsCancle()
    {
        return isCancle;
    }
    
    public void setIsCancle(Boolean isCancle)
    {
        this.isCancle = isCancle;
    }
    
    public synchronized int getUncheckCount()
    {
        return checkQueue.size();
    }
    
    public synchronized int getCheckedCount()
    {
        return resultQueue.size();
    }
    
    public synchronized Boolean canGetNext()
    {
        return (!isCancle && checkQueue.size() > 0);
    }
    
    public ProxyPool()
    {
        //LinkedList 其实是进程不安全的,可以用下面的获的一个进程安全的对象
        //但是这里我选择用手动处理同步
        checkQueue  = new LinkedList<>();
        resultQueue  = new LinkedList<>();
//        //获得一个进程安全的LinkedLink
//        uncheckQueue = (LinkedList<Proxy>)Collections.synchronizedList(
//                                                    new LinkedList<Proxy>());
//        checkedQueue  = (LinkedList<ProxyCheckResult>)Collections.synchronizedList(
//                                                    new LinkedList<ProxyCheckResult>());
        isCancle = false;
    }
    
    public synchronized Proxy getNext()
    {
        //用了synchronized,可以用if而不需要使用while
        if (canGetNext())
        {
            return checkQueue.removeFirst();
        }
        return null;
    }
    
    public synchronized  void addProxy(Proxy proxy)
    {
        checkQueue.add(proxy);
    }
    
    public synchronized void addResult(ProxyCheckResult proxyResult)
    {
        resultQueue.add(proxyResult);
    }
    
    public synchronized void cleanResultQueue()
    {
        resultQueue.clear();
    }

    public synchronized void cleanChckQueue()
    {
        checkQueue.clear();
    }
        
    private synchronized void moveResultToCheck()    
    {
        while (!resultQueue.isEmpty())
        {            
            checkQueue.add(resultQueue.remove().getProxy());
        }          
    }
}
