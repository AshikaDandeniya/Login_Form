import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setVisible(true);
    }
}

class RegistrationForm extends JFrame {
    Connection connection;
    private final Container c;

    RegistrationForm() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3308/jdbc", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setTitle("Login Form");
        setBounds(300, 90, 400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        c = getContentPane();
        c.setLayout(null);

        JLabel title = new JLabel("Login Form", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(100, 50, 200, 25);
        title.setForeground(Color.BLUE);
        c.add(title);

        JLabel name = new JLabel("Name");
        name.setFont(new Font("Arial", Font.BOLD, 20));
        name.setBounds(30, 100, 100, 25);
        c.add(name);

        JTextField tname = new JTextField();
        tname.setFont(new Font("Arial", Font.PLAIN, 15));
        tname.setBounds(100, 100, 200, 25);
        c.add(tname);

        JLabel email = new JLabel("Email");
        email.setFont(new Font("Arial", Font.BOLD, 20));
        email.setBounds(30, 160, 100, 25);
        c.add(email);

        JTextField temail = new JTextField();
        temail.setFont(new Font("Arial", Font.PLAIN, 15));
        temail.setBounds(100, 160, 200, 25);
        c.add(temail);

        JLabel gender = new JLabel("Gender");
        gender.setFont(new Font("Arial", Font.BOLD, 20));
        gender.setBounds(30, 220, 100, 25);
        c.add(gender);

        JRadioButton male = new JRadioButton("Male");
        male.setFont(new Font("Arial", Font.PLAIN, 15));
        male.setBounds(120, 220, 80, 25);
        male.setSelected(true);
        c.add(male);

        JRadioButton female = new JRadioButton("Female");
        female.setFont(new Font("Arial", Font.PLAIN, 15));
        female.setBounds(200, 220, 85, 25);
        c.add(female);

        ButtonGroup gengrp = new ButtonGroup();
        gengrp.add(male);
        gengrp.add(female);

        JLabel password = new JLabel("Password");
        password.setFont(new Font("Arial", Font.BOLD, 20));
        password.setBounds(30, 280, 100, 25);
        c.add(password);

        JPasswordField tpassword = new JPasswordField();
        tpassword.setFont(new Font("Arial", Font.PLAIN, 15));
        tpassword.setBounds(150, 280, 200, 25);
        c.add(tpassword);

        JCheckBox robot = new JCheckBox("I am not a robot");
        robot.setFont(new Font("Arial", Font.PLAIN, 15));
        robot.setBounds(30, 330, 200, 25);
        c.add(robot);

        JButton signup = new JButton("Sign-Up");
        signup.setFont(new Font("Arial", Font.BOLD, 20));
        signup.setBounds(100, 390, 200, 25);
        signup.setBackground(Color.BLUE);
        signup.setForeground(Color.WHITE);
        c.add(signup);

        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tname.getText().trim();
                String email = temail.getText().trim();
                String password = new String(tpassword.getPassword());

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!isValidEmail(email)) {
                    JOptionPane.showMessageDialog(null, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!robot.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please confirm you are not a robot!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    PreparedStatement pstm = connection.prepareStatement("INSERT INTO users(name,email,gender,password) VALUES(?,?,?,?)");

                    pstm.setString(1, name);
                    pstm.setString(2, email);
                    pstm.setString(3, male.isSelected() ? "Male" : "Female");
                    pstm.setString(4, password);
                    pstm.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Sign-Up Successful", "Success", JOptionPane.INFORMATION_MESSAGE);

                    // Clear input fields after success
                    tname.setText("");
                    temail.setText("");
                    tpassword.setText("");
                    male.setSelected(true);
                    robot.setSelected(false);

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error saving data!", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
