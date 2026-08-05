package Database.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Connection conn;

    private static final String URL ="jdbc:mysql://localhost:3306/torneos" + "?useSSL=false" + "&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "Admin$1234";

    public Database() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

        } catch (ClassNotFoundException e) {throw new RuntimeException("No se encontró el controlador de MySQL.", e);
        }
    }

    public synchronized Connection getConnection() {

        try {

            if (conn == null || conn.isClosed()) {

                conn = DriverManager.getConnection(URL,USER,PASSWORD);

                System.out.println("Conexión a la base de datos establecida." );
            }

            return conn;

        } catch (SQLException e) {throw new RuntimeException( "No fue posible conectarse a la base de datos.",e);
        }
    }

    public synchronized void Close() {

        try {

            if (conn != null && !conn.isClosed()) {

                conn.close();

                System.out.println("Conexión a la base de datos cerrada.");
            }

        } catch (SQLException e) {e.printStackTrace();

        } finally {

            conn = null;
        }
    }
}
