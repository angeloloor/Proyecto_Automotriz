import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *Esta es la función para poder actualizar el stock
 * @author Angelo Loor
 * @version 2023 B
 */
public class GestionStock extends JFrame {
    private JTextField idField;
    private JTextField stockField;
    private JTable stockTable;
    private DefaultTableModel stockTableModel;

    public GestionStock() {
        setTitle("Gestión de Stock");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        cargarStock();
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel idLabel = new JLabel("Producto ID:");
        idLabel.setBounds(10, 20, 80, 25);
        panel.add(idLabel);

        idField = new JTextField(20);
        idField.setBounds(100, 20, 160, 25);
        panel.add(idField);

        JLabel stockLabel = new JLabel("Nuevo Stock:");
        stockLabel.setBounds(10, 50, 80, 25);
        panel.add(stockLabel);

        stockField = new JTextField(20);
        stockField.setBounds(100, 50, 160, 25);
        panel.add(stockField);

        JButton updateButton = new JButton("Actualizar Stock");
        updateButton.setBounds(10, 80, 200, 25);
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarStock();
                    cargarStock();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton closeButton = new JButton("Cerrar");
        closeButton.setBounds(10, 400, 100, 25); // Posiciona el botón en la parte inferior
        panel.add(closeButton);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
            }
        });

        // Tabla de Stock
        String[] stockColumnNames = {"ID", "Nombre", "Descripción", "Precio", "Stock"};
        stockTableModel = new DefaultTableModel(stockColumnNames, 0);
        stockTable = new JTable(stockTableModel);
        JScrollPane stockScrollPane = new JScrollPane(stockTable);
        stockScrollPane.setBounds(10, 120, 760, 400);
        panel.add(stockScrollPane);
    }

    private void actualizarStock() throws SQLException {
        if (idField.getText().isEmpty() || stockField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Producto ID y Nuevo Stock son obligatorios.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "UPDATE PRODUCTO SET stock = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(stockField.getText()));
        preparedStatement.setInt(2, Integer.parseInt(idField.getText()));
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Stock actualizado exitosamente!");
    }

    private void cargarStock() {
        stockTableModel.setRowCount(0);
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM PRODUCTO";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                double precio = resultSet.getDouble("precio");
                int stock = resultSet.getInt("stock");

                stockTableModel.addRow(new Object[]{id, nombre, descripcion, precio, stock});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}

