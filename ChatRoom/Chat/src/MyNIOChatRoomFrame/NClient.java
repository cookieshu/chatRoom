package MyNIOChatRoomFrame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NClient {
    private String userName = "";
    private String password = "";
    static int flag = 0;
    //定义检测socketChannel的selector对象
    private Selector selector = null;
    static final int PORT = 9999;
    //定义处理编码和解码的字符集
    Charset charset = Charset.forName("UTF-8");
    //定义客户端SocketChannel
    private SocketChannel sc = null;
    public void init() throws IOException {
        selector = Selector.open();
        InetSocketAddress isa = new InetSocketAddress("127.0.0.1", PORT);
        //调用open静态方法创建连接到指定主机的SocketChannel
        sc = SocketChannel.open(isa);
        //设置sc以非阻塞模式
        sc.configureBlocking(false);
        //注册到Selector
        sc.register(selector, SelectionKey.OP_READ);
        //启动读取服务器端数据的线程
        new ClientThread().start();
        if (flag == 2) {
            //把用户的个人信息发送给服务器！
            String user = "register->" + userName + ";" + password;
            sc.write(charset.encode(user));
        } else if (flag == 1) {
            //把用户的个人信息发送给服务器！
            String user = "land->" + userName + ";" + password;
            sc.write(charset.encode(user));
        }
        //创建键盘输入流
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            //读取键盘输入
            String line = scan.nextLine();
            //将键盘输入的内容输出到SocketChannel   不用buffer?因为charset.encode(msg)返回的是ByteBuffer
            String msg = userName + ":" + line;
            sc.write(charset.encode(msg));
        }
    }
    //定义读取服务器端数据的线程,  私聊！
    private class ClientThread extends Thread {
        public void run() {
            try {
                while (selector.select() > 0) {
                    //遍历每个有可用IO操作Channel对应的SelectionKey
                    for (SelectionKey sk : selector.selectedKeys()) {
                        //删除正在处理的SelectionKey
                        selector.selectedKeys().remove(sk);
                        //如果该selectionKey对应的Channel中有可读数据
                        if (sk.isReadable()) {
                            //读取Channel中的数据
                            SocketChannel sc = (SocketChannel) sk.channel();
                            ByteBuffer buff = ByteBuffer.allocate(1024);
                            String content = "";
                            while (sc.read(buff) > 0) {
                                buff.flip();
                                content += charset.decode(buff);
                            }
                            if (content.startsWith("error:")) {
                                System.out.println(content);
                                System.exit(0);
                            } else if (content.startsWith("land:")) {
                                System.out.println(content);
                            } else if (content.startsWith("注册成功")) {
                                System.out.println(content);
                                System.out.println("私聊格式：p-userName:私聊信息");
                                System.out.println("群聊格式：无格式，直接写信息");
                                System.out.println("退出格式：exit");
                            } else if (content.startsWith("退出")) {
                                System.out.println(content);
                                System.exit(0);
                            } else {
                                String uname = content.split(":")[0];
                                String msg = content.split(":")[1];
                                System.out.println(uname + "私聊你说：" + msg);
                            }
                            //为下一次读取做准备
                            sk.interestOps(SelectionKey.OP_READ);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void registerOrEnter(String userName, String password) {

    }

    public NClient(String userName, String password,int comdflag) {
        this.userName = userName;
        this.password = password;
        flag=comdflag;
    }
    public NClient() {
    }
    /*
    //发送在窗口写的数据信息
    public void frameSendmsg(String msg) throws IOException {
        if (msg.length()>0)
        {
            msg=userName+":"+msg;
            sc.write(charset.encode(msg));
        }
    }*/
    /*public static void main(String[] args) throws IOException {
        NClient client = new NClient();
        //先获取用户名和密码
        SetNameFrame setNameFrame = new SetNameFrame();
        setNameFrame.setVisible(true);
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入用户名：");
        client.userName += scanner.nextLine();
        System.out.print("请输入密码：");
        client.password += scanner.nextLine();
        //判断选择登陆还是注册
        System.out.println("请选择：1,登陆；2,注册");
        flag = scanner.nextInt();
        client.init();
    }*/
}
