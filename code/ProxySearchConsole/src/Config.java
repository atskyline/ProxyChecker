
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class Config 
{
    private File file;
    private Properties properties =  new Properties();
    
    public Config(String fileName) throws IOException
    {
        this.file = new File(fileName);
        if (!file.exists())
        {
            file.createNewFile();
        }
        properties.loadFromXML(new FileInputStream(file));  
    }

    public Config(File file) throws IOException
    {
        this.file = file;
        if (!file.exists())
        {
            file.createNewFile();
        }
        properties.load(new FileInputStream(this.file));  
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
