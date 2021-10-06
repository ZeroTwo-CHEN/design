package FUCK.Client;

import FUCK.DataUtils;

import javax.swing.*;

public class AdminClient {
    private JPanel root;
    private JPanel dishesPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JPanel JPanel;
    private JScrollPane dishesScrollPane;
    private JPanel __dishesPanel;

    public AdminClient() {
        DataUtils data = new DataUtils();
        searchButton.addActionListener(e -> {
            dishesPanel = DishesPanelUtils.paintDishesPanel(data,searchTextField.getText());
            __dishesPanel.removeAll();
            __dishesPanel.repaint();
            __dishesPanel.add(dishesPanel);
            __dishesPanel.revalidate();
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("AdminClient");
        frame.setContentPane(new AdminClient().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        //数据实例
        DataUtils data = new DataUtils();


        //菜品栏
        __dishesPanel = new JPanel();//辅助用面板 搜索后刷新面板的辅助
        dishesPanel=DishesPanelUtils.paintDishesPanel(data,"");
        __dishesPanel.add(dishesPanel);
    }
}
