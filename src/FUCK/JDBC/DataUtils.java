package FUCK.JDBC;

import FUCK.Model.Dish;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataUtils {
    private static SQLiteUtils sqLiteUtils;
    private static PreparedStatement ps;
    private ResultSet rs;
    private int numOfDishes;

    public DataUtils() {
        sqLiteUtils = new SQLiteUtils();
        updateNumOfDishes();
    }

    private void updateNumOfDishes() {
        try {
            String sql = String.format("SELECT COUNT(ID) FROM DISHES;");
            ps = sqLiteUtils.getStatement(sql);
            rs = ps.executeQuery();
            numOfDishes = rs.getInt("COUNT(ID)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sqLiteUtils.close();
        }
    }

    private void updateNumOfDishes(String word) {
        try {
            String sql = String.format("SELECT COUNT(ID) FROM DISHES WHERE NAME GLOB '*%s*' OR CLASS GLOB '*%s*';", word, word);
            ps = sqLiteUtils.getStatement(sql);
            rs = ps.executeQuery();
            numOfDishes = rs.getInt("COUNT(ID)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sqLiteUtils.close();
        }
    }

    public static void addDish(String name, double price, String classification, String url){
        String sql = String.format("INSERT INTO DISHES (NAME,PRICE,CLASS,URL) VALUES ('%s',%f,'%s','%s')", name, price, classification, url);
        try {
            ps = sqLiteUtils.getStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            sqLiteUtils.close();
        }
    }

    public static void updateSQLDish(int id, String name, double price, String classification, String url) {
        try {
            String sql = String.format("UPDATE DISHES SET NAME = '%s', " +
                    "PRICE = %f, CLASS = '%s', URL = '%s' WHERE ID = %d;", name, price, classification, url, id);
            ps = sqLiteUtils.getStatement(sql);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sqLiteUtils.close();
        }
    }

    public Dish[] showDishes(String word) {
        if (word.equals(""))
            updateNumOfDishes();
        else
            updateNumOfDishes(word);
        Dish[] result = new Dish[numOfDishes];
        try {
            String sql;
            if (word.equals(""))
                sql = "SELECT * FROM DISHES;";
            else
                sql = String.format("SELECT * FROM DISHES WHERE NAME GLOB '*%s*' OR CLASS GLOB '*%s*';", word, word);
            ps = sqLiteUtils.getStatement(sql);
            rs = ps.executeQuery();
            for (int i = 0; i < numOfDishes; i++) {
                rs.next();
                result[i] = new Dish(rs.getInt("ID"), rs.getString("NAME"),
                        rs.getDouble("PRICE"), rs.getString("CLASS"),
                        rs.getString("URL"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            sqLiteUtils.close();
        }
        return result;
    }

    //弹出更新信息的窗口
    public static void updateDish(Dish dish, Frame owner, Component parentComponent) {
        JDialog dialog = new JDialog(owner, "更新信息", true);
        dialog.setSize(600, 300);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentComponent);

        //是否更新图片
        final Boolean[] isUpdateImg = {false};

        //如果保存就复制图片到resource目录
        final Path[] source = {null};
        final Path[] target = {null};
        target[0] = Path.of(dish.getUrl());

        //暂时储存新信息
        int id = dish.getId();
        String name = dish.getName();
        double price = dish.getPrice();
        String classification = dish.getClassification();
        String url = "";


        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 0));

        //点击图片即可更换
        final JLabel[] image = {new JLabel(dish.getImageIcon())};
        image[0].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser fileChooser = new JFileChooser();

                //默认显示当前文件夹
                fileChooser.setCurrentDirectory(new File("."));

                //设置为只选文件夹
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                //不允许多选
                fileChooser.setMultiSelectionEnabled(false);

                //设置文件过滤器
                fileChooser.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png)", "jpg", "png"));

                //获取选择状态
                int result = fileChooser.showOpenDialog(panel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    //如果点了确定则获取文件路径
                    File file = fileChooser.getSelectedFile();
                    //获取文件后缀
                    String fileName = file.getName();
                    String fileExt = fileName.substring(fileName.lastIndexOf("."));
                    //先暂存源目录和目标目录
                    source[0] = file.toPath();
                    target[0] = new File("resources/img/" + dish.getId() + fileExt).toPath();

                    //更新显示图片
                    image[0].setIcon(Dish.imgToIcon(source[0].toString()));

                    //设置状态
                    isUpdateImg[0] = true;
                }
            }
        });

        //修改姓名
        JTextField nameField = new JTextField(name, 10);
        nameField.setEditable(true);

        //修改价格
        JTextField priceField = new JTextField(String.valueOf(dish.getPrice()), 5);
        priceField.setEditable(true);

        //修改分类
        JTextField classificationField = new JTextField(dish.getClassification(), 10);
        classificationField.setEditable(true);

        panel.add(image[0]);
        panel.add(nameField);
        panel.add(priceField);
        panel.add(classificationField);

        //顶部提示
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 30));
        topPanel.add(new JLabel("图片"));
        topPanel.add(new JLabel("菜名"));
        topPanel.add(new JLabel("价格"));
        topPanel.add(new JLabel("分类"));

        //保存和取消按钮
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 30));
        JButton sava = new JButton("保存");
        JButton cancel = new JButton("取消");

        sava.addActionListener(e -> {
            //更新数据库
            updateSQLDish(dish.getId(), nameField.getText(), Double.parseDouble(priceField.getText()),
                    classificationField.getText(), target[0].toString());

            if (isUpdateImg[0])
            //把文件复制到程序所在的文件夹
            {
                try {
                    Files.copy(source[0], target[0], StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            //更新内存
            Dish.updateDies(dish, nameField.getText(), Double.parseDouble(priceField.getText()),
                    classificationField.getText(), target[0].toString());

            dialog.dispose();
        });

        cancel.addActionListener(e -> dialog.dispose());

        bottomPanel.add(sava);
        bottomPanel.add(cancel);

        //根面板
        JPanel rootPanel = new JPanel(new BorderLayout());
        rootPanel.add(BorderLayout.NORTH, topPanel);
        rootPanel.add(BorderLayout.CENTER, panel);
        rootPanel.add(BorderLayout.SOUTH, bottomPanel);

        dialog.setContentPane(rootPanel);
        dialog.setVisible(true);
    }

    public static void deleteDish(Dish dish, Component parentComponent) {
        int result = JOptionPane.showConfirmDialog(parentComponent,
                "确认删除？", "提示", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.out.println("yes");
            String sql = String.format("DELETE FROM DISHES WHERE ID = %d", dish.getId());
            try {
                ps = sqLiteUtils.getStatement(sql);
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                sqLiteUtils.close();
            }
        }
    }
}
