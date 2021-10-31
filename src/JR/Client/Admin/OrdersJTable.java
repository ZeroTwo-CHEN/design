package JR.Client.Admin;

import JR.Model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class OrdersJTable {
    private final JTable table;
    private final OrdersTableModel tableModel;
    private final Vector<String> colName;
    private final OperationRenderer operationRenderer;
    private final OperationEditor operationEditor;
    private Vector<Vector<Object>> data = new Vector<>();
    private LinkedBlockingQueue<Order> orderQueue;
    private ArrayList<Order> orderArrayList;

    public OrdersJTable() {
        this.orderArrayList = new ArrayList<>();
        colName = new Vector<>();
        colName.add("桌号");
        colName.add("订单价格");
        colName.add("订单提交时间");
        colName.add("操作");
        tableModel = new OrdersTableModel();
        table = new JTable(tableModel);
        operationRenderer = new OperationRenderer();
        operationEditor = new OperationEditor(this);
        table.setRowHeight(50);
        table.setRowSelectionAllowed(false);
        table.setFont((new Font("微软雅黑", Font.PLAIN, 20)));
    }

    public void setOrderQueue(LinkedBlockingQueue<Order> orderQueue) {
        this.orderQueue = orderQueue;
    }

    public void init() {
        this.orderQueue = Server.getOrderQueue();
        reloadJTable();
    }

    public void reloadJTable() {
        data.removeAllElements();

        orderArrayList.clear();
        orderArrayList.addAll(orderQueue);

        for (Order order : orderArrayList) {
            Vector<Object> a = new Vector<>();
            a.add(order.getTableId());
            a.add(order.getSumPrice());
            a.add(order.getDate());
            a.add("");
            data.add(a);
        }

        tableModel.setDataVector(data, colName);
        table.getColumnModel().getColumn(3).setCellRenderer(operationRenderer);
        table.getColumnModel().getColumn(3).setCellEditor(operationEditor);
    }

    public JTable getTable() {
        return table;
    }

    public ArrayList<Order> getOrderArrayList() {
        return orderArrayList;
    }

    public OrdersTableModel getTableModel() {
        return tableModel;
    }
}

class OperationRenderer implements TableCellRenderer {
    private final JPanel panel;

    public OperationRenderer() {
        JButton detailButton = new JButton("查看");
        JButton finishButton = new JButton("完成");
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(5);
        panel = new JPanel(layout);
        panel.add(detailButton);
        panel.add(finishButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

class OperationEditor extends DefaultCellEditor {
    private final JPanel panel;

    public OperationEditor(OrdersJTable ordersJTable) {
        super(new JTextField());
        setClickCountToStart(1);
        OperationEditor.this.fireEditingCanceled();
        JButton detailButton = new JButton("查看");
        JButton finishButton = new JButton("完成");
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(5);
        panel = new JPanel(layout);

        detailButton.addActionListener(e -> {
            int index = ordersJTable.getTable().convertRowIndexToModel(ordersJTable.getTable().getSelectedRow());
            DetailPanel detailPanel = new DetailPanel(ordersJTable.getOrderArrayList().get(index),index,ordersJTable);
            detailPanel.setLocationRelativeTo(panel);
            detailPanel.setTitle("详情");
            detailPanel.pack();
            detailPanel.setVisible(true);
        });

        finishButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(panel, "确认完成吗？", "提示", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                int index = ordersJTable.getTable().convertRowIndexToModel(ordersJTable.getTable().getSelectedRow());
                boolean isDelete = Server.getOrderQueue().remove(ordersJTable.getOrderArrayList().get(index));
                ordersJTable.getTableModel().removeRow(index);
                ordersJTable.reloadJTable();
                Server.setOrderFlag(true);
            }
        });

        panel.add(detailButton);
        panel.add(finishButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}

class OrdersTableModel extends DefaultTableModel {
    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}
