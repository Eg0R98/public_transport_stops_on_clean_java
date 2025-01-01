package publicTransportStop.jdbc.creation;

import org.glassfish.jaxb.core.v2.model.core.ID;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreatorOfMySqlTables implements CreatorOfTables {

    @Override
    public void createTableForTimeUpdate(Connection con) throws SQLException {
        String tableTime = "create table time_update (id int primary key auto_increment not null, time double)";
        try (Statement stmt = con.createStatement();) {
            stmt.execute(tableTime);
        }
    }

    @Override
    public void createTableForListStops(Connection con) throws SQLException {
        String tableStops = "create table stops (ks_id int not null primary key, title varchar(100), adjacentStreet  varchar(100), " +
                "direction  varchar(100), busesMunicipal  varchar(100), busesCommercial  varchar(100), busesPrigorod  varchar(100), " +
                "busesSeason  varchar(100), busesSpecial  varchar(100), busesIntercity  varchar(100), trams  varchar(100), trolleybuses  varchar(100), " +
                "metros  varchar(100), electricTrains  varchar(300), riverTransports  varchar(100))";
        try (Statement stmt = con.createStatement();) {
            stmt.execute(tableStops);
        }
    }


    @Override
    public boolean isExist(String tableName, Connection con) throws SQLException {
        String query = String.format("SELECT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'db_for_public_transport_stops_on_clean_java' AND TABLE_NAME = '%s') " +
                "AS table_exists", tableName);
        try (Statement statement = con.createStatement()) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt(1) == 1) return true;
            }
        }
        return false;
    }
}
