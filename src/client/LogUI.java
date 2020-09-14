package client;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LogUI {
    public JFrame jFrame;
    public JTextArea textArea;
    public LogUI(){
        jFrame=new JFrame("log");
        jFrame.setSize(600,400);
        textArea=new JTextArea();
        jFrame.add(textArea);
        jFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);// 退出程序
            }

        });
        jFrame.setVisible(true);
    }
    public JTextArea getTextArea() {
        return textArea;
    }
    public void addMsg(String msg){
        textArea.append(msg+"\n");
    }

    public static void main(String[] args) {
        LogUI logUI=new LogUI();
        for (int i=1;i<5;i++){
            logUI.addMsg(String.valueOf(i));
        }
    }
}
