
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class TestFileIO3 
{

    public static void main(String[] args) 
    {
        try
        {
            Config test = new Config("config.ini");
            System.out.println(test.readKey("server"));
            System.out.println(test.readKey("qwerrewtdfs"));
            test.setKey("test", "12345");
            test.save();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

}
