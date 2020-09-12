package tgs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class chatRoomUI extends JFrame implements ActionListener {
    private final JTextArea displayArea = new JTextArea();
    private final JTextArea inputArea = new JTextArea();
    private final JButton sendMsgButton = new JButton();
    private final JButton exitButton = new JButton();
    private final JLabel chatMsgLabel = new JLabel();
    private final JLabel inputMsgLabel = new JLabel();
    private final JScrollPane jsp1 = new JScrollPane();
    private final JScrollPane jsp2 = new JScrollPane();

    public chatRoomUI() {
        init();
    }

    public void init() {
        //全局基础设置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);//固定大小
        this.getContentPane().setLayout(null);
        this.setTitle("CHAT");
        this.setBounds(100, 100, 500, 500);

        //

        //聊天框显示
        chatMsgLabel.setBounds(0, 0, 80, 30);
        chatMsgLabel.setFont(new Font("黑体", Font.PLAIN, 16));
        chatMsgLabel.setForeground(Color.BLACK);
        chatMsgLabel.setText("聊天消息");
        displayArea.setForeground(Color.BLACK);
        displayArea.setFont(new Font("Serif", Font.PLAIN, 12));
        displayArea.setLineWrap(true);
        displayArea.setOpaque(false);
        displayArea.setEditable(false);
        jsp1.setBounds(30, 30, 410, 200);
        jsp1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp1.setOpaque(false);
        jsp1.getViewport().setOpaque(false);
        //输入框显示
        inputMsgLabel.setBounds(0, 260, 80, 30);
        inputMsgLabel.setFont(new Font("黑体", Font.PLAIN, 16));
        inputMsgLabel.setForeground(Color.BLACK);
        inputMsgLabel.setText("输入");
        inputArea.setForeground(Color.BLACK);
        inputArea.setFont(new Font("Serif", Font.PLAIN, 12));
        inputArea.setLineWrap(true);
        inputArea.setOpaque(false);
        jsp2.setBounds(30, 300, 410, 100);
        jsp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jsp2.setOpaque(false);
        jsp2.getViewport().setOpaque(false);


        sendMsgButton.setText("发送消息");
        sendMsgButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        sendMsgButton.setBounds(10, 410, 100, 28);
        sendMsgButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendMsgButton.setBackground(getBackground());
        sendMsgButton.setBackground(Color.white);
        Border b = new LineBorder(Color.white, 2);
        sendMsgButton.setBorder(b);
        sendMsgButton.setVisible(true);


        exitButton.setText("退出");
        exitButton.setFont(new Font("Dialog", Font.PLAIN, 12));
        exitButton.setBounds(130, 410, 100, 28);
        exitButton.setBackground(Color.WHITE);
        exitButton.setVisible(true);
        exitButton.setBorder(b);
        exitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        this.setBackground(Color.BLACK);
        this.getContentPane().add(jsp1);
        this.getContentPane().add(jsp2);
        this.getContentPane().add(sendMsgButton);
        this.getContentPane().add(exitButton);
        this.getContentPane().add(chatMsgLabel);
        this.getContentPane().add(inputMsgLabel);

        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static void main(String[] args) throws Exception {
        //org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        new chatRoomUI();
    }
}
