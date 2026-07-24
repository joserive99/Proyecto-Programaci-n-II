
package Database.com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private Connection conn;

    public Database() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/torneos?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8",
                "root",
                "Medium"
            );

        } catch (ClassNotFoundException | SQLException ex) {

            ex.printStackTrace();

        }

    }

    public Connection getConnection() {

        return conn;

    }

    public void Close() {

        try {

            if(conn!=null){

                conn.close();

            }

        } catch (SQLException ex) {

            ex.printStackTrace();

        }

    }

}
