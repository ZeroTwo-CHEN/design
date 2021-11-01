package jr.client.user;

import jr.model.Message;
import jr.model.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private int id;

    public Client() {

    }

    public int sendOrder(Message message) {
        try {
            objectOutputStream.writeObject(message);
            Message num = (Message) objectInputStream.readObject();
            return num.getTableId();//返回前方订单数
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean read(int id, String ip, int port) {
        this.id = id;
        try {
            socket = new Socket(ip, 8000);

            //必须先获得输出流，才能再获得输入流
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //发送桌号查询
            Message login = new Message(MessageType.TYPE_LOGIN, id);
            objectOutputStream.writeObject(login);

            //接收结果
            Message response = (Message) objectInputStream.readObject();

            //判断结果
            if (response.getType() == MessageType.TYPE_LOGIN_SUCCESS) {
                return true;
            }
            if (response.getType() == MessageType.TYPE_LOGIN_FAIL) {
                return false;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("服务器连接失败");
            return false;
        }
        return false;
    }

    public void logout() {
        Message message = new Message(MessageType.TYPE_LOGOUT, id);
        try {
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
