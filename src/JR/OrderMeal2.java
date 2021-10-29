package JR;

import javax.swing.*;
import java.awt.*;

public class OrderMeal2 extends JFrame {
    private JPanel mainPanel;
    private JPanel _class;
    private JPanel dishes;

    public OrderMeal2() {
        mainPanel = new JPanel();
        _class = new JPanel();
        dishes = new JPanel();
        _class.setPreferredSize(new Dimension(200, 400));
        var layout = new GridLayout(10, 1);
//        layout.setVgap(10);
        _class.setLayout(layout);
        _class.add(new JLabel("分类"));
        dishes.add(new JLabel("菜品"));
        mainPanel.add(_class, BorderLayout.WEST);
        mainPanel.add(dishes, BorderLayout.CENTER);
        add(mainPanel);
        pack();
    }

    public static void main(String... a) {
        var frame = new OrderMeal2();
        frame.setTitle("点餐");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
