package zhu.ProxyChecker;

import java.io.File;
import java.io.IOException;


public class Config 
{
    //参数
    public static int connectTimout;
    public static int readTimeout;
    public static int threadCount;
    public static String urlString;
    public static String keyword;
    public static String lookAndFeel;
    
    public static void readConfig(File file)
    {
        try
        {
            ConfigSetter configSetter = new ConfigSetter(file);
            Config.connectTimout = Integer.valueOf(configSetter.readKey("ConnectTimout"));
            Config.readTimeout = Integer.valueOf(configSetter.readKey("ReadTimeout"));
            Config.threadCount = Integer.valueOf(configSetter.readKey("ThreadCount"));
            Config.urlString = configSetter.readKey("URL");
            Config.keyword = configSetter.readKey("KeyWord");
            Config.lookAndFeel = configSetter.readKey("LookAndFeel");
            setLookAndFeel(lookAndFeel);
        } catch (IOException ex)
        {
            System.out.println("ReadConfig错误,读取默认设置");
            setDefaultConfig();
        }
    }
    
    public static void readConfig(String fileName)
    {
        readConfig(new File(fileName));
    }
    
    public static void readConfig()
    {
        readConfig(new File("config.xml"));
    }   
    
    public static void writeConfig(File file)
    {
        try
        {
            ConfigSetter configSetter = new ConfigSetter(file);
            configSetter.setKey("ConnectTimout",String.valueOf(Config.connectTimout));
            configSetter.setKey("ReadTimeout",String.valueOf(Config.readTimeout));
            configSetter.setKey("ThreadCount",String.valueOf(Config.threadCount));
            configSetter.setKey("URL",Config.urlString);
            configSetter.setKey("KeyWord",Config.keyword);
            configSetter.setKey("LookAndFeel",Config.lookAndFeel);

            configSetter.save();            
        } catch (IOException ex)
        {
            System.out.println("WriteConfig错误");
        }
    }
    
    public static void writeConfig(String fileName)
    {
        writeConfig(new File(fileName));
    }
    
    public static void writeConfig()
    {
        writeConfig(new File("config.xml"));
    }
    
    public static void setDefaultConfig()
    {
        Config.connectTimout = 3000;
        Config.readTimeout = 5000;
        Config.threadCount = 30;
        Config.urlString = "http://www.baidu.com/";
        Config.keyword = "百度";
        Config.lookAndFeel = "Metal";
    }
    
    public static void setLookAndFeel(String lookAndFeel)
    {
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if (lookAndFeel.equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
}
