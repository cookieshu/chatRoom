package MyNIOChatRoomFrame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NServer_Open {
    //定义界面
    static ServerFrame serverFrame;
    //存储用户的信息
    User user = null;
    Map<User, SocketChannel> userMap = new HashMap<>();
    //定义选择器
    private Selector selector = null;
    static final int PORT = 9999;
    //定义实现编码，解码的字符集对象
    private Charset charset = Charset.forName("UTF-8");

    public void init() throws IOException {
        selector = Selector.open();
        //通过open方法打开一个未绑定的ServerSocketChannel实例
        ServerSocketChannel server = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", PORT);
        //将ServerSocketChannel绑定到该IP地址
        server.bind(inetSocketAddress);
        //设置ServerSocket非阻塞工作
        server.configureBlocking(false);
        ///将server注册到selector对象
        server.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select() > 0) {
            //依次处理已选择的selectionKey
            for (SelectionKey sk : selector.selectedKeys()) {
                //从selector上已选择Key集合中删除正在处理的SelectionKey
                //否则会累加
                selector.selectedKeys().remove(sk);
                //如果sk对应的channel包含客户端的连接请求
                if (sk.isAcceptable()) {
                    //调用accept接受连接，产生服务器端的SocketChannel
                    SocketChannel sc = server.accept();
                    //设置非阻塞模式
                    sc.configureBlocking(false);
                    //将该socketChanel注册到selector
                    sc.register(selector, SelectionKey.OP_READ);//首先是读
                    //sk对应的Channel设置成准备接受其它请求
                    sk.interestOps(SelectionKey.OP_ACCEPT);
                }
                //如果sk对应的Channel有数据需要读取
                if (sk.isReadable()) {
                    //获取该SelectionKey对应的Channel，该Channel中有可读数据
                    SocketChannel sc = (SocketChannel) sk.channel();
                    //定义准备读取的ByteBuffer
                    //申请的是堆内存？还是直接内存？
                    ByteBuffer buff = ByteBuffer.allocate(1024);
                    String content = "";
                    //开始读取数据
                    try {
                        while (sc.read(buff) > 0) {
                            buff.flip();
                            content += charset.decode(buff);//解码
                        }
                        //注册用户
                        if (content.startsWith("register->")) {
                            String name = content.split("->")[1].split(";")[0];
                            String ps = content.split("->")[1].split(";")[1];
                            //注册用户
                            user = new User();
                            user.setUserName(name);
                            user.setPassword(ps);
                            userMap.put(user, sc);
//                            channel.write(ByteBuffer.wrap(attachment.toString().getBytes()));
                            sc.write(charset.encode("注册成功^_^"));
                            System.out.println(name+"用户已上线...");
                        } else if (content.startsWith("land->")) {
                            int flag = 0;
                            String name = content.split("->")[1].split(";")[0];
                            String ps = content.split("->")[1].split(";")[1];
                            Set<Map.Entry<User, SocketChannel>> users = userMap.entrySet();
                            for (Map.Entry<User, SocketChannel> user : users) {
                                if (name.equals(user.getKey().getUserName()) && ps.equals(user.getKey().getPassword())) {
                                    userMap.remove(user.getKey());//取消上次的用户数据
                                    userMap.put(user.getKey(),sc);//更新用户的socketChannel
                                    flag = 1;
                                }
                            }
                            if (flag == 1) {
                                sc.write(charset.encode("land:登陆成功"));
                                System.out.println(name+"用户已上线...");
                            } else {
                                String error = "error:用户名或密码错误！";
                                sc.write(charset.encode(error));
                            }
                        } else if (content.contains("exit")) {
                            sc.write(charset.encode("退出,欢迎下次登陆"));
                        } else if (content.split(":")[1].startsWith("p")) {
                            String name = content.split("-")[1].split(":")[0];
                            String msg = content.split("-")[1].split(":")[1];
                            privateChat(sc, name, msg);
                        } else {
                            //群聊：直接打印读取到的数据
                            String userName = content.split(":")[0];
                            String msg = content.split(":")[1];
                            serverFrame.showMsg(userName + "说：" + msg);
                            System.out.println(userName + "说：" + msg);
                        }
                        //将sk对应的Channel设置成准备下一次读取
                        sk.interestOps(SelectionKey.OP_READ);
                    }
                    //如果捕获到sk对应的channel出现了异常，
                    // 即表明该Channel对应的Client出现了问题，所以取消Selector中sk的注册
                    catch (IOException e) {
                        //从selector中删除指定的selectionKey
                        sk.cancel();//不会立刻删除，下一次select()
                        if (sk.channel() != null) {
                            sk.channel().close();
                        }
                    }
                }
            }
        }
    }
    private void privateChat(SocketChannel mysocketChannel, String name, String msg) throws IOException {
        Set<Map.Entry<User, SocketChannel>> set = userMap.entrySet();
        SocketChannel socketChannel = null;//对方的客户端
        //遍历查找
        for (Map.Entry<User, SocketChannel> user : set) {
            if (name.equals(user.getKey().getUserName())) {
                socketChannel = user.getValue();
            }
        }
        if (socketChannel == null) {
            mysocketChannel.write(charset.encode("@_@,对不起，查无此人!"));
        } else {
//            ByteBuffer buffer=ByteBuffer.allocate(1024);
            msg = name + ":" + msg;
            socketChannel.write(charset.encode(msg));
        }

    }
    public static void main(String[] args) throws IOException {
        //开启服务器界面
        serverFrame=new ServerFrame();
        serverFrame.init();
        serverFrame.showMsg("欢迎来到聊天室^_^");
        //开启服务器
        new NServer_Open().init();
    }
}
