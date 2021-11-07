package jr.client.admin;

import javax.swing.*;
import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class LogPanel extends StreamHandler {
    private final JTextArea textArea;

    public LogPanel(JTextArea textArea) {
        this.textArea = textArea;
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        setOutputStream(new OutputStream() {
            @Override
            public void write(int b) {
            }

            @Override
            public void write(byte[] b, int off, int len) {
                textArea.insert(new String(b, off, len),0);
            }
        });
    }

    @Override
    public void publish(LogRecord record) {
        if (!textArea.isVisible()) {
            return;
        }
        super.publish(record);
        flush();
    }
}
