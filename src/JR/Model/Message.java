package JR.Model;

import java.io.Serializable;

public class Message implements Serializable {
    public int type;
    private int tableId;
    private Order order;

    public Message(int type) {
        this.type = type;
    }

    public Message(int type, int tableId) {
        this.type = type;
        this.tableId = tableId;
    }

    public Message(int type, int tableId, Order order) {
        this.type = type;
        this.tableId = tableId;
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public int getTableId() {
        return tableId;
    }

    public Order getOrder() {
        return order;
    }
}
