package  HaiShengClasses;
import Socket.ClientSocket;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

public class LoginListener implements ActionListener{
    private javax.swing.JTextField textname;
    private javax.swing.JPasswordField textpass;
    private javax.swing.JFrame login;
    private javax.swing.JButton button;

    public LoginListener(javax.swing.JTextField textName,javax.swing.JPasswordField textPass,javax.swing.JFrame login,javax.swing.JButton button1){
        this.button = button1;
        this.textname = textName;
        this.textpass = textPass;
        this.login = login;
    }

    public void actionPerformed(ActionEvent e){
        try
        {
            ClientSocket socket=new ClientSocket();
            socket.send();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }

    }
}
