package FUCK;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OrderMeal {
    private JPanel root;
    private JPanel dishesPanel;
    private JPanel _classPanel;
    private JPanel _class;
    private JPanel dishes;

    public OrderMeal() {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("OrderMeal");
        frame.setContentPane(new OrderMeal().root);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        /**
         * 左侧分类栏
         */
        _class = new JPanel();
        _class.setLayout(new GridLayout(0,1));
//        var data = new SQLiteUtils();
//        var a = data.showAll();
//        JButton[] bts = new JButton[a.size()+50];
//
//        for (int i=0;i<a.size()+50;i++) {
//            bts[i]=new JButton("我暂且蒙古");
//            _class.add(bts[i]);
//        }


        /**
         * 右侧菜品栏
         */
        dishes = new JPanel();
        dishes.setLayout(new GridLayout(0,1));
        JPanel[] jps=new JPanel[50];
        JLabel[] jls = new JLabel[50];
        JLabel[] jls2 = new JLabel[50];
        SpinnerNumberModel[] numberModel=new SpinnerNumberModel[50];
        JSpinner[] numberSpinner = new JSpinner[50];
        BufferedImage[] img= new BufferedImage[50];
        ImageIcon[] icon=new ImageIcon[50];
        JLabel[] label = new JLabel[50];
        JLabel[] dishImg = new JLabel[50];
        for (int i=0;i<50;i++) {
            jps[i] = new JPanel();
            jps[i].setLayout(new FlowLayout(FlowLayout.LEFT,50,10));
            jls[i] = new JLabel("烤嘉然啊嗯");
            jls2[i] = new JLabel("¥114514");

            numberModel[i]=new SpinnerNumberModel(0,0,100,1);
            numberSpinner[i] = new JSpinner(numberModel[i]);

            try {
                img[i] = ImageIO.read(new File("resources/img/default.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            icon[i] = new ImageIcon(img[i]);
            icon[i].setImage(icon[i].getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT));
            label[i] = new JLabel(icon[i]);

            jps[i].add(label[i]);
            jps[i].add(jls[i]);
            jps[i].add(jls2[i]);
            jps[i].add(numberSpinner[i]);

            dishes.add(jps[i]);
        }


    }
}
