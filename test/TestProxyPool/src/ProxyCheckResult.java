import java.net.Proxy;

public class ProxyCheckResult 
{
    private Proxy proxy;
    private int state;
    //state > 0  表示代理延时(毫秒)
    //state = -1 代理测试失败
    //state = -2 还未进行验证
    
    public Proxy getProxy()
    {
        return proxy;
    }

    public int getState()
    {
        return state;
    }

    public void setDelay(int state)
    {
        this.state = state;
    }
    
    //如果isSuccess为false,delay设置的值会被忽略
    public ProxyCheckResult(Proxy proxy,int state)
    {
        this.proxy = proxy;
        this.state = state;
    }

}
