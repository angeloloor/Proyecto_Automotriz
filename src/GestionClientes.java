import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *Esta es la funcion de la Gestion de clientes
 * @author Angelo Loor
 * @version 2023 B
 */
public class GestionClientes extends JFrame {
    private JTextField idField, nombreField, direccionField, telefonoField, emailField;
    private JTextArea resultArea;

    public GestionClientes() {
        setTitle("Gestión de Clientes");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        placeComponents(panel);
    }

    private void placeComponents(JPanel panel) {
        // Cliente ID
        JLabel idLabel = new JLabel("Cliente ID:");
        idLabel.setBounds(10, 20, 80, 25);
        panel.add(idLabel);

        idField = new JTextField(20);
        idField.setBounds(100, 20, 160, 25);
        panel.add(idField);

        // Nombre
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(10, 50, 80, 25);
        panel.add(nombreLabel);

        nombreField = new JTextField(20);
        nombreField.setBounds(100, 50, 160, 25);
        panel.add(nombreField);

        // Dirección
        JLabel direccionLabel = new JLabel("Dirección:");
        direccionLabel.setBounds(10, 80, 80, 25);
        panel.add(direccionLabel);

        direccionField = new JTextField(20);
        direccionField.setBounds(100, 80, 160, 25);
        panel.add(direccionField);

        // Teléfono
        JLabel telefonoLabel = new JLabel("Teléfono:");
        telefonoLabel.setBounds(10, 110, 80, 25);
        panel.add(telefonoLabel);

        telefonoField = new JTextField(20);
        telefonoField.setBounds(100, 110, 160, 25);
        panel.add(telefonoField);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 140, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField(20);
        emailField.setBounds(100, 140, 160, 25);
        panel.add(emailField);

        // Botones
        JButton addButton = new JButton("Agregar");
        addButton.setBounds(10, 180, 150, 25);
        panel.add(addButton);

        JButton updateButton = new JButton("Actualizar");
        updateButton.setBounds(170, 180, 150, 25);
        panel.add(updateButton);

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setBounds(330, 180, 150, 25);
        panel.add(deleteButton);

        JButton searchButton = new JButton("Buscar");
        searchButton.setBounds(10, 220, 150, 25);
        panel.add(searchButton);

        JButton closeButton = new JButton("Cerrar");
        closeButton.setBounds(10, 400, 100, 25);
        panel.add(closeButton);

        // Resultados
        resultArea = new JTextArea();
        resultArea.setBounds(10, 260, 470, 200);
        resultArea.setEditable(false);
        panel.add(resultArea);

        // Acción de los botones
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarCliente();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(GestionClientes.this, "Error al agregar el cliente.");
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarCliente();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(GestionClientes.this, "Error al actualizar el cliente.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarCliente();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(GestionClientes.this, "Error al eliminar el cliente.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    buscarCliente();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(GestionClientes.this, "Error al buscar el cliente.");
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
            }
        });
    }

    private void agregarCliente() throws SQLException {
        Connection conn = DatabaseConnection.getConnection();
        String query = "INSERT INTO CLIENTE (nombre, direccion, telefono, email) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, nombreField.getText());
        preparedStatement.setString(2, direccionField.getText());
        preparedStatement.setString(3, telefonoField.getText());
        preparedStatement.setString(4, emailField.getText());
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente!");
        clearFields();
    }

    private void actualizarCliente() throws SQLException {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cliente ID es obligatorio.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "UPDATE CLIENTE SET nombre = ?, direccion = ?, telefono = ?, email = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, nombreField.getText());
        preparedStatement.setString(2, direccionField.getText());
        preparedStatement.setString(3, telefonoField.getText());
        preparedStatement.setString(4, emailField.getText());
        preparedStatement.setInt(5, Integer.parseInt(idField.getText()));
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente!");
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
        }
        clearFields();
    }

    private void eliminarCliente() throws SQLException {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cliente ID es obligatorio.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "DELETE FROM CLIENTE WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente!");
        } else {
            JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
        }
        clearFields();
    }

    private void buscarCliente() throws SQLException {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cliente ID es obligatorio.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM CLIENTE WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            String result = "ID: " + resultSet.getInt("id") + "\n" +
                    "Nombre: " + resultSet.getString("nombre") + "\n" +
                    "Dirección: " + resultSet.getString("direccion") + "\n" +
                    "Teléfono: " + resultSet.getString("telefono") + "\n" +
                    "Email: " + resultSet.getString("email");
            resultArea.setText(result);
        } else {
            resultArea.setText("Cliente no encontrado.");
        }
    }

    private void clearFields() {
        idField.setText("");
        nombreField.setText("");
        direccionField.setText("");
        telefonoField.setText("");
        emailField.setText("");
    }
}
