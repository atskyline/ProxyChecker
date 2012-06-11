/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class TestProxyPool2 
{

    public static void main(String[] args) 
    {
        String test1 = "127.0.0.1";
        String test2 = "327.0.0.1"; 
        
        Boolean result1 = InetAddressHelper.isVaildAddress(test1);
        Boolean result2 = InetAddressHelper.isVaildAddress(test2);

        System.out.println(result1);
        System.out.println(result2);

    }

}
