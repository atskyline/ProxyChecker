package zhu.ProxyChecker;

import java.util.EventListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;


interface CheckFinishListener extends EventListener
{
    void action();
}

interface CheckStartedListener extends EventListener
{
    void action();
}

public class ProxyChecker
{

    private EventListenerList checkFinishEventListenerList 
                                            = new EventListenerList();
 
    private EventListenerList checkStartedEventListenerList 
                                            = new EventListenerList();
    
    private ProxyPool proxyPool;
    public ProxyChecker(ProxyPool proxyPool)
    {
        this.proxyPool = proxyPool;
    }

    public void addCheckFinishListener(CheckFinishListener listener)
    {
        checkFinishEventListenerList.add(CheckFinishListener.class, listener);
    }
    
    public void removeCheckFinishListener(CheckFinishListener listener)
    {
        checkFinishEventListenerList.remove(CheckFinishListener.class, listener);
    }
    
    private void fireCheckFinishAction()
    {
        for(CheckFinishListener listener : 
                checkFinishEventListenerList.getListeners(
                                            CheckFinishListener.class))
        {
            listener.action();
        }
    }
    
    public void addCheckStartedListener(CheckStartedListener listener)
    {
        checkStartedEventListenerList.add(
                            CheckStartedListener.class, listener);
    }
    
    public void removeCheckStartedListener(CheckStartedListener listener)
    {
        checkStartedEventListenerList.remove(
                                CheckStartedListener.class, listener);
    }
    
    private void fireCheckStartAction()
    {
        for(CheckStartedListener listener :
                checkStartedEventListenerList.getListeners(
                CheckStartedListener.class))
        {
            listener.action();
        }
    }
    
    public void startCheckAll()
    {
        //把所有的变成未检验的
        proxyPool.moveAllToUncheck();
        //开启检验标志 
        proxyPool.setIsEnable(true);
        //通知已经开始了
        fireCheckStartAction();
        
        ExecutorService execCheck = Executors.newCachedThreadPool();
        for (int i = 0; i < Config.threadCount; i++)
        {
            execCheck.execute(new CheckProxyThread(proxyPool));
        }
        //shutdown()调用前提交所有任务
        execCheck.shutdown();
        
        //开一个线程去等待完成
        ExecutorService execWaitCheckFiniash = 
                Executors.newSingleThreadExecutor();
        execWaitCheckFiniash.execute(new Runnable()
        {
            //等待检验完成,并调用事件.
            @Override
            public void run()
            {
                while(CheckProxyThread.getCurrentThreadCount() != 0)
                {
                    Thread.yield();
                }
                //通知已经完成了
                fireCheckFinishAction();
            }
        });
        execWaitCheckFiniash.shutdown();

    }
    
    public void startCheck(ZProxy proxy)
    {
        new CheckProxyThread(proxyPool).checkProxy(proxy);
    }
    
    public void stopCheck()
    {
        proxyPool.setIsEnable(false);
    }
}

