package zhu.ProxyChecker;


import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;

public class ZProxy extends Proxy
{
    private int state;
    //state > 0  表示代理延时(毫秒)
    //state = -1 代理测试失败
    //state = -2 正在进行验证
    //state = -3 还未进行验证
    
    public static int FAILED = -1;
    public static int CHECKING = -2;
    public static int UNCHECK = -3;
    
    public ZProxy(InetAddress address, int port, Proxy.Type type, int state)
    {
        super(type,new InetSocketAddress(address, port));
        this.state = state;
    }
    
    public ZProxy(byte[] addBytes, int port, Proxy.Type type, int state) 
                                        throws UnknownHostException
    {
        super(type,new InetSocketAddress(
                            InetAddress.getByAddress(addBytes), port));
        this.state = state;
    }
    
    public String getAddressString()
    {
        return ((InetSocketAddress)this.address())
                            .getAddress().getHostAddress();
    }
    
    public String getTypeString()
    {
        return this.type().toString();
    }
    
    public int getPort()
    {
        return ((InetSocketAddress)this.address()).getPort();
    }
    
    public int getState()
    {
        return state;
    }
    
    public void setState(int state)
    {
        this.state = state;
    }
    
    public String toString()
    {
        return getAddressString() + 
                "@" + getPort() +
                "@" + getTypeString() +
                "@" + getState();
    }
}
