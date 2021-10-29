package JR.Model;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Dish implements Comparable<Dish>, Serializable {
    private int id;
    private String name;
    private double price;
    private ImageIcon icon;
    private String classification;
    private String url;

    public Dish(int id, String name, double price, String classification, String url) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.classification = classification;
        this.icon = Dish.imgToIcon(url);
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ImageIcon getImageIcon() {
        return icon;
    }

    public String getClassification() {
        return classification;
    }

    public String getUrl() {
        return url;
    }

    public static ImageIcon imgToIcon(String url) {
        //在使用了Buffer后出现读图错误的情况，暂时注释掉
//        BufferedImage img = null;
//        try {
//            if (!url.equals(""))
//                img = ImageIO.read(new File(url));
//            else
//                img = ImageIO.read(new File("resources/img/default.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ImageIcon icon = new ImageIcon(url);
        icon.setImage(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        return icon;
    }

    public static void updateDies(Dish dish, String name, double price, String classification, String url) {
        //虽然Static修饰的方法不属于类本身，但是还是在private（本类）范围内，所以可以访问到类的私有变量
        dish.name = name;
        dish.price = price;
        dish.classification = classification;
        dish.icon = imgToIcon(url);
    }

    @Override
    public int compareTo(Dish o) {
        if (o.id < this.id) {
            return 1;
        } else {
            return -1;
        }
    }
}
