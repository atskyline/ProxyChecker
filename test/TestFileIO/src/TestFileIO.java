
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.LinkedList;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class TestFileIO 
{

    public static void main(String[] args)
    {
        LinkedList<Proxy> list = new LinkedList<>();
        try
        {
            BufferedReader inReader = new BufferedReader(
                                        new FileReader("result.txt"));
            while(inReader.ready())
            {
                String line = inReader.readLine();
                String[] elements = line.split("@");
                list.add(new Proxy(Proxy.Type.valueOf(elements[2]), 
                            (SocketAddress) new InetSocketAddress(elements[0], 
                                            Integer.parseInt(elements[1]))));
            }
            inReader.close();
            System.out.println("Read Complete");
        } catch (FileNotFoundException ex)
        {
            System.out.println("未找到文件");
            ex.printStackTrace();
        } catch(IOException ex)
        {
            ex.printStackTrace();
        }
        ///////////////////////////////////////////////////////////////////////
        for (Proxy proxy : list)
        {
            System.out.println(proxy);
        }
        ///////////////////////////////////////////////////////////////////////
        try
        {
            BufferedWriter outWriter = 
                    new BufferedWriter(new FileWriter("result2.txt", false));
            while (!list.isEmpty())
            {    
                String line;
                Proxy proxy = list.remove();
                InetSocketAddress addr = (InetSocketAddress)proxy.address();
                line = addr.getAddress().getHostAddress() + 
                        "@" + addr.getPort()+ "@" + proxy.type() + "\n";
                outWriter.write(line);
            }
            outWriter.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
