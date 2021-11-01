package jr.client.user;

import jr.model.Dish;
import jr.model.Message;
import jr.model.MessageType;
import jr.model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.TreeMap;

public class ShoppingCar extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable listTable;
    private JTextField sumPriceField;
    private double sumPrice;
    private final TreeMap<Dish, Integer> dishNumTreeMap;
    private final Client client;

    public ShoppingCar(CustomerDishesJTable customerDishesJTable, Client client) {
        this.client = client;

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        dishNumTreeMap = new TreeMap<>();
        Dish[] allDishes = customerDishesJTable.getDishes();
        JTable jTable = customerDishesJTable.getTable();
        //customerDishesJTable.newFilter("");
        for (int i = 0; i < jTable.getRowCount(); i++) {
            int num = (int) jTable.getValueAt(i, 5);
            //把获取到的表格索引改为数据模型中的索引
            int modelIndex = jTable.convertRowIndexToModel(i);
            if (num > 0) {
                dishNumTreeMap.put(allDishes[modelIndex], num);
            }
        }
        printList();


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
        if (!dishNumTreeMap.isEmpty()) {
            Order order = new Order(client.getId(), sumPrice, dishNumTreeMap);
            Message message = new Message(MessageType.TYPE_ORDER, client.getId(), order);
            int num = client.sendOrder(message);
            JOptionPane.showMessageDialog(contentPane,
                    "提交订单成功\n前方还有" + num + "个订单\n请耐心等待哦", "提示", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }else {
            JOptionPane.showMessageDialog(contentPane, "您还没选择菜品", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    private void printList() {
        sumPrice = 0;
        String[] colName = {"菜名", "单价", "数量"};
        Object[][] data = new Object[dishNumTreeMap.size()][3];
        int i = 0;
        for (Map.Entry<Dish, Integer> entry : dishNumTreeMap.entrySet()) {
            Dish dish = entry.getKey();
            data[i][0] = dish.getName();
            data[i][1] = dish.getPrice();
            data[i++][2] = entry.getValue();
            sumPrice += dish.getPrice() * entry.getValue();
        }
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setDataVector(data, colName);
        listTable.setModel(model);
        listTable.setRowSelectionAllowed(false);
        listTable.setRowHeight(30);
        sumPriceField.setText(String.valueOf(sumPrice));
    }

}
