package MyNIOChatRoomFrame;
import javax.swing.*;
import java.awt.event.*;
public class ChatFrame {
    private JFrame frame = new JFrame("聊天窗口");// 窗体界面
    private JTextArea writeContext = new JTextArea(6, 30);// 发送消息文本框
    private JTextArea readContext=new JTextArea(8,30);//信息显示框
    private JButton btnSend = new JButton("发送");// 发送消息按钮
    private JButton btnClose = new JButton("关闭");// 关闭聊天窗口按钮
    private String userName;
    private String msg;
    public ChatFrame(String userName) {
        this.userName = userName;
    }
    // 初始化界面控件及事件
    private void init() {
        frame.setLayout(null);
        frame.setTitle(userName+ "聊天窗口");
        frame.setSize(350, 500);
        frame.setLocation(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);//窗口大小不可改变
        //创建滚动条
        JScrollPane writeScroll = new JScrollPane(writeContext);
        //确定滚动条何时显示,当组件内容垂直区域大于显示区域时出现垂直滚动轴。
        writeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollPane readScroll=new JScrollPane(readContext);
        readScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.add(readScroll);
        frame.add(writeScroll);
        frame.add(btnSend);
        frame.add(btnClose);
        readScroll.setBounds(10,30,320,180);
        writeScroll.setBounds(10, 230, 320, 180);
        readContext.setBounds(0,0,320,180);
        writeContext.setBounds(0, 230,320, 180);
        writeContext.setLineWrap(true);// 自动换行

        btnSend.setBounds(220, 420, 60, 25);
        btnClose.setBounds(50, 420, 60, 25);
        //退出！
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msg = writeContext.getText();//获得文本
                writeContext.setText(null);//置空
                writeContext.requestFocus();//请求此 Component 获得输入焦点
            }
        });
        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    // 显示界面
    public void show() {
        this.init();
        this.frame.setVisible(true);
    }
    public String getMsg() {
        return msg;
    }

    public ChatFrame() {
    }

    public static void main(String[] args) {
        ChatFrame chatFrame=new ChatFrame();
        chatFrame.show();
    }
}