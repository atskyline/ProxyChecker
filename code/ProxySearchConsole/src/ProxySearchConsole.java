
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProxySearchConsole 
{

    //参数
    public static int connectTimout;
    public static int readTimeout;
    public static int threadCount;
    public static String urlString;
    public static String keyword;
    
    private static ProxyPool proxyPool = new ProxyPool();
    
    public static void main(String[] args) 
    {
        readConfig();
        readResult();
        
        //proxyPool.add("222.195.0.0", "222.195.63.255", 808, Proxy.Type.HTTP);
        
        for (ZProxy proxy : proxyPool.getAllProxyArray())
        {
            System.out.println(proxy);
        }
        System.out.println("===========================");
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++)
        {
            exec.execute(new CheckProxyThread(proxyPool));
        }
        
        //shutdown()调用前提交所有任务
        exec.shutdown();
        
        try
        {
            Thread.currentThread().sleep(10000);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(ProxySearchConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        proxyPool.setIsEnable(false);
        
        while(!proxyPool.isChengingEmpty() || !proxyPool.isUncheckEmpty());
        {
            Thread.yield();
        }
        
        for (ZProxy proxy : proxyPool.getAllProxyArray())
        {
            System.out.println(proxy);
        }
        
        wirteResult();
        
    }
    
    private static void readConfig()
    {
        try
        {
            Config config = new Config("config.xml");
            connectTimout = Integer.valueOf(config.readKey("ConnectTimout"));
            readTimeout = Integer.valueOf(config.readKey("ReadTimeout"));
            threadCount = Integer.valueOf(config.readKey("ThreadCount"));
            urlString = config.readKey("URL");
            keyword = config.readKey("KeyWord");
            
        } catch (IOException ex)
        {
            System.out.println("ReadConfig错误,读取默认设置");
            setDefaultConfig();
        }
        
    }
    
    private static void setDefaultConfig()
    {
        connectTimout = 3000;
        readTimeout = 5000;
        threadCount = 30;
        urlString = "http://www.baidu.com/";
        keyword = "百度";
    }
    
    private static void readResult()
    {
        proxyPool.add(ResultIO.readResult("result.txt"));
    }
    
    private static void wirteResult()
    {
        ResultIO.WriteReult(proxyPool.getAllProxyList(), "result2.txt");
    }
}
