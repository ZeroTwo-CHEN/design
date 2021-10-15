package FUCK.Client;

import javax.swing.*;

public class AdminClient {
    private JPanel root;
    private JTextField searchTextField;
    private JPanel JPanel;
    private JScrollPane dishesScrollPane;
    private JTable dishesTable;
    private JButton addButton;

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
        //菜品栏
        DishesJTable jTable = new DishesJTable();
        dishesTable= jTable.getTable();

        //搜索框
        searchTextField = new JTextField();
        searchTextField.addActionListener(e -> jTable.newFilter(searchTextField.getText()));

        //增加菜品
        addButton = new JButton();
        addButton.addActionListener(e -> {
            AddDish addDish = new AddDish();
            addDish.setLocationRelativeTo(null);
            addDish.setTitle("新增菜品");
            addDish.pack();
            addDish.setVisible(true);
            jTable.reloadJTable();
        });
    }
}
