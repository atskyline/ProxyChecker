package zhu.ProxyChecker;

import java.io.File;
import javax.swing.SwingUtilities;

public class Application 
{
    public static MainFrame mainFrame;
    public static ProxyPool proxyPool;
    public static ProxyChecker proxyChecker;
    
    private Application()
    {
        
    }
    
    public static void main(String[] args) 
    {   
        proxyPool = new ProxyPool();
        proxyChecker = new ProxyChecker(proxyPool);
        
        //读取配置参数
        Config.readConfig();
        //读取结果列表
        readResult();

        //显示主窗体
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });
    }
        
    public static void readResult(File file)
    {
        proxyPool.add(ResultIO.readResult(file));
    }
    
    public static void readResult(String fileName)
    {
        readResult(new File(fileName));
    }
    
    public static void readResult()
    {
        readResult(new File("result.txt"));
    }
            
    public static void writeResult(File file)
    {
        ResultIO.WriteReult(proxyPool.getAllProxyList(), file);
    }
    
    public static void writeResult(String fileName)
    {
        writeResult(new File(fileName));
    }
        
    public static void writeResult()
    {
        writeResult(new File("result.txt"));
    }
        
    
    public static void addCheckFinishListener(CheckFinishListener listener)
    {
        proxyChecker.addCheckFinishListener(listener);
    }
    
    public static void addCheckStartedListener(CheckStartedListener listener)
    {
        proxyChecker.addCheckStartedListener(listener);
    }
    
    public static void startCheckAll()
    {
        proxyChecker.startCheckAll();
    }
    
    public static void stopCheck()
    {
        proxyChecker.stopCheck();
    }
    
    public static void cleanAll()
    {
        proxyPool.removeAll();
    }
    
    public static void cleanFailed()
    {
        proxyPool.cleanFailed();
    }
}
