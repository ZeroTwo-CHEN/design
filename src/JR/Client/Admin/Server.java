package JR.Client.Admin;

import JR.Model.Message;
import JR.Model.MessageType;
import JR.Model.Order;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private ServerSocket serverSocket;
    private Set<Integer> clients;
    private Queue<Order> orderQueue;
    private static final Logger logger = Logger.getLogger("Server");

    public Server(JTextArea textArea) {
        //日志
        LogPanel windowHandler = new LogPanel(textArea);
        windowHandler.setLevel(Level.ALL);
        logger.addHandler(windowHandler);

        try {
            FileHandler fileHandler = new FileHandler("Server.log", true);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        orderQueue = new LinkedList<>();
        //使用Collections辅助类解决线程不安全
        clients = Collections.synchronizedSet(new HashSet<>());
        ExecutorService executorService = Executors.newFixedThreadPool(12);
        try {
            serverSocket = new ServerSocket(8000);
            logger.info("服务器已启动...");
            while (true) {
                Socket socket = serverSocket.accept();
                UserThread userThread = new UserThread(socket, clients, orderQueue, logger);
                executorService.execute(userThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumOfClients() {
        return clients.size();
    }

    public Queue<Order> getOrderQueue() {
        return orderQueue;
    }
}

class UserThread implements Runnable {
    private int id;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Queue<Order> orderQueue;
    private Logger logger;
    private Set<Integer> clients;

    //TODO: id由客户端发送来
    public UserThread(Socket socket, Set<Integer> clients, Queue<Order> orderQueue, Logger logger) {
        this.socket = socket;
        this.clients = clients;
        this.orderQueue = orderQueue;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            logger.info("收到一个连接");
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                Message message = (Message) objectInputStream.readObject();
                int type = message.getType();
                switch (type) {
                    case MessageType.TYPE_LOGIN -> {
                        if (clients.add(message.getTableId())) {
                            objectOutputStream.writeObject(new Message(MessageType.TYPE_LOGIN_SUCCESS));
                            logger.info("客户端【" + message.getTableId() + "】注册");
                        } else {
                            objectOutputStream.writeObject(new Message(MessageType.TYPE_LOGIN_FAIL));
                        }
                    }
                    case MessageType.TYPE_ORDER -> {
                        logger.info("收到来自客户端【" + message.getTableId() + "】的订单");
                        orderQueue.add(message.getOrder());
                        objectOutputStream.writeObject(new Message(MessageType.TYPE_ORDER,orderQueue.size()-1));
                    }
                    case MessageType.TYPE_LOGOUT -> {
                        logger.info("客户端【" + message.getTableId() + "】下线");
                        clients.remove(message.getTableId());
                        return;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

