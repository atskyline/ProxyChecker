package zhu.ProxyChecker;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class ConfigSetter 
{
    private File file;
    private Properties properties =  new Properties();

    public ConfigSetter(File file) throws IOException
    {
        this.file = file;
        if (!file.exists())
        {
            file.createNewFile();
        }
        properties.loadFromXML(new FileInputStream(this.file));  
    }
    
    public String readKey(String key)
    {
        return properties.getProperty(key);
    }
    
    public void setKey(String key, String value)
    {
        properties.setProperty(key, value);
    }
    
    public void save() throws IOException
    {
        properties.storeToXML(new FileOutputStream(file), null);
    }
}
