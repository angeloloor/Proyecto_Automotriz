import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class productoClase {

    public void addProducto(Producto producto) throws SQLException {
        String query = "insert into PRODUCTO (name, description, price, stock, categoria_id, image_path) values (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, producto.getName());
            statement.setString(2, producto.getDescription());
            statement.setDouble(3, producto.getPrice());
            statement.setInt(4, producto.getStock());
            statement.setInt(5, producto.getCategoriaId());
            statement.setString(6, producto.getImagePath());
            statement.executeUpdate();
        }
    }

    public Producto getProducto(int id) throws SQLException {
        Producto producto = null;
        String query = "select * from PRODUCTO where id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                producto = new Producto();
                producto.setId(resultSet.getInt("id"));
                producto.setName(resultSet.getString("name"));
                producto.setDescription(resultSet.getString("description"));
                producto.setPrice(resultSet.getDouble("price"));
                producto.setStock(resultSet.getInt("stock"));
                producto.setCategoriaId(resultSet.getInt("categoria_id"));
                producto.setImagePath(resultSet.getString("image_path"));
            }
        }
        return producto;
    }

    public List<Producto> getAllProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String query = "select * from PRODUCTO";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Producto producto = new Producto();
                producto.setId(resultSet.getInt("id"));
                producto.setName(resultSet.getString("name"));
                producto.setDescription(resultSet.getString("description"));
                producto.setPrice(resultSet.getDouble("price"));
                producto.setStock(resultSet.getInt("stock"));
                producto.setCategoriaId(resultSet.getInt("categoria_id"));
                producto.setImagePath(resultSet.getString("image_path"));
                productos.add(producto);
            }
        }
        return productos;
    }

    public void updateProducto(Producto producto) throws SQLException {
        String query = "update PRODUCTO set name = ?, description = ?, price = ?, stock = ?, categoria_id = ?, image_path = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, producto.getName());
            statement.setString(2, producto.getDescription());
            statement.setDouble(3, producto.getPrice());
            statement.setInt(4, producto.getStock());
            statement.setInt(5, producto.getCategoriaId());
            statement.setString(6, producto.getImagePath());
            statement.setInt(7, producto.getId());
            statement.executeUpdate();
        }
    }

    public void deleteProducto(int id) throws SQLException {
        String query = "delete from PRODUCTO where id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
