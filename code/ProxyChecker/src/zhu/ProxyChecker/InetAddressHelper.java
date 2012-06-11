package zhu.ProxyChecker;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressHelper 
{
    public static Boolean isVaildAddress(String address)
    {
        String[] array = address.split("[.]");
        if (array.length != 4)
        {
            return false;
        }
        
        try
        {
            for (String string : array)
            {
                //这里不能使用Byte.valueOf(string);因为java的Byte是有符号的
                int test = Integer.valueOf(string);
                if (test < 0 || test > 255)
                {
                    return false;
                }
            }
        } catch (NumberFormatException e)
        {
            return false;
        }
        
        return true;
    }
    
    
    public static InetAddress getInetAddress(String address) 
                                        throws UnknownHostException
    {
        if (!isVaildAddress(address))
        {
            throw new UnknownHostException(address);
        }
        
        return InetAddress.getByName(address);
    }
}
