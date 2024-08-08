import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *Esta es la interfaz para poder logearse
 * @author Angelo Loor
 * @version 2023 B
 * @see <a href="https://youtu.be/R8sysR88s1g">Aqui puedes encontra el video de youtube</a>
 */
public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public Login() {
        setTitle("Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (authenticateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
                        messageLabel.setText("Login successful!");
                        new MainFrame().setVisible(true);
                    } else {
                        messageLabel.setText("Invalid username or password.");
                    }
                } catch (SQLException ex) {
                    messageLabel.setText("Database error.");
                    ex.printStackTrace();
                }
            }
        });
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 160, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 160, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        messageLabel = new JLabel("");
        messageLabel.setBounds(100, 80, 200, 25);
        panel.add(messageLabel);
    }

    private boolean authenticateUser(String username, String password) throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM USUARIO WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

}

