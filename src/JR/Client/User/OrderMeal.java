package JR.Client.User;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OrderMeal {
    private JPanel root;
    private JTable dishesTable;
    private JTextField searchTextField;
    private JButton carButton;
    private static Client client;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("OrderMeal");
            frame.setContentPane(new OrderMeal().root);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    client.logout();
                }
            });

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 600);
            frame.setVisible(true);
        });
    }

    //TODO: 重置所有选择的按钮
    private void createUIComponents() {
        // TODO: place custom component creation code here
        client = new Client();


        CustomerDishesJTable customerDishesJTable = new CustomerDishesJTable();
        dishesTable = customerDishesJTable.getTable();

        carButton = new JButton();
        carButton.addActionListener(e -> {
            shoppingCar sC = new shoppingCar(customerDishesJTable, client);
            sC.setLocationRelativeTo(root);
            sC.setTitle("购物车");
            sC.pack();
            sC.setVisible(true);
        });


        readIdAndIP readIdAndIP = new readIdAndIP(client);
        readIdAndIP.setLocationRelativeTo(null);
        readIdAndIP.pack();
        readIdAndIP.setTitle("初始化");
        readIdAndIP.setVisible(true);
    }
}
