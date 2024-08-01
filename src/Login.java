import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame {


    private JPanel panel1;
    private JButton loginButton;
    private JPasswordField contratxt;
    private JTextField usuariotxt;


    public Login() {

        setTitle("Login del sistema");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setPreferredSize(new Dimension(300,300));
        setMinimumSize(new Dimension(300,300));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String url="jdbc:mysql://localhost:3306/ AutoPartsXpress";
                String user="root";
                String password="123456";

                try (Connection connection= DriverManager.getConnection(url,user,password)){

                    String query="select * from USUARIO where username= '"+ usuariotxt.getText()+"' and password = '"+contratxt.getText()+"'";
                    Statement statement=connection.createStatement();
                    ResultSet resultSet=statement.executeQuery(query);

                    if (resultSet.next()){

                        if(resultSet.getString("username").equals(usuariotxt.getText()) && resultSet.getString("password").equals(contratxt.getText())){
                            JOptionPane.showMessageDialog(null, "ACCESO CONCEDIDO");
                            new paginaPrincipal();
                            setVisible(false);
                        }


                    }else{
                        JOptionPane.showMessageDialog(null, "ACCESO DENEGADO");

                    }

                } catch (SQLException e1) {
                    System.out.println(e1);
                }

            }
        });


    }
}
