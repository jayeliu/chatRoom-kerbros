package client;

import javax.swing.*;

public class LogUI {
    public JFrame jFrame;
    public JTextArea textArea;
    public LogUI(){
        jFrame=new JFrame("log");
        jFrame.setSize(600,400);
        textArea=new JTextArea();
        jFrame.add(textArea);
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
