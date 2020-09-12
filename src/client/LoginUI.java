package client;/*
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.security.NoSuchAlgorithmException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class RegistUI extends JFrame implements ActionListener{

    private static final long serialVersionUID = 1L;
    private JLabel back;
    private JLabel usr=new JLabel();
    private JLabel pwd=new JLabel();
    private JLabel jl=new JLabel();
    private JTextField jt = new JTextField("输入用户名");		//创建带有初始化文本的文本框对象
    private JPasswordField jp1=new JPasswordField(20);
    private JPasswordField jp2=new JPasswordField(20);
    private JButton x=new JButton();

    public RegistUI(){
        System.out.println(this.getClass().getResource("").getPath());
        this.setResizable(false); 		//不能修改大小
        this.getContentPane().setLayout(null);
        this.setTitle("注册");
        this.setSize(450,350);

        //设置运行位置，是对话框居中
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((int)(screenSize.width-350)/2,
                (int)(screenSize.height-600)/2+45);

        back=new JLabel();
        //ImageIcon icon=new ImageIcon(this.getClass().getResource("../res/regist.png"));
        ImageIcon icon=new ImageIcon("res/regist.png");
        back.setIcon(icon);
        back.setBounds(0, 0, 450, 350);

        usr.setBounds(95, 40, 80, 50);
        usr.setFont(new Font("黑体",Font.PLAIN,14));
        usr.setForeground(Color.BLACK);
        usr.setText("用户名:");
        jt.setForeground(Color.gray);
        jt.setBounds(150, 50, 150, 30);
        jt.setFont(new Font("Serif",Font.PLAIN,12));
        jt.setOpaque(false);

        pwd.setBounds(95, 85, 80, 50);
        pwd.setFont(new Font("黑体",Font.PLAIN,14));
        pwd.setForeground(Color.BLACK);
        pwd.setText("密码： ");
        //创建密码框
        jp1.setFont(new Font("Serif",Font.PLAIN,12));
        jp1.setBounds(150, 95, 150, 30);
        jp1.setVisible(true);
        jp1.setOpaque(false);

        jl.setBounds(85, 130, 80, 60);
        jl.setFont(new Font("黑体",Font.PLAIN,14));
        jl.setForeground(Color.BLACK);
        jl.setText("密码确认： ");
        jp2.setFont(new Font("Serif",Font.PLAIN,12));
        jp2.setBounds(150, 140, 150, 30);
        jp2.setVisible(true);
        jp2.setOpaque(false);


        x.setText("立即注册");
        x.setFont(new Font("Dialog",0,12));
        x.setBounds(180,200, 90, 30);
        x.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        x.setBackground(getBackground());
        x.setBackground(Color.white);
        Border b = new LineBorder(Color.white, 2);
        x.setBorder(b);
        x.setVisible(true);

        x.addActionListener(this);
        this.getContentPane().add(jt);
        this.getContentPane().add(usr);
        this.getContentPane().add(pwd);
        this.getContentPane().add(jl);
        this.getContentPane().add(jp1);
        this.getContentPane().add(jp2);
        this.getContentPane().add(x);

        this.getContentPane().add(back);
        this.setVisible(true);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        String usr=jt.getText().toString();	//获取文本框内容
        String password1 =String.valueOf(jp1.getPassword());	//获取密码框内容
        String password2 =String.valueOf(jp2.getPassword());	//获取密码框内容
        String Content=usr+password1+password2;

        if(usr.equals("")||password1.equals("")||password2.equals("")){
            //System.out.println("请输入完整信息!");
            JOptionPane.showMessageDialog(null, "请输入完整信息!");
            jp1.setText(null);
            jp2.setText(null);
        }
        else if(password1.equals(password2)){
            /*
            Clientbackhandle newregist=new Clientbackhandle();
            try {
                newregist.registsendToAS(usr, password1);//后台做处理
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "注册成功");
            setVisible(false);
        }
        else{
            JOptionPane.showMessageDialog(null, "两次输入的密码不一致，请重新输入！");
            jp1.setText(null);
            jp2.setText(null);
        }
    }

    public static void main(String[] args) {

        // TODO Auto-generated method stub
        new RegistUI();
    }
}

*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;

//判断账号密码是否正确——正确显示“登陆成功”，不正确显示“登录失败”
class LoginButtonListener implements ActionListener {
    public JButton jb1;
    public JButton jb2;
    public JTextField jtf1;
    public JPasswordField jtf2;
    //重写接口中的所有方法：actionPerformed为ActionListener中的方法
    //当该事件发生，这个方法被自动调用
    public void actionPerformed(ActionEvent e){
        //System.out.println("按钮被点击！");
        String account = jtf1.getText();	//获取账号
        char[] passwords = jtf2.getPassword();
        String password =String.valueOf(passwords);	//获取密码框内容
        String Content = account + password;
        //System.out.println(Content); // 日志

        if(account.equals("")||password.equals("")){
            JOptionPane.showMessageDialog(null, "请输入完整信息!");
        }
        else{
            jb1.setText("正在登录...");
            jb2.setVisible(false);
            String[] asretu;
            try {
                asretu = Client.LogintoAS(account,password);
                String idc = asretu[2];
                if(asretu[1]==null)
                {
                    System.out.println("错误");
                    JOptionPane.showMessageDialog(null, asretu[0]);
                    jb2.setVisible(true);
                    jb1.setText("登录");
                }
                else {
                    String[] tgsretu = Client.logintotgs(asretu[2],asretu[0], asretu[1]);//TGS认证结果
                    if(tgsretu[1] == null) {
                        JOptionPane.showMessageDialog(null, tgsretu[0]);
                        jb2.setVisible(true);
                        jb1.setText("登录");
                    } else {
                        String retu = Client.logintov(tgsretu[0],tgsretu[1],tgsretu[2]);
                        if( retu.isEmpty()){ //认证成功开始进入聊天UI
                            jb2.setVisible(true);
                            jb1.setText("已登录");
                            //this.setVisible(false);
                            //new ChatRoomUI(idc,account);
                            new ChatRoomUI();
                        } else {
                            JOptionPane.showMessageDialog(null, retu);//认证失败重新登录
                            jb2.setVisible(true);
                            jb1.setText("登录");
                        }
                    }
                }

            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }/* catch (UnknownHostException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
             */ catch (Exception exception) {
                exception.printStackTrace();
            }
            jb1.setBounds(95, 200, 150, 30);
            //this.setVisible(false);
            /*long usrId = Long.parseLong(jt.getText());
			boolean goon = false;*/
        }
    }

    //将界面中文本框的值传入事件监听器中
    //(a)构造方法，参数为文本框内容
    public LoginButtonListener(JButton jb1,JButton jb2,JTextField jtf1,JPasswordField jtf2){
        this.jb1 = jb1;
        this.jb2 = jb2;
        this.jtf1 = jtf1;
        this.jtf2 = jtf2;
    }
    //(b)set方法
    public void setText(JTextField jtf1,JPasswordField jtf2){
        this.jtf1 = jtf1;
        this.jtf2 = jtf2;
    }

    //判断方法——判断登陆成功or失败，显示弹框信息后，初始化输入框文本为空“”
    public void validate(JTextField jtf1,JTextField jtf2){
        if(jtf1.getText().equals("iris")&&jtf2.getText().equals("123")){
            System.out.println("登陆成功！");
            JOptionPane.showMessageDialog(null, "登录成功！");
        }
        else {
            System.out.println("登录失败！");
            JOptionPane.showMessageDialog(null, "登录失败！");
        }
        jtf1.setText("");
        jtf2.setText("");
    }
}

class RegistButtonListener implements ActionListener {
    public JFrame jf;
    //重写接口中的所有方法：actionPerformed为ActionListener中的方法
    //当该事件发生，这个方法被自动调用
    public void actionPerformed(ActionEvent e){
        //System.out.println("按钮被点击！");
        jf.dispose();
        new RegistUI().showUI();
    }

    //将jf传入事件监听器中
    public RegistButtonListener(JFrame jf){
        this.jf = jf;
    }
}

public class LoginUI{
    public void showUI(String account){
        //创建窗体
        JFrame jf = new JFrame();
        jf.setSize(380,420);               //窗体大小
        jf.setDefaultCloseOperation(3);    //可以退出
        jf.setLocationRelativeTo(null);    //相对屏幕居中
        jf.setTitle("Kerberos");              //窗体名字

        //流式布局
        FlowLayout flow = new FlowLayout();
        jf.setLayout(flow);

        //图像——创建JLabel对象，使用ImageIcon作为输入初始化JLabel
        ImageIcon icon = new ImageIcon("res/Kerberos.jpg");
        icon.setImage(icon.getImage().getScaledInstance(350,270,Image.SCALE_DEFAULT));//这里设置图片大小，目前是20*20
        JLabel jla = new JLabel(icon);
        jf.add(jla);

        //文本输入——文字JLabel、账号JTextField、密码JPasswordField
        //除了JFrame设置大小为setSize(int x, int y),其他组件都用setPreferredSize(Dimension d)
        JLabel jl1 = new JLabel("账号：");
        JTextField jt1 = new JTextField();
        jt1.setPreferredSize(new Dimension(300,30));
        JLabel jl2 = new JLabel("密码：");
        JPasswordField jt2 = new JPasswordField();
        jt2.setPreferredSize(new Dimension(300,30));
        jf.add(jl1);
        jf.add(jt1);
        jf.add(jl2);
        jf.add(jt2);

        //填充刚刚注册的账号
        jt1.setText(account);

        //复选框——JCheckBox
        JCheckBox jcb = new JCheckBox("记住密码");
        jf.add(jcb);

        //按钮——JButton
        JButton jb1= new JButton("登录");
        jf.add(jb1);
        //按钮——JButton
        JButton jb2 = new JButton("注册");
        jf.add(jb2);

        //登陆按钮绑定事件
        LoginButtonListener lbl = new LoginButtonListener(jb1,jb2,jt1,jt2);
        jb1.addActionListener(lbl);
        //注册按钮绑定事件j
        RegistButtonListener rbl = new RegistButtonListener(jf);
        jb2.addActionListener(rbl);

        //窗体可见，写在add组件之后
        jf.setVisible(true);
    }

    public static void main(String args[]){
        LoginUI tf = new LoginUI();
        tf.showUI("");
    }
    //以下写方法
}