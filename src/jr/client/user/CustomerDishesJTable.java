package jr.client.user;

import jr.client.utils.FilterUtil;
import jr.client.utils.TableUtils;
import jr.jdbc.DataUtils;
import jr.model.Dish;
import jr.model.MyTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Vector;

public class CustomerDishesJTable {
    private final JTable table;
    static MyTableModel myTableModel;
    private final Vector<String> colName;
    private final SpinnerEditor spinnerEditor;
    private final SpinnerRenderer spinnerRenderer;
    private final TableRowSorter<MyTableModel> sorter;
    private final Vector<Vector<Object>> data = new Vector<>();
    private Dish[] dishes;
    private RowFilter<MyTableModel, Object> classRowFilter;

    public static void main(String[] args) {
        CustomerDishesJTable customerDishesJTable = new CustomerDishesJTable();
        JFrame jFrame = new JFrame();
        jFrame.setSize(400, 600);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JScrollPane jScrollPane = new JScrollPane(customerDishesJTable.getTable());
        jFrame.add(jScrollPane);
    }

    public CustomerDishesJTable() {
        colName = new Vector<>();
        colName.add("ID");
        colName.add("图片");
        colName.add("菜名");
        colName.add("价格");
        colName.add("分类");
        colName.add("数量");
        myTableModel = new MyTableModel();
        table = new JTable(myTableModel);
        spinnerEditor = new SpinnerEditor();
        spinnerRenderer = new SpinnerRenderer();
        sorter = new TableRowSorter<>(myTableModel);
        table.setRowSorter(sorter);
        table.setRowHeight(100);
        table.setRowSelectionAllowed(false);

//        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//        renderer.setHorizontalAlignment(SwingConstants.CENTER);
//        table.getColumnModel().getColumn(2).setCellRenderer(renderer);

        init();
    }

    public void init() {
        DataUtils dataUtils = new DataUtils();
        dishes = dataUtils.showDishes("");
        TableUtils.dataLoadUtil(dishes, data);
        myTableModel.setDataVector(data, colName);
        table.getColumnModel().getColumn(5).setCellRenderer(spinnerRenderer);
        table.getColumnModel().getColumn(5).setCellEditor(spinnerEditor);
    }

    public void searchFieldFilter(String word) {
        FilterUtil.searchFieldFilterUtil(sorter, classRowFilter, word);
    }

    public void classFilter(String word) {
        classRowFilter=FilterUtil.classFilterUtil(sorter, word);
    }

    public void reset() {
        for (int i = 0; i < table.getRowCount(); i++) table.setValueAt(0, i, 5);
    }

    public JTable getTable() {
        return table;
    }

    public Dish[] getDishes() {
        return dishes;
    }

}

class SpinnerRenderer implements TableCellRenderer {
    private final JSpinner spinner;

    public SpinnerRenderer() {
        spinner = new JSpinner();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }
}

class SpinnerEditor extends DefaultCellEditor {
    private final JSpinner spinner;

    public SpinnerEditor() {
        super(new JTextField());
        setClickCountToStart(1);
        SpinnerEditor.this.fireEditingCanceled();
        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
        spinner = new JSpinner(spinnerModel);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        spinner.setValue(value);
        return spinner;
    }

    @Override
    public Object getCellEditorValue() {
        return spinner.getValue();
    }
}


