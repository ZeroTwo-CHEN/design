package jr.client.admin;

import jr.jdbc.DataUtils;
import jr.model.Dish;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Date;

public class AddDish extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton imgButton;
    private JTextField classification;
    private JPanel imgPanel;
    private JTextField price;
    private JTextField name;

    private Path source;
    private Path target = Path.of("resources/img/default.jpg");
    private boolean isAddImg = false;


    public AddDish() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // 单击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // 在此处添加您的代码
        //把文件复制到程序所在的文件夹
        if (isAddImg) {
            try {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        DataUtils.addDish(name.getText(), Double.parseDouble(price.getText()), classification.getText(), target.toString());
        dispose();
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        dispose();
    }

    public static void main(String[] args) {
        AddDish dialog = new AddDish();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        //图片
        imgPanel = new JPanel();
        ImageIcon icon = new ImageIcon("resources/img/default.jpg");
        icon.setImage(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        JLabel imgLabel = new JLabel(icon);
        imgPanel.add(imgLabel);

        //按钮
        //如果保存就复制图片到resource目录
        imgButton = new JButton();
        imgButton.addActionListener(e -> {
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
            int result = fileChooser.showOpenDialog(imgPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                //如果点了确定则获取文件路径
                File file = fileChooser.getSelectedFile();
                //获取文件后缀
                String fileName = file.getName();
                String fileExt = fileName.substring(fileName.lastIndexOf("."));
                //先暂存源目录和目标目录
                source = file.toPath();
                Date date = new Date();
                target = new File("resources/img/" + date.getTime() / 1000 + fileExt).toPath();

                isAddImg = true;

                //更新显示图片
                imgLabel.setIcon(Dish.imgToIcon(source.toString()));
            }
        });
    }
}
