package jr.client.utils;

import jr.model.MyTableModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

public class FilterUtil {
    public static void searchFieldFilterUtil(TableRowSorter<MyTableModel> sorter,
                                             RowFilter<MyTableModel, Object> classRowFilter,
                                             String word) {
        RowFilter<MyTableModel, Object> rf = null;
        try {
            rf = RowFilter.regexFilter(word, 0, 2);//只过滤id和菜名两列
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        ArrayList<RowFilter<MyTableModel, Object>> andFilters = new ArrayList<>();
        andFilters.add(rf);
        if (classRowFilter != null)
            andFilters.add(classRowFilter);
        RowFilter<MyTableModel, Object> andFilter = RowFilter.andFilter(andFilters);
        sorter.setRowFilter(andFilter);
    }

    public static RowFilter<MyTableModel, Object> classFilterUtil(TableRowSorter<MyTableModel> sorter,
                                                                  String word) {
        if (word.equals("全部")) {
            sorter.setRowFilter(null);
            return null;
        }
        RowFilter<MyTableModel, Object> classRowFilter = new RowFilter<>() {
            @Override
            public boolean include(Entry<? extends MyTableModel, ?> entry) {
                MyTableModel model = entry.getModel();
                String s = (String) model.getValueAt((int) entry.getIdentifier(), 4);
                return s.equals(word);
            }
        };
        sorter.setRowFilter(classRowFilter);
        return classRowFilter;
    }
}
