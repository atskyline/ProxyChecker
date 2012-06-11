/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Zhu
 */
public class Cantainer
{

    private Object[] objects;
    private int index;
    
    public synchronized Boolean isOver()
    {
        return (index >= objects.length);
    }
    
    public Cantainer(Object[] objects)
    {
        this.objects = objects;
        index = 0;
    }
    
    public synchronized Object next()
    {
        if(!isOver())
        {
            return objects[index++];
        }
        else
        {
            return null;
        }
    }
}
