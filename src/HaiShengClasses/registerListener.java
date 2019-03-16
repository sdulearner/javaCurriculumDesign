package HaiShengClasses;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class registerListener implements ActionListener{
    public void registListener(){

    }
    public void actionPerformed(ActionEvent e){
        JFrame register = new JFrame("注册");
        JPanel panel = new JPanel();
        register.setResizable(true);
        register.setFont(new Font("宋体",Font.PLAIN,14));
        register.setDefaultCloseOperation(register.EXIT_ON_CLOSE);
        register.setLocationRelativeTo(null);
        register.pack();
        panel.setBorder(new EmptyBorder(16,16,16,16));
        register.add(panel);

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbs = new GridBagConstraints();
        panel.setLayout(layout);

        //实例化JLabel对象
        JLabel name = new JLabel("学号:");
        name.setFont(new Font("宋体",Font.PLAIN,14));
        gbs.gridx = 0;
        gbs.gridy = 0;
        layout.setConstraints(name, gbs);
        panel.add(name);

        JTextField regiName = new JTextField(15);
        gbs.gridx = 1;
        gbs.gridy = 0;
        layout.setConstraints(regiName,gbs);
        panel.add(regiName);

        JLabel pass = new JLabel("密码:");
        pass.setFont(new Font("宋体",Font.PLAIN,14));
        gbs.gridx = 0;
        gbs.gridy = 1;
        layout.setConstraints(pass,gbs);
        panel.add(pass);

        JPasswordField regiPass = new JPasswordField(15);
        gbs.gridx = 1;
        gbs.gridy = 1;
        layout.setConstraints(regiPass,gbs);
        panel.add(regiPass);

        JLabel gender = new JLabel("性别：");
        gender.setFont(new Font("宋体",Font.PLAIN,14));
        gbs.gridx = 0;
        gbs.gridy = 3;
        layout.setConstraints(gender,gbs);
        panel.add(gender);

        String[] option1 = new String[]{"-请选择-","男","女"};
        final JComboBox<String> comboBox1 = new JComboBox<String>(option1);
        comboBox1.setSelectedIndex(0);
        gbs.gridx = 1;
        gbs.gridy = 3;
        layout.setConstraints(comboBox1,gbs);
        panel.add(comboBox1);

        JLabel manager = new JLabel("是否成为管理员:");
        manager.setFont(new Font("宋体",Font.PLAIN,14));
        gbs.gridx = 0;
        gbs.gridy = 4;
        layout.setConstraints(manager,gbs);
        panel.add(manager);

        String[] options2 = new String[]{"-请选择-","是","否"};
        final JComboBox<String> comboBox2 = new JComboBox<String>(options2);
        comboBox2.setSelectedIndex(0);
        gbs.gridx = 1;
        gbs.gridy = 4;
        layout.setConstraints(comboBox2,gbs);
        panel.add(comboBox2);

        JButton JB = new JButton("确定");
        gbs.gridx = 0;
        gbs.gridy = 5;
        gbs.weightx = 1;
        layout.setConstraints(JB,gbs);
        panel.add(JB);

        register.setVisible(true);
        JB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register.dispose();
            }
        });

    }


}
