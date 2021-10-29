package JR.Model;

import java.io.Serializable;
import java.util.TreeMap;

public class Order implements Serializable {
    private double sumPrice;
    private TreeMap<Dish, Integer> dishNumTreeMap;

    public Order(double sumPrice, TreeMap<Dish, Integer> dishNumTreeMap) {
        this.dishNumTreeMap = dishNumTreeMap;
        this.sumPrice = sumPrice;
    }
}
