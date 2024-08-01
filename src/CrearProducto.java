import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class CrearProducto extends JFrame {
    private JTextField nameField, descriptionField, priceField, stockField, categoriaIdField, imagePathField;
    private productoClase productoClase;

    public CrearProducto() {
        productoClase = new productoClase();
        setTitle("Crear Producto");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Nombre:"));
        nameField = new JTextField();
        panel.add(nameField);
        panel.add(new JLabel("Descripción:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);
        panel.add(new JLabel("Precio:"));
        priceField = new JTextField();
        panel.add(priceField);
        panel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        panel.add(stockField);
        panel.add(new JLabel("Categoría ID:"));
        categoriaIdField = new JTextField();
        panel.add(categoriaIdField);
        panel.add(new JLabel("Ruta de Imagen:"));
        imagePathField = new JTextField();
        panel.add(imagePathField);

        JButton addButton = new JButton("Añadir");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Producto producto = new Producto();
                    producto.setName(nameField.getText());
                    producto.setDescription(descriptionField.getText());
                    producto.setPrice(Double.parseDouble(priceField.getText()));
                    producto.setStock(Integer.parseInt(stockField.getText()));
                    producto.setCategoriaId(Integer.parseInt(categoriaIdField.getText()));
                    producto.setImagePath(imagePathField.getText());
                    productoClase.addProducto(producto);
                    JOptionPane.showMessageDialog(null, "Producto añadido con éxito");
                    clearFields();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void clearFields() {
        nameField.setText("");
        descriptionField.setText("");
        priceField.setText("");
        stockField.setText("");
        categoriaIdField.setText("");
        imagePathField.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CrearProducto().setVisible(true);
            }
        });
    }
}

