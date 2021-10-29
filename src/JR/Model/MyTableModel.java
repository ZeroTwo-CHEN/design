package JR.Model;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 5;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}
