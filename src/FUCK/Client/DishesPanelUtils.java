package FUCK.Client;

import FUCK.DataUtils;
import FUCK.Model.Dish;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DishesPanelUtils {
    public static JPanel paintDishesPanel(DataUtils data,String word){
        JPanel dishesPanel = new JPanel();
        dishesPanel.setLayout(new GridLayout(0, 1));
        Dish[] dishes = data.showDishes(word);
        int num = dishes.length;

        JPanel[] dishPanel = new JPanel[num];
        JLabel[] images = new JLabel[num];
        //JLabel[] ids = new JLabel[num];
        JLabel[] name = new JLabel[num];
        JLabel[] price = new JLabel[num];
        JLabel[] classification = new JLabel[num];

        for (int i = 0; i < num; i++) {
            System.out.println(dishes[i].getName());
            dishPanel[i] = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 10));
            //ids[i] = new JLabel(String.valueOf(dishes[i].getId()));
            images[i] = new JLabel(dishes[i].getImageIcon());
            name[i] = new JLabel(dishes[i].getName());
            price[i] = new JLabel("¥ " + dishes[i].getPrice());
            classification[i] = new JLabel(dishes[i].getClassification());

            dishPanel[i].add(images[i]);
            //dishPanel[i].add(ids[i]);
            dishPanel[i].add(name[i]);
            dishPanel[i].add(price[i]);
            dishPanel[i].add(classification[i]);

            int finalI = i;
            dishPanel[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    data.updateDish(dishes[finalI],
                            (JFrame) dishPanel[finalI].getRootPane().getParent(),
                            dishPanel[finalI]);
                    //重绘面板
                    dishPanel[finalI].removeAll();
                    dishPanel[finalI].repaint();
                    dishPanel[finalI].add(new JLabel(dishes[finalI].getImageIcon()));
                    dishPanel[finalI].add(new JLabel(dishes[finalI].getName()));
                    dishPanel[finalI].add(new JLabel("¥ " + dishes[finalI].getPrice()));
                    dishPanel[finalI].add(new JLabel(dishes[finalI].getClassification()));
                    dishPanel[finalI].revalidate();//不可或缺
                }
            });

            dishesPanel.add(dishPanel[i]);
        }
        return dishesPanel;
    }
}
