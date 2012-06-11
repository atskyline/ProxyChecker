/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestInetAddress 
{

    public static void main(String[] args) 
    {
        try
        {
            SocketAddress addr = 
                    new InetSocketAddress(InetAddress.getByAddress(new byte[]{127,0,0,1}), 8080);  
//            SocketAddress addr = 
//                        new InetSocketAddress("127.0.0.1", 8080);
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        
            System.out.println(proxy);
            System.out.println(((InetSocketAddress)proxy.address()).getAddress().getHostAddress());
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(TestInetAddress.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
