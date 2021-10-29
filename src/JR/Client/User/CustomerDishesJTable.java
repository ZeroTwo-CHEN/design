package JR.Client.User;

import JR.JDBC.DataUtils;
import JR.Model.Dish;
import JR.Model.MyTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

public class CustomerDishesJTable {
    private final JTable table;
    static MyTableModel myTableModel;
    private final Vector<String> colName;
    private SpinnerEditor spinnerEditor;
    private SpinnerRenderer spinnerRenderer;
    private TableRowSorter<MyTableModel> sorter;
    private Vector<Vector<Object>> data = new Vector<>();
    private Dish[] dishes;

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
        init();
    }

    public void init() {
        DataUtils dataUtils = new DataUtils();
        dishes = dataUtils.showDishes("");
        for (Dish dish : dishes) {
            Vector<Object> a = new Vector<>();
            a.add(dish.getId());
            a.add(dish.getImageIcon());
            a.add(dish.getName());
            a.add(dish.getPrice());
            a.add(dish.getClassification());
            a.add(0);
            data.add(a);
        }
        myTableModel.setDataVector(data, colName);
        table.getColumnModel().getColumn(5).setCellRenderer(spinnerRenderer);
        table.getColumnModel().getColumn(5).setCellEditor(spinnerEditor);

//        table.getModel().addTableModelListener(new TableModelListener() {
//            @Override
//            public void tableChanged(TableModelEvent e) {
//                if (e.getType() == TableModelEvent.UPDATE) {
//                    System.out.println(e.getLastRow());
//                }
//            }
//        });

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

    public JTable getTable() {
        return table;
    }

    public Dish[] getDishes() {
        return dishes;
    }

}

class SpinnerRenderer implements TableCellRenderer {
    private JSpinner spinner;

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
    private JSpinner spinner;

    public SpinnerEditor() {
        super(new JTextField());
        setClickCountToStart(1);
        SpinnerEditor.this.fireEditingCanceled();
        SpinnerModel spinnerModel = new SpinnerNumberModel(0, 0, null, 1);
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


