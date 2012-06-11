
import java.io.*;
import java.net.Proxy;
import java.util.LinkedList;

public class ResultIO 
{
    public static LinkedList<ZProxy> readResult(String fileName)
    {
        LinkedList<ZProxy> list = new LinkedList<>();
        
        try
        {
            BufferedReader inReader = new BufferedReader(
                                        new FileReader(fileName));
            while(inReader.ready())
            {
                String line = inReader.readLine();
                String[] elements = line.split("@");
                list.add(new ZProxy(
                                InetAddressHelper.getInetAddress(elements[0]),
                                Integer.parseInt(elements[1]),
                                Proxy.Type.valueOf(elements[2]),
                                Integer.parseInt(elements[3])));
            }
            inReader.close();
            
        } catch (FileNotFoundException ex)
        {
            System.out.println("文件不存在");
            ex.printStackTrace();
        } catch(IOException ex)
        {
            ex.printStackTrace();
        }
        
        return list;
    }
    
    public static void WriteReult(LinkedList<ZProxy> list, String fileName)
    {
        try
        {
            BufferedWriter outWriter = 
                    new BufferedWriter(new FileWriter(fileName, false));
            
            for (ZProxy proxy : list)
            {
                String line = proxy.getAddressString() +
                              "@" + proxy.getPort() + 
                              "@" + proxy.getTypeString() +
                              "@" + proxy.getState() + "\n";
                outWriter.write(line);
            }
            outWriter.close();
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
