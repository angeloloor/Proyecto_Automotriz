import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
/**
 *Esta es la funcion es para Gestionar las transacciones
 * @author Angelo Loor
 * @version 2023 B
 */
public class GestionTransaccion extends JFrame {
    private JTextField clienteIdField;
    private JTextField totalField;
    private JTable transaccionTable;
    private DefaultTableModel transaccionTableModel;
    private JTable detalleTransaccionTable;
    private DefaultTableModel detalleTransaccionTableModel;

    public GestionTransaccion() {
        setTitle("Gestión de Transacciones");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        cargarTransacciones();
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        // Campos de entrada
        JLabel clienteIdLabel = new JLabel("Cliente ID:");
        clienteIdLabel.setBounds(10, 20, 80, 25);
        panel.add(clienteIdLabel);

        clienteIdField = new JTextField(20);
        clienteIdField.setBounds(100, 20, 160, 25);
        panel.add(clienteIdField);

        JLabel totalLabel = new JLabel("Total:");
        totalLabel.setBounds(10, 50, 80, 25);
        panel.add(totalLabel);

        totalField = new JTextField(20);
        totalField.setBounds(100, 50, 160, 25);
        panel.add(totalField);

        // Botones
        JButton addButton = new JButton("Registrar Transacción");
        addButton.setBounds(10, 80, 200, 25);
        panel.add(addButton);

        JButton closeButton = new JButton("Cerrar");
        closeButton.setBounds(10, 400, 100, 25); // Posiciona el botón en la parte inferior
        panel.add(closeButton);

        // Acción del botón para registrar una transacción
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    registrarTransaccion();
                    cargarTransacciones(); // Actualiza la tabla de transacciones
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Acción del botón para cerrar la ventana
        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
            }
        });

        // Tabla de Transacciones
        String[] transaccionColumnNames = {"ID", "Fecha", "Cliente ID", "Total"};
        transaccionTableModel = new DefaultTableModel(transaccionColumnNames, 0);
        transaccionTable = new JTable(transaccionTableModel);
        JScrollPane transaccionScrollPane = new JScrollPane(transaccionTable);
        transaccionScrollPane.setBounds(10, 120, 760, 200);
        panel.add(transaccionScrollPane);

        // Tabla de Detalle de Transacciones
        String[] detalleColumnNames = {"ID", "Transacción ID", "Producto ID", "Cantidad", "Precio"};
        detalleTransaccionTableModel = new DefaultTableModel(detalleColumnNames, 0);
        detalleTransaccionTable = new JTable(detalleTransaccionTableModel);
        JScrollPane detalleScrollPane = new JScrollPane(detalleTransaccionTable);
        detalleScrollPane.setBounds(10, 340, 760, 200);
        panel.add(detalleScrollPane);

        // Acción cuando se selecciona una fila en la tabla de transacciones
        transaccionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && transaccionTable.getSelectedRow() != -1) {
                int selectedRow = transaccionTable.getSelectedRow();
                int transaccionId = (int) transaccionTableModel.getValueAt(selectedRow, 0);
                cargarDetalleTransaccion(transaccionId); // Carga los detalles de la transacción seleccionada
            }
        });
    }

    // Método para registrar una nueva transacción
    private void registrarTransaccion() throws SQLException {
        if (clienteIdField.getText().isEmpty() || totalField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cliente ID y Total son obligatorios.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "INSERT INTO TRANSACCION (fecha, cliente_id, total) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setDate(1, new java.sql.Date(new Date().getTime())); // Fecha actual
        preparedStatement.setInt(2, Integer.parseInt(clienteIdField.getText()));
        preparedStatement.setDouble(3, Double.parseDouble(totalField.getText()));
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Transacción registrada exitosamente!");
    }

    // Método para cargar todas las transacciones en la tabla
    private void cargarTransacciones() {
        transaccionTableModel.setRowCount(0); // Limpiar la tabla
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM TRANSACCION";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date fecha = resultSet.getDate("fecha");
                int clienteId = resultSet.getInt("cliente_id");
                double total = resultSet.getDouble("total");

                transaccionTableModel.addRow(new Object[]{id, fecha, clienteId, total});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Método para cargar los detalles de una transacción específica en la tabla
    private void cargarDetalleTransaccion(int transaccionId) {
        detalleTransaccionTableModel.setRowCount(0); // Limpiar la tabla
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM DETALLE_TRANSACCION WHERE transaccion_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, transaccionId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int productoId = resultSet.getInt("producto_id");
                int cantidad = resultSet.getInt("cantidad");
                double precio = resultSet.getDouble("precio");

                detalleTransaccionTableModel.addRow(new Object[]{id, transaccionId, productoId, cantidad, precio});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
