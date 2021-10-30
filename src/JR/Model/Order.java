package JR.Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

public class Order implements Serializable {
    private int tableId;
    private String date;
    private double sumPrice;
    private TreeMap<Dish, Integer> dishNumTreeMap;

    public Order(int tableId,double sumPrice, TreeMap<Dish, Integer> dishNumTreeMap) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        this.tableId = tableId;
        this.date = simpleDateFormat.format(date);
        this.dishNumTreeMap = dishNumTreeMap;
        this.sumPrice = sumPrice;
    }

    public int getTableId() {
        return tableId;
    }

    public String getDate() {
        return date;
    }

    public double getSumPrice() {
        return sumPrice;
    }

    public TreeMap<Dish, Integer> getDishNumTreeMap() {
        return dishNumTreeMap;
    }
}
