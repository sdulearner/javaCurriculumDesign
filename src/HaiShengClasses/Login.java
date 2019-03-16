package HaiShengClasses;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class Login
{

    private String loginId;
    private char[] loginPassWord;

    public static void main(String[] args)
    {
        Login login = new Login();
        login.initUI();
    }

    //初始化界面的方法
    public void initUI()
    {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame.add(panel);

        //实例化布局类的对象
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbs = new GridBagConstraints();
        panel.setLayout(layout);

        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        //实例化JLabel对象，该对象显示账号
        JLabel labName = new JLabel("学号:");
        labName.setFont(new Font("宋体", Font.PLAIN, 14));
        gbs.gridx = 0;
        gbs.gridy = 0;
        layout.setConstraints(labName, gbs);
        panel.add(labName);

        //实例化JTextField标签对象化
        JTextField textName = new JTextField(15);
        //将textName添加到窗体上
        gbs.gridx = 1;
        gbs.gridy = 0;
        gbs.gridwidth = 5;
        gbs.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(textName, gbs);
        panel.add(textName);

        //实例化JLabel标签对象
        JLabel labPass = new JLabel("密码:");
        labPass.setFont(new Font("宋体", Font.PLAIN, 14));
        //将labpass添加到窗体上
        gbs.gridx = 0;
        gbs.gridy = 1;
        layout.setConstraints(labPass, gbs);
        panel.add(labPass);

        //实例化JPasswordField
        JPasswordField textPass = new JPasswordField(15);
        //添加到窗体
        gbs.gridx = 1;
        gbs.gridy = 1;
        gbs.gridwidth = 5;
        gbs.insets = new Insets(10, 0, 0, 0);
        gbs.fill = GridBagConstraints.HORIZONTAL;
        layout.setConstraints(textPass, gbs);
        panel.add(textPass);

        JCheckBox RemenberPass = new JCheckBox("记住密码");
        gbs.gridx = 0;
        gbs.gridy = 2;
        gbs.gridwidth = 3;
        layout.setConstraints(RemenberPass, gbs);
        panel.add(RemenberPass);

        JCheckBox AutoLogin = new JCheckBox("自动登陆");
        gbs.gridx = 2;
        gbs.gridy = 2;
        gbs.gridwidth = 3;
        layout.setConstraints(AutoLogin, gbs);
        panel.add(AutoLogin);

        //实例化JButton组件
        JButton button1 = new JButton("注册");
        button1.setFont(new Font("宋体", Font.PLAIN, 14));
        gbs.gridx = 0;
        gbs.gridy = 3;
        gbs.gridwidth = 2;
        gbs.insets = new Insets(0, 0, 0, 10);
        layout.setConstraints(button1, gbs);
        panel.add(button1);

        JButton button2 = new JButton("登陆");
        button2.setFont(new Font("宋体", Font.PLAIN, 14));
        gbs.gridx = 1;
        gbs.gridy = 3;
        gbs.gridwidth = 2;
        gbs.insets = new Insets(0, 0, 0, 10);
        layout.setConstraints(button2, gbs);
        panel.add(button2);

        JButton button3 = new JButton("找回密码");
        button3.setFont(new Font("宋体", Font.PLAIN, 14));
        gbs.gridx = 2;
        gbs.gridy = 3;
        gbs.gridwidth = 2;
        layout.setConstraints(button3, gbs);
        panel.add(button3);

        frame.setTitle("登陆-班级管理系统");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.pack();
        frame.setFont(new Font("宋体", Font.PLAIN, 14));
        frame.setVisible(true);

        button2.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JOptionPane.showMessageDialog(null, "登陆");
                textName.setEnabled(false);
                textPass.setEnabled(false);
                button1.setEnabled(false);
                button3.setEnabled(false);
                button2.setEnabled(false);
                RemenberPass.setEnabled(false);
                AutoLogin.setEnabled(false);
                frame.setEnabled(false);
                button2.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                loginId = textName.getText();
                loginPassWord = textPass.getPassword();
            }
        });

        button1.addActionListener(new registerListener());


    }

//    public void restore()
//    {
//        textName.setEnabled(true);
//        textPass.setEnabled(true);
//        button1.setEnabled(true);
//        button3.setEnabled(true);
//        button2.setEnabled(true);
//        RemenberPass.setEnabled(true);
//        AutoLogin.setEnabled(true);
//        frame.setEnabled(true);
//        button2.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
//    }
}
