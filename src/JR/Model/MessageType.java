package JR.Model;

public class MessageType {
    public static final int TYPE_LOGIN = 0x1;//客户端上线
    public static final int TYPE_LOGOUT = 0x2;//下线
    public static final int TYPE_ORDER = 0x3;//订单
    public static final int TYPE_LOGIN_SUCCESS = 0x4;//登录成功
    public static final int TYPE_LOGIN_FAIL = 0x5;//登录失败

}
