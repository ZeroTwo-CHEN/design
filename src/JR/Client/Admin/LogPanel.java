package JR.Client.Admin;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class LogPanel extends StreamHandler {
    private final JTextArea textArea;

    public LogPanel(JTextArea textArea) {
        this.textArea = textArea;
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        //textArea.setFont();
        setOutputStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                textArea.append(new String(b, off, len));
            }
        });
    }

    public void publish(LogRecord record) {
        if (!textArea.isVisible()) return;
        super.publish(record);
        flush();
    }
}
