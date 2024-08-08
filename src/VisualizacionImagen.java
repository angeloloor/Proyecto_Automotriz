import javax.swing.*;
import java.awt.*;
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
 *Esta es la función para visualizar la imagen
 * @author Angelo Loor
 * @version 2023 B
 */
public class VisualizacionImagen extends JFrame {
    private JTextField idField;
    private JLabel imagenLabel;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton closeButton;
    private File selectedFile;

    public VisualizacionImagen() {
        setTitle("Visualización de Imágenes");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        placeComponents(panel);
        //cargarImagen(); // Cargar imagen inicialmente si es necesario
    }

    private void placeComponents(JPanel panel) {
        JLabel idLabel = new JLabel("Producto ID:");
        idLabel.setBounds(10, 20, 80, 25);
        panel.add(idLabel);

        idField = new JTextField(20);
        idField.setBounds(100, 20, 160, 25);
        panel.add(idField);

        addButton = new JButton("Agregar Imagen");
        addButton.setBounds(10, 50, 200, 25);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    agregarImagen();
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(VisualizacionImagen.this, "Error al agregar la imagen.");
                }
            }
        });

        deleteButton = new JButton("Eliminar Imagen");
        deleteButton.setBounds(10, 80, 200, 25);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    eliminarImagen();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(VisualizacionImagen.this, "Error al eliminar la imagen.");
                }
            }
        });

        updateButton = new JButton("Actualizar Imagen");
        updateButton.setBounds(10, 110, 200, 25);
        panel.add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    actualizarImagen();
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(VisualizacionImagen.this, "Error al actualizar la imagen.");
                }
            }
        });

        closeButton = new JButton("Cerrar");
        closeButton.setBounds(10, 400, 100, 25); // Posiciona el botón en la parte inferior
        panel.add(closeButton);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
            }
        });

        imagenLabel = new JLabel();
        imagenLabel.setBounds(10, 150, 760, 400);
        panel.add(imagenLabel);

        JButton viewButton = new JButton("Ver Imagen");
        viewButton.setBounds(10, 560, 150, 25);
        panel.add(viewButton);

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    verImagen();
                } catch (SQLException | IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(VisualizacionImagen.this, "Error al cargar la imagen.");
                }
            }
        });
    }


    private void verImagen() throws SQLException, IOException {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Producto ID es obligatorio.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT imagen FROM IMAGEN_PRODUCTO WHERE producto_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            byte[] imageBytes = resultSet.getBytes("imagen");
            if (imageBytes != null && imageBytes.length > 0) {
                ImageIcon icon = new ImageIcon(imageBytes);
                Image image = icon.getImage().getScaledInstance(imagenLabel.getWidth(), imagenLabel.getHeight(), Image.SCALE_SMOOTH);
                imagenLabel.setIcon(new ImageIcon(image));
            } else {
                imagenLabel.setIcon(null);
                JOptionPane.showMessageDialog(this, "No hay imagen para este producto.");
            }
        } else {
            imagenLabel.setIcon(null);
            JOptionPane.showMessageDialog(this, "Producto no encontrado.");
        }
    }

    private void agregarImagen() throws SQLException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            if (idField.getText().isEmpty() || selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Producto ID y archivo de imagen son obligatorios.");
                return;
            }

            Connection conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO IMAGEN_PRODUCTO (producto_id, imagen) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(idField.getText()));

            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                preparedStatement.setBinaryStream(2, fis, (int) selectedFile.length());
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Imagen agregada exitosamente!");
            }
        }
    }

    private void eliminarImagen() throws SQLException {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Producto ID es obligatorio.");
            return;
        }

        Connection conn = DatabaseConnection.getConnection();
        String query = "DELETE FROM IMAGEN_PRODUCTO WHERE producto_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(idField.getText()));
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Imagen eliminada exitosamente!");
            imagenLabel.setIcon(null);
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró ninguna imagen para el producto.");
        }
    }

    private void actualizarImagen() throws SQLException, IOException {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            if (idField.getText().isEmpty() || selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Producto ID y archivo de imagen son obligatorios.");
                return;
            }

            Connection conn = DatabaseConnection.getConnection();
            String query = "UPDATE IMAGEN_PRODUCTO SET imagen = ? WHERE producto_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(2, Integer.parseInt(idField.getText()));

            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                preparedStatement.setBinaryStream(1, fis, (int) selectedFile.length());
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Imagen actualizada exitosamente!");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ninguna imagen para actualizar.");
                }
            }
        }
    }

}
