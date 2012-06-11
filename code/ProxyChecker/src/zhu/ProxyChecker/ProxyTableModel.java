
package zhu.ProxyChecker;

import javax.swing.table.AbstractTableModel;

public class ProxyTableModel extends AbstractTableModel
{
    private static ProxyTableModel model;
    
    private ProxyPool proxyPool; 
    private String[] head;
    
    static
    {
        model = new ProxyTableModel();
    }
    
    public static ProxyTableModel getModel()
    {
        return model;
    }
    
    private ProxyTableModel()
    {
        super();
        proxyPool = Application.proxyPool;
        head = new String[]{"IP地址","端口","协议","结果(延时ms)"};
    }
    
    
    
    public void refresh()
    {
        //把Application.proxyPool的全部内容刷新到表中
        
    }
    
    
    @Override
    public boolean isCellEditable(int row, int column) 
    {
        return false;
    }
    
    @Override
    public int getRowCount()
    {
        return proxyPool.getSize();
    }

    @Override
    public int getColumnCount()
    {
        return head.length;
    }
    
    @Override
    public String getColumnName(int column)
    {
        if (column >= 0 && column < head.length)
        {
            return head[column];
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return proxyPool.getValueAt(rowIndex, columnIndex);
    }
    
}
