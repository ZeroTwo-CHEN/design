package JR.Client.Admin;

import JR.Model.Dish;
import JR.Model.Order;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;

public class DetailPanel extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable dishesTable;
    private JPanel infoPanel;
    private final Order order;
    private final OrdersJTable ordersJTable;
    private final int index;

    public DetailPanel(Order order, int index, OrdersJTable ordersJTable) {
        this.order = order;
        this.index = index;
        this.ordersJTable = ordersJTable;

        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // 在此处添加您的代码
        int result = JOptionPane.showConfirmDialog(contentPane, "确认完成吗？", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            boolean isDelete = Server.getOrderQueue().remove(ordersJTable.getOrderArrayList().get(index));
            ordersJTable.getTableModel().removeRow(index);
            ordersJTable.reloadJTable();
            Server.setOrderFlag(true);
            dispose();
        }
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        infoPanel = new JPanel(new GridLayout(1, 4));
        infoPanel.add(new JLabel("桌号"));
        infoPanel.add(new JLabel(String.valueOf(order.getTableId())));
        infoPanel.add(new JLabel("订单时间"));
        infoPanel.add(new JLabel(order.getDate()));

        String[] columnNames = {"ID", "菜名", "数量"};

        Object[][] data = new Object[order.getDishNumTreeMap().size()][4];

        int i = 0;
        for (Map.Entry<Dish, Integer> entry : order.getDishNumTreeMap().entrySet()) {
            data[i][0] = order.getTableId();
            data[i][1] = entry.getKey().getName();
            data[i++][2] = entry.getValue();
        }

        dishesTable = new JTable(data, columnNames);
        dishesTable.setRowHeight(25);
        dishesTable.setEnabled(false);
    }
}
