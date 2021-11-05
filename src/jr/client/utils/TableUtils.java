package jr.client.utils;

import jr.model.Dish;

import java.util.Vector;

public class TableUtils {
    public static void dataLoadUtil(Dish[] dishes, Vector<Vector<Object>> data) {
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
    }
}
