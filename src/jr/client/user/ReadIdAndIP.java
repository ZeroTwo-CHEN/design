package jr.client.user;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.StringTokenizer;

public class ReadIdAndIP extends JFrame {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JFormattedTextField tableIdField;
    private JFormattedTextField ipField;
    private JFormattedTextField portField;
    private final Client client;
    private final JFrame frame;

    public ReadIdAndIP(Client client, JFrame frame) {
        this.client = client;
        this.frame = frame;

        setContentPane(contentPane);
        //setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        //setUndecorated(true);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // 点击 X 时调用 onCancel()
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
        if (check()) {
            if (client.read(Math.toIntExact((Long) tableIdField.getValue()),
                    ipField.getText(),
                    Math.toIntExact((Long) tableIdField.getValue()))) {
                //已保存client.setId(Math.toIntExact((Long) tableIdField.getValue()));
                frame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(contentPane, "该桌号已被注册", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void onCancel() {
        // 必要时在此处添加您的代码
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        InternationalFormatter internationalFormatter = new InternationalFormatter(NumberFormat.getIntegerInstance()) {
            private final DocumentFilter filter = new IntFilter();

            @Override
            protected DocumentFilter getDocumentFilter() {
                return filter;
            }
        };
        tableIdField = new JFormattedTextField(internationalFormatter);
        portField = new JFormattedTextField(internationalFormatter);
        ipField = new JFormattedTextField(new IPAddressFormatter());
    }

    private boolean check() {
        if ("".equals(tableIdField.getText().trim())
                || "".equals(ipField.getText().trim())
                || "".equals(portField.getText().trim())) {
            JOptionPane.showMessageDialog(contentPane, "信息不能为空", "提示", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }
}

class IPAddressFormatter extends DefaultFormatter {
    @Override
    public String valueToString(Object value) throws ParseException {
        if (!(value instanceof byte[] a)) {
            throw new ParseException("Not a byte[]", 0);
        }
        if (a.length != 4) {
            throw new ParseException("Length != 4", 0);
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int b = a[i];
            if (b < 0) {
                b += 256;
            }
            builder.append(b);
            if (i < 3) {
                builder.append('.');
            }
        }
        return builder.toString();
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        StringTokenizer tokenizer = new StringTokenizer(text, ".");
        byte[] a = new byte[4];
        for (int i = 0; i < 4; i++) {
            int b;
            if (!tokenizer.hasMoreTokens()) {
                throw new ParseException("Too few bytes", 0);
            }
            try {
                b = Integer.parseInt(tokenizer.nextToken());
            } catch (NumberFormatException e) {
                throw new ParseException("Not an integer", 0);
            }
            if (b < 0 || b >= 256) {
                throw new ParseException("Byte out of range", 0);
            }
            a[i] = (byte) b;
        }
        if (tokenizer.hasMoreTokens()) {
            throw new ParseException("Too many bytes", 0);
        }
        return a;
    }
}

//自定义一个DocumentFilter类
class IntFilter extends DocumentFilter {
    //重载insertString方法
    @Override
    public void insertString(FilterBypass fb, int offset, String string,
                             AttributeSet attr) throws BadLocationException {
        StringBuilder builder = changeString(string);
        super.insertString(fb, offset, builder.toString(), attr);
    }

    private StringBuilder changeString(String string) {
        StringBuilder builder = new StringBuilder(string);
        for (int i = builder.length() - 1; i >= 0; i--) {
            int cp = builder.codePointAt(i);
            if (!Character.isDigit(cp) && cp != '-') {
                builder.deleteCharAt(i);
                if (Character.isSupplementaryCodePoint(cp)) {
                    i--;
                    builder.deleteCharAt(i);
                }
            }
        }
        return builder;
    }

    //重载replace方法
    @Override
    public void replace(FilterBypass fb, int offset, int length, String string,
                        AttributeSet attr) throws BadLocationException {
        if (string != null) {
            StringBuilder builder = changeString(string);
            string = builder.toString();
        }
        super.replace(fb, offset, length, string, attr);
    }
}
