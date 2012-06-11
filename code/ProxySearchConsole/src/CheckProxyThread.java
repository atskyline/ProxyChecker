
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;



public class CheckProxyThread extends Thread
{
    private ProxyPool proxyPool;
    
    public CheckProxyThread(ProxyPool proxyPool)
    {
        this.proxyPool = proxyPool;
    }
    
    public void run()
    {
        ZProxy proxy = null;
        URL url;
        URLConnection conn;
        long startTime;
        
        while (proxyPool.canGetNext())
        {            
            try
            {
                //获取proxy和设置参数
                proxy = proxyPool.getNext();
                if (proxy == null)
                {
                    return;
                }
                url = new URL(ProxySearchConsole.urlString);
                conn = url.openConnection(proxy);
                conn.setConnectTimeout(ProxySearchConsole.connectTimout);
                conn.setReadTimeout(ProxySearchConsole.readTimeout);
                
                System.out.println("start:" + Thread.currentThread() +  proxy);
                
                //开始连接并计时
                startTime = System.currentTimeMillis();
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

                
                //获取网络流
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream(),charset));
                //开始逐行验证
                String line;
                while ((line = reader.readLine()) != null)
                {
                    //这里因为效率的原因只进行逐行的判断
                    //另外因为还需要搜索,所以没有使用StringBuffer
                    if (line.indexOf(ProxySearchConsole.keyword) != -1)
                    {
                        //找到关键字
                        proxy.setState(
                              (int)(System.currentTimeMillis() - startTime));
                        System.out.println("测试成功");

                        return;
                    }
                    
                }
                reader.close();
                //没有找到关键字
                proxy.setState(ZProxy.FAILED);
            } catch(SocketTimeoutException ex)
            {
                proxy.setState(ZProxy.FAILED);
                System.out.println("测试超时");
            } catch(UnsupportedEncodingException ex)
            {
                System.out.println("编码错误");
            } catch (MalformedURLException ex)
            {
                System.out.println("URL 错误");
            } catch (IOException ex)
            {
                proxy.setState(ZProxy.FAILED);
                System.out.println("网络IO异常");
            } finally
            {
                System.out.println("end:" + Thread.currentThread() +  proxy);
                proxyPool.add(proxy);
            }
        }     
    }
}
