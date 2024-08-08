import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *Esta es la funcion para Gestionar los productos
 * @author Angelo Loor
 * @version 2023 B
 */
public class GestionProducto extends JFrame {
    private JTextField idField;
    private JTextField nombreField;
    private JTextField descripcionField;
    private JTextField precioField;
    private JTextField stockField;
    private JTextField categoriaIdField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton uploadButton;
    private JButton viewImageButton;
    private JButton closeButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public GestionProducto() {
        setTitle("Gestión de Productos");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarProducto();
                    cargarProductos();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarProducto();
                    cargarProductos();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarProducto();
                    cargarProductos();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subirImagen();
            }
        });

        viewImageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verImagen();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
            }
        });

        cargarProductos();
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(10, 20, 80, 25);
        panel.add(idLabel);

        idField = new JTextField(20);
        idField.setBounds(100, 20, 160, 25);
        panel.add(idField);

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(10, 50, 80, 25);
        panel.add(nombreLabel);

        nombreField = new JTextField(20);
        nombreField.setBounds(100, 50, 160, 25);
        panel.add(nombreField);

        JLabel descripcionLabel = new JLabel("Descripción:");
        descripcionLabel.setBounds(10, 80, 80, 25);
        panel.add(descripcionLabel);

        descripcionField = new JTextField(20);
        descripcionField.setBounds(100, 80, 160, 25);
        panel.add(descripcionField);

        JLabel precioLabel = new JLabel("Precio:");
        precioLabel.setBounds(10, 110, 80, 25);
        panel.add(precioLabel);

        precioField = new JTextField(20);
        precioField.setBounds(100, 110, 160, 25);
        panel.add(precioField);

        JLabel stockLabel = new JLabel("Stock:");
        stockLabel.setBounds(10, 140, 80, 25);
        panel.add(stockLabel);

        stockField = new JTextField(20);
        stockField.setBounds(100, 140, 160, 25);
        panel.add(stockField);

        JLabel categoriaIdLabel = new JLabel("Categoría ID:");
        categoriaIdLabel.setBounds(10, 170, 80, 25);
        panel.add(categoriaIdLabel);

        categoriaIdField = new JTextField(20);
        categoriaIdField.setBounds(100, 170, 160, 25);
        panel.add(categoriaIdField);

        addButton = new JButton("Agregar");
        addButton.setBounds(10, 210, 100, 25);
        panel.add(addButton);

        updateButton = new JButton("Actualizar");
        updateButton.setBounds(120, 210, 100, 25);
        panel.add(updateButton);

        deleteButton = new JButton("Eliminar");
        deleteButton.setBounds(230, 210, 100, 25);
        panel.add(deleteButton);

        uploadButton = new JButton("Subir Imagen");
        uploadButton.setBounds(340, 210, 130, 25);
        panel.add(uploadButton);

        viewImageButton = new JButton("Ver Imagen");
        viewImageButton.setBounds(340, 240, 130, 25);
        panel.add(viewImageButton);

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(10, 400, 100, 25); // Posiciona el botón en la parte inferior
        panel.add(closeButton);

        String[] columnNames = {"ID", "Nombre", "Descripción", "Precio", "Stock", "Categoría ID"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBounds(10, 250, 360, 150);
        panel.add(scrollPane);
    }

    private void cargarProductos() {
        tableModel.setRowCount(0); // Clear existing data
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
                int categoriaId = resultSet.getInt("categoria_id");

                tableModel.addRow(new Object[]{id, nombre, descripcion, precio, stock, categoriaId});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validarEntradas() {
        if (nombreField.getText().isEmpty() || descripcionField.getText().isEmpty() ||
                precioField.getText().isEmpty() || stockField.getText().isEmpty() ||
                categoriaIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }

        try {
            Double.parseDouble(precioField.getText());
            Integer.parseInt(stockField.getText());
            Integer.parseInt(categoriaIdField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio, Stock y Categoría ID deben ser numéricos.");
            return false;
        }

        return true;
    }

    private void agregarProducto() throws SQLException {
        if (!validarEntradas()) return;
        Connection conn = DatabaseConnection.getConnection();
        String query = "INSERT INTO PRODUCTO (nombre, descripcion, precio, stock, categoria_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, nombreField.getText());
        preparedStatement.setString(2, descripcionField.getText());
        preparedStatement.setDouble(3, Double.parseDouble(precioField.getText()));
        preparedStatement.setInt(4, Integer.parseInt(stockField.getText()));
        preparedStatement.setInt(5, Integer.parseInt(categoriaIdField.getText()));
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Producto agregado exitosamente!");
    }

    private void actualizarProducto() throws SQLException {
        if (!validarEntradas()) return;
        Connection conn = DatabaseConnection.getConnection();
        String query = "UPDATE PRODUCTO SET nombre = ?, descripcion = ?, precio = ?, stock = ?, categoria_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, nombreField.getText());
        preparedStatement.setString(2, descripcionField.getText());
        preparedStatement.setDouble(3, Double.parseDouble(precioField.getText()));
        preparedStatement.setInt(4, Integer.parseInt(stockField.getText()));
        preparedStatement.setInt(5, Integer.parseInt(categoriaIdField.getText()));
        preparedStatement.setInt(6, Integer.parseInt(idField.getText()));
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente!");
    }

    private void eliminarProducto() throws SQLException {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID es obligatorio para eliminar un producto.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "DELETE FROM PRODUCTO WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
        preparedStatement.executeUpdate();
        JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente!");
    }

    private void subirImagen() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                FileInputStream fis = new FileInputStream(file);
                byte[] imageBytes = fis.readAllBytes();

                Connection conn = DatabaseConnection.getConnection();
                String query = "INSERT INTO IMAGEN_PRODUCTO (producto_id, imagen) VALUES (?, ?) ON DUPLICATE KEY UPDATE imagen = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
                preparedStatement.setBytes(2, imageBytes);
                preparedStatement.setBytes(3, imageBytes);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Imagen subida exitosamente!");
            } catch (IOException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al subir la imagen.");
            }
        }
    }

    private void verImagen() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT imagen FROM IMAGEN_PRODUCTO WHERE producto_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                byte[] imageBytes = resultSet.getBytes("imagen");
                ImageIcon icon = new ImageIcon(imageBytes);
                JLabel imageLabel = new JLabel(icon);
                JOptionPane.showMessageDialog(this, imageLabel, "Imagen del Producto", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No hay imagen para este producto.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

}
