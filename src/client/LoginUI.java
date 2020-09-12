package client;

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
                        ReturnV retu = Client.logintov(tgsretu[0],tgsretu[1],tgsretu[2]);
                        if( retu.error == null){ //认证成功开始进入聊天UI
                            jb2.setVisible(true);
                            jb1.setText("已登录");
                            //this.setVisible(false);
                            //new ChatRoomUI(idc,account);
                            new ChatRoomUI(account,retu.getSock());
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