/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zhu
 */
public class Printer implements Runnable
{
    private Cantainer can;
    
    public Printer(Cantainer can)
    {
        this.can = can;
        TestThread.threadNumber++;
    }
    
    public void run()
    {
        while (!can.isOver())
        {
            System.out.println(Thread.currentThread().toString() +
                                "---" + can.next().toString());
        }
        TestThread.threadNumber--;
    }
}
