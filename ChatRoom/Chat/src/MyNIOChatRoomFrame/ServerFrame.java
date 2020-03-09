package MyNIOChatRoomFrame;

import javax.swing.*;

public class ServerFrame extends JFrame {
    private static JTextArea groupMsg=new JTextArea(36,30);

    public void init() {
        this.setTitle("群聊窗口");
        this.setSize(500,500);
        this.setLocation(400,200);
        this.setResizable(false);//窗口大小不可改变
        JScrollPane msg=new JScrollPane(groupMsg);
        //设置滚动条什么时候出现
        msg.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        this.add(msg);
        this.setDefaultCloseOperation(3);//设置关闭
        this.setVisible(true);//可视化
    }
    public void showMsg(String msg){
        msg=groupMsg.getText()+"\r\n"+msg;
        groupMsg.setText(msg);
        groupMsg.setEditable(false);//不可编辑
    }
   /* public static void main(String[] args) {
        ServerFrame serverFrame = new ServerFrame();
        *//*  设置关闭按钮
        0”或DO_NOTHING_ON_CLOSE：
                    （在 WindowConstants 中定义）：不执行任何操作；要求程序在已注册的
            1”或HIDE_ON_CLOSE
                调用任意已注册的 WindowListener 对象后自动隐藏该窗体。
                此时没有关闭程序，只是将程序界面隐藏了
             2”或DISPOSE_ON_CLOSE
                    调用任意已注册 WindowListener 的对象后自动隐藏并释放该窗体。
                    但继续运行应用程序，释放了窗体中占用的资源
              3”EXIT_ON_CLOSE（在 JFrame 中定义）：
                      使用 System exit 方法退出应用程序。
                      仅在应用程序中使用。结束了应用程序。
            *//*

        serverFrame.init();
    }*/
}
