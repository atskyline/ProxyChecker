/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zhu
 */
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestThread
{

    /**
     * @param args the command line arguments
     */
    public static volatile int threadNumber = 0;
    
    public static void main(String[] args)
    {
        Object[] test = new Object[]{"123","456","789","qwe","rty",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","123",
                                      "uio","asd","fgh","jkl","zxc","vbn"};
        
        Cantainer can = new Cantainer(test);
        
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
        {
            exec.execute(new Printer(can));
        }
        
        //shutdown()调用前提交所有任务
        exec.shutdown();
        
        while (threadNumber != 0);
        
        System.out.println("Complete");

        
//        try
//        {
//            //5秒还没有执行完就强制结束程序
//            exec.awaitTermination(5, TimeUnit.SECONDS);
//        } catch (InterruptedException ex)
//        {
//            ex.printStackTrace();
//        }
        
    }
}
