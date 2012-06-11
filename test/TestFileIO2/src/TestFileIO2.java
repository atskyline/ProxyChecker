/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream.GetField;
import java.util.Properties;
import java.util.TreeMap;


public class TestFileIO2 
{
   
    //指定文件路径及文件名
    private static  String fileName = "config.ini";
    private static  Properties props = new Properties();
    private static  InputStream inputFile;
    private static  FileOutputStream outputFile;
    
    /**
     * 根据Key 读取Value
     * @param key
     * @return
     */
    public static String readKey(String key) 
    {
        String value="";
        try 
        {
            inputFile = new BufferedInputStream(new FileInputStream(fileName));
            props.load(inputFile);
            inputFile.close();
            value = props.getProperty(key);
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
        return value;
    }
    
    public static void setKey(String key, String value)
    {
        props.setProperty(key, value);
    }
    
    /**
     * 修改或添加键值对 如果key存在，修改否则添加。
     * @param key
     * @param value
     */
    public static void saveKey(String comment) 
    {
        try {
            File file = new File(fileName);
            if (!file.exists())
            {
                file.createNewFile();
            }
            outputFile = new FileOutputStream(fileName);
            props.store(outputFile, comment);
            outputFile.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        //读取信息
        String server = readKey("server");
        String mydb = readKey("database");
        System.out.println(server);
        System.out.println(mydb);
        
        //写入信息
        setKey("dbuser", "freezq");
        setKey("test", "aaabbbcc");
        saveKey("test sava");
    }
}
