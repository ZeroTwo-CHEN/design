package JR.Client.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Vector;

public class ordersJTable {
    private JTable jTable;
    private OrdersTableModel tableModel;
    private Vector<String> colName;

    public ordersJTable() {
    }
}

class detailButtonRenderer implements TableCellRenderer {
    private JPanel panel;

    public detailButtonRenderer() {
        JButton detailButton = new JButton("详情");
        JButton finishButton = new JButton("完成");
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(15);
        panel = new JPanel(layout);
        panel.add(detailButton);
        panel.add(finishButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

class OrdersTableModel extends DefaultTableModel {
    @Override
    public int getColumnCount() {
        return 3;
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
