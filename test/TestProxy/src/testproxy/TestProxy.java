/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testproxy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
/**
 *
 * @author atskyline
 */
public class TestProxy
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        SocketAddress addr = new
	InetSocketAddress("192.168.0.1", 80);
	Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);

        try
	{
            URL url = new URL("http://www.baidu.com/");
            URLConnection conn = url.openConnection(proxy);

            //设置连接超时
            conn.setConnectTimeout(3000);
            //设置读取超时
            conn.setReadTimeout(5000);
            
            conn.connect();
            
            //获取网页编码
            //很多网页不能使用getContentEncoding();
            String charset = "utf-8";
            String contentType = conn.getContentType();
            if (contentType != null)
            {
                String[] values = contentType.split(";");
                for (String value : values)
                {
                    value = value.trim();
                    
                    if (value.toLowerCase().startsWith("charset="))
                    {
                        charset = value.substring("charset=".length());
                    }
                }
            }
//          System.out.println(charset);

            String line;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),charset));
            while ((line = reader.readLine()) != null)
            {
                //这里因为效率的原因只进行逐行的判断
                //另外因为还需要搜索,所以没有使用StringBuffer
                if (line.indexOf("百度") != -1)
                {
                    //找到关键字
                    System.out.println("成功");
                    break;
                }
            }
            reader.close();

//            如果使用下面的方法有一个比较神奇的问题,还不能确定是很么原因
//            String page = new String();
//            while (reader.ready())
//            {     
//                //这个可用的
//                page = page + reader.readLine();
//                //下面两个是不可用的
//                page += reader.readLine();
//                page.concat(reader.readLine());
//            }
//            System.out.println(page); 
        }catch(SocketTimeoutException ex)
        {
            System.out.println("超时");
        } catch(UnsupportedEncodingException ex)
        {
            System.out.println("编码错误");
            
        } catch (MalformedURLException ex)
        {
            System.out.println("URL 错误");
        } catch (IOException ex)
        {
            System.out.println("网络IO异常");
        } 
    }
}
