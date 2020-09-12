package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

//判断账号密码是否正确——正确显示“登陆成功”，不正确显示“登录失败”
class ConfirmButtonListener implements ActionListener {
    public JFrame jf;
    public JTextField jtf1; // 账号
    public JPasswordField jtf2; // 密码
    public JPasswordField jtf3; // 确认密码
    //重写接口中的所有方法：actionPerformed为ActionListener中的方法
    //当该事件发生，这个方法被自动调用
    public void actionPerformed(ActionEvent e){
        System.out.println("按钮被点击！");
        //判断方法——判断登陆成功or失败，显示弹框信息后，初始化输入框文本为空“”
        String usr = jtf1.getText().toString();	//获取文本框内容
        String password1 = String.valueOf(jtf2.getPassword());	//获取密码框内容
        String password2 = String.valueOf(jtf3.getPassword());	//获取密码框内容
        String Content= usr + password1 + password2;
        System.out.println(Content);

        if(usr.equals("")||password1.equals("")||password2.equals("")){
            //System.out.println("请输入完整信息!");
            JOptionPane.showMessageDialog(null, "请输入完整信息!");
            jtf1.setText(null);
            jtf2.setText(null);
        }
        else if(password1.equals(password2)){
            Client newregist=new Client();
            boolean status = false;
            try {
                status = newregist.registsendToAS(usr, password1);//后台做处理
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if(status == true){
                JOptionPane.showMessageDialog(null, "注册成功");
                // 销毁该页面
                jf.dispose();
                new LoginUI().showUI(jtf1.getText());
            }else {
                JOptionPane.showMessageDialog(null, "注册失败");
                jtf2.setText(null);
                jtf3.setText(null);
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "两次输入的密码不一致，请重新输入！");
            jtf2.setText(null);
            jtf3.setText(null);
        }
    }

    //将界面中文本框的值传入事件监听器中
    //(a)构造方法，参数为文本框内容
    public ConfirmButtonListener(JFrame jf,JTextField jtf1,JPasswordField jtf2,JPasswordField jtf3){
        this.jf = jf;
        this.jtf1 = jtf1;
        this.jtf2 = jtf2;
        this.jtf3 = jtf3;
    }
    //(b)set方法
    public void setText(JTextField jtf1,JPasswordField jtf2,JPasswordField jtf3){
        this.jtf1 = jtf1;
        this.jtf2 = jtf2;
        this.jtf3 = jtf3;
    }
}


public class RegistUI {
    public void showUI() {
        //创建窗体
        JFrame jf = new JFrame();
        jf.setSize(380, 440);               //窗体大小
        jf.setDefaultCloseOperation(3);    //可以退出
        jf.setLocationRelativeTo(null);    //相对屏幕居中
        jf.setTitle("Kerberos");              //窗体名字

        //流式布局3
        FlowLayout flow = new FlowLayout();
        jf.setLayout(flow);

        //图像——创建JLabel对象，使用ImageIcon作为输入初始化JLabel
        ImageIcon icon = new ImageIcon("res/Kerberos.jpg");
        icon.setImage(icon.getImage().getScaledInstance(350, 270, Image.SCALE_DEFAULT));//这里设置图片大小，目前是20*20
        JLabel jla = new JLabel(icon);
        jf.add(jla);

        //文本输入——文字JLabel、账号JTextField、密码JPasswordField
        //除了JFrame设置大小为setSize(int x, int y),其他组件都用setPreferredSize(Dimension d)
        JLabel jl1 = new JLabel("账号：");
        JTextField jt1 = new JTextField();
        jt1.setPreferredSize(new Dimension(300, 30));
        JLabel jl2 = new JLabel("密码：");
        JPasswordField jt2 = new JPasswordField();
        jt2.setPreferredSize(new Dimension(300, 30));
        JLabel jl3 = new JLabel("确认：");
        JPasswordField jt3 = new JPasswordField();
        jt3.setPreferredSize(new Dimension(300, 30));
        jf.add(jl1);
        jf.add(jt1);
        jf.add(jl2);
        jf.add(jt2);
        jf.add(jl3);
        jf.add(jt3);
        //JTextField jt3 = new JTextField(4);//设置输入框大小另一种方式——4个输入字符

        //按钮——JButton
        JButton jb = new JButton("注册");
        jf.add(jb);

        //窗体可见，写在add组件之后
        jf.setVisible(true);

        //(a)构造方法初始化文本框的对象
        ConfirmButtonListener bl = new ConfirmButtonListener(jf, jt1, jt2, jt3);

        //(b)set方法设置监听器类中的文本框类的对象
//      ButtonListener bl = new ButtonListener();
//      bl.setText(jt1,jt2);

        //4.绑定
        jb.addActionListener(bl);
    }
}