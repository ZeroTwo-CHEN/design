package JR.Client.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminClient {
    private JPanel root;
    private JTextField searchTextField;
    private JPanel managementPanel;
    private JScrollPane dishesScrollPane;
    private JTable dishesTable;
    private JButton addButton;
    private JTabbedPane tabbedPane1;
    private JPanel logPanel;
    private JTextArea logArea;
    private JScrollPane orderPanel;
    private JPanel statusPanel;
    private JTable orderTable;
    private JTextField numOfOrdersField;
    private JTextField numOfClientsField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("AdminClient");
            frame.setContentPane(new AdminClient().root);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 600);
            frame.setVisible(true);
        });
    }

    private void createUIComponents() {
        //实时订单面板
        statusPanel = new JPanel(new GridLayout(1, 4));
        statusPanel.add(new JLabel("当前订单数："));
        numOfOrdersField = new JTextField("0");
        numOfOrdersField.setEditable(false);
        numOfClientsField = new JTextField("0");
        statusPanel.add(numOfOrdersField);
        numOfClientsField.setEditable(false);
        statusPanel.add(new JLabel("当前在线客户端数："));
        statusPanel.add(numOfClientsField);

        OrdersJTable ordersJTable = new OrdersJTable();
        orderTable = ordersJTable.getTable();

        //菜品管理面板
        //菜品栏
        DishesJTable jTable = new DishesJTable();
        dishesTable = jTable.getTable();

        //搜索框
        searchTextField = new JTextField();
        searchTextField.addActionListener(e -> jTable.newFilter(searchTextField.getText()));

        //增加菜品
        addButton = new JButton();
        addButton.addActionListener(e -> {
            AddDish addDish = new AddDish();
            addDish.setLocationRelativeTo(searchTextField);
            addDish.setTitle("新增菜品");
            addDish.pack();
            addDish.setVisible(true);
            jTable.reloadJTable();
        });

        //服务器线程
        logArea = new JTextArea();
        Thread serverThread = new Thread(() -> {
            new Server(logArea);
            //ordersJTable.setOrderQueue(Server.getOrderQueue());
            //以下语句不可达到
            System.out.println("fas");
        });
        serverThread.start();

        try {
            Thread.sleep (1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ordersJTable.setOrderQueue(Server.getOrderQueue());

        Thread thread=new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    if (Server.isOrderFlag()) {
                        ordersJTable.reloadJTable();
                        numOfOrdersField.setText(String.valueOf(Server.getOrderQueue().size()));
                        Server.setOrderFlag(false);
                    }
                    if(Server.isClientFlag()){
                        numOfClientsField.setText(String.valueOf(Server.getNumOfClients()));
                        Server.setClientFlag(false);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
