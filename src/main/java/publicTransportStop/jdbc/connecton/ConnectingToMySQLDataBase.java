package publicTransportStop.jdbc.connecton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectingToMySQLDataBase implements ConnectingToDataBase {

    public Connection connectToDataBase() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_for_public_transport_stops_on_clean_java", "root", "root");
        return con;
    }
}
