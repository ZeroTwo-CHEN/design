package FUCK.Client;

import FUCK.JDBC.DataUtils;
import FUCK.Model.Dish;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

public class DishesJTable {
    private final JTable table;
    static MyTableModel myTableModel;
    private final DataUtils dataUtils;
    private final Vector<String> colName;
    private ButtonEditor buttonEditor;
    private ButtonRenderer buttonRenderer;
    private TableRowSorter<MyTableModel> sorter;
    private Vector<Vector<Object>> data = new Vector<>();
    static Dish[] dishes;

    public static void main(String[] colName) {
        DishesJTable dishesJTable = new DishesJTable();
        JFrame jFrame = new JFrame();
        jFrame.setSize(400, 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JScrollPane jScrollPane = new JScrollPane(dishesJTable.getTable());
        jFrame.add(jScrollPane);
    }

    public DishesJTable() {
        colName = new Vector<>();
        colName.add("ID");
        colName.add("图片");
        colName.add("菜名");
        colName.add("价格");
        colName.add("分类");
        colName.add("操作");
        dataUtils = new DataUtils();
        myTableModel = new MyTableModel();
        table = new JTable(myTableModel);
        buttonEditor = new ButtonEditor(this);
        buttonRenderer = new ButtonRenderer();
        sorter = new TableRowSorter<>(myTableModel);
        table.setRowSorter(sorter);
        table.setRowHeight(100);
        table.setRowSelectionAllowed(false);
        init();
    }

    public void init() {
        reloadJTable();
    }

    public void reloadJTable() {
        dishes = dataUtils.showDishes("");

        for (Dish dish : dishes) {
            Vector<Object> a = new Vector<>();
            a.add(dish.getId());
            a.add(dish.getImageIcon());
            a.add(dish.getName());
            a.add(dish.getPrice());
            a.add(dish.getClassification());
            a.add("");
            data.add(a);
        }
        myTableModel.setDataVector(data, colName);
        table.getColumnModel().getColumn(5).setCellRenderer(buttonRenderer);
        table.getColumnModel().getColumn(5).setCellEditor(buttonEditor);
        //myTableModel.fireTableStructureChanged();
    }

    public void newFilter(String word) {
        RowFilter<MyTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(word);
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        sorter.setRowFilter(rf);
    }

    public void updateTableData(int index, JPanel panel) {
        DataUtils.updateDish(DishesJTable.dishes[index], (Frame) panel.getRootPane().getParent(), panel);
        myTableModel.setValueAt(dishes[index].getImageIcon(), index, 1);
        myTableModel.setValueAt(dishes[index].getName(), index, 2);
        myTableModel.setValueAt(dishes[index].getPrice(), index, 3);
        myTableModel.setValueAt(dishes[index].getClassification(), index, 4);
        //reloadJTable();
    }

    public JTable getTable() {
        return table;
    }
}

class ButtonRenderer implements TableCellRenderer {
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel panel;

    public ButtonRenderer() {
        updateButton = new JButton("更新");
        deleteButton = new JButton("删除");
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(15);
        panel = new JPanel(layout);
        panel.add(updateButton);
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

class ButtonEditor extends DefaultCellEditor {
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel panel;

    public ButtonEditor(DishesJTable table) {
        super(new JTextField());
        setClickCountToStart(1);
        ButtonEditor.this.fireEditingCanceled();
        updateButton = new JButton("更新");
        deleteButton = new JButton("删除");
        GridLayout layout = new GridLayout(2, 1);
        layout.setVgap(15);

        updateButton.addActionListener(e -> {
            int index = table.getTable().convertRowIndexToModel(table.getTable().getSelectedRow());
            table.updateTableData(index, panel);
        });

        deleteButton.addActionListener(e -> {
            int index = table.getTable().convertRowIndexToModel(table.getTable().getSelectedRow());
            DataUtils.deleteDish(DishesJTable.dishes[index], panel);
            DishesJTable.myTableModel.removeRow(index);
        });

        panel = new JPanel(layout);
        panel.add(updateButton);
        panel.add(deleteButton);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}

class MyTableModel extends DefaultTableModel {

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