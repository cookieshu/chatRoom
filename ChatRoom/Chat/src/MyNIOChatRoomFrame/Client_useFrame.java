package MyNIOChatRoomFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

class SetNameFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static JLabel name;
    private static JLabel password;
    private static JTextField txtName;
    private static JTextField txtPassword;
    private static JButton btnOK;
    private static JButton btnLogin;
    private static JLabel label;
    private static JLabel warning;
    private String userName;
    private String passWord;
    public SetNameFrame() {
        this.setLayout(null);
        Toolkit kit = Toolkit.getDefaultToolkit();
        int w = kit.getScreenSize().width;
        int h = kit.getScreenSize().height;
        this.setBounds(w / 2 - 230 / 2, h / 2 - 200 / 2, 230, 200);
        this.setTitle("登录/注册");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);//固定大小
        name=new JLabel("用户名:");
        this.add(name);
        name.setBounds(10,10,60,25);
        password=new JLabel("密码:");
        this.add(password);
        password.setBounds(10,40,60,25);
        txtName = new JTextField(10);
        this.add(txtName);
        txtName.setBounds(80, 10, 100, 25);
        txtPassword=new JTextField(12);
        this.add(txtPassword);
        txtPassword.setBounds(80,40,100,25);
        btnOK = new JButton("注册");
        this.add(btnOK);
        btnOK.setBounds(120, 80, 60, 25);
        btnLogin=new JButton("登录");
        this.add(btnLogin);
        btnLogin.setBounds(50,80,60,25);
        label = new JLabel("欢迎使用");
        this.add(label);
        label.setBounds(80, 120, 60, 25);
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName = txtName.getText();
                passWord=txtPassword.getText();
                setVisible(false);//不可视化
                NClient client=new NClient(userName,passWord,2);
                try {
                    client.init();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userName = txtName.getText();
                 passWord=txtPassword.getText();
                setVisible(false);//不可视化
                //把用户数据传给客户端
                NClient client=new NClient(userName,passWord,1);
                try {
                    client.init();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public static void main(String[] args) {
        SetNameFrame setNameFrame = new SetNameFrame();
        setNameFrame.setVisible(true);
    }

}