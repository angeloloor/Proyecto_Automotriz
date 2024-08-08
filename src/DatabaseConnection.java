import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *Esta es la funcion de coneccion a la base de datos
 * @author Angelo Loor
 * @version 2023 B
 */
public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/AutoPartsXpress";
    private static final String user = "root";
    private static final String password = "123456";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

