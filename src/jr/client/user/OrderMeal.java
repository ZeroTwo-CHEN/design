package jr.client.user;

import jr.jdbc.DataUtils;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class OrderMeal {
    private JPanel root;
    private JTable dishesTable;
    private JTextField searchTextField;
    private JButton carButton;
    private JButton resetButton;
    private JComboBox<String> classComboBox;
    private JTextField tableIdField;
    private JTextField timeField;
    private static Client client;

    public static void main(String[] args) {
        client = new Client();

//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("客户端");
//            frame.setContentPane(new OrderMeal().root);
//
//            frame.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosing(WindowEvent e) {
//                    super.windowClosing(e);
//                    client.logout();
//                }
//            });
//
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(700, 600);
//            frame.setVisible(false);
//
//            ReadIdAndIP readIdAndIP = new ReadIdAndIP(client, frame);
//            readIdAndIP.setLocationRelativeTo(null);
//            readIdAndIP.pack();
//            readIdAndIP.setTitle("初始化");
//            readIdAndIP.setVisible(true);
//        });

        ReadIdAndIP readIdAndIP = new ReadIdAndIP(client);
        readIdAndIP.setLocationRelativeTo(null);
        readIdAndIP.pack();
        readIdAndIP.setTitle("初始化");
        readIdAndIP.setVisible(true);
    }

    public JPanel getRoot() {
        return root;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        Font font = new Font("Microsoft YaHei UI", Font.PLAIN, 18);

        CustomerDishesJTable customerDishesJTable = new CustomerDishesJTable();
        dishesTable = customerDishesJTable.getTable();

        //搜索框,分类下拉框
        searchTextField = new JTextField();
        classComboBox = new JComboBox<>(DataUtils.allClass());
        classComboBox.setFont(font);

        searchTextField.addActionListener(e -> customerDishesJTable.searchFieldFilter(searchTextField.getText()));

        classComboBox.addActionListener(e -> customerDishesJTable.classFilter((String) Objects.requireNonNull(classComboBox.getSelectedItem())));

        //重置按钮
        resetButton = new JButton();
        resetButton.setFont(font);
        resetButton.addActionListener(e -> customerDishesJTable.reset());

        //购物车
        carButton = new JButton();
        carButton.setFont(font);
        carButton.addActionListener(e -> {
            customerDishesJTable.searchFieldFilter("");
            classComboBox.setSelectedItem("全部");
            ShoppingCar sC = new ShoppingCar(customerDishesJTable, client);
            sC.setLocationRelativeTo(searchTextField);
            sC.setTitle("购物车");
            sC.pack();
            sC.setVisible(true);
        });

        tableIdField = new JTextField(client.getId());

        timeField = new JTextField();
        Timer timer = new Timer(1000, e -> {
            timeField.setText(new SimpleDateFormat("HH:mm").format(new Date()));
            tableIdField.setText(String.valueOf(client.getId()));
        });
        timer.start();
    }
}
