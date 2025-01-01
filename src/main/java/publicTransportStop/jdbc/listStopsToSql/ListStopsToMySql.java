package publicTransportStop.jdbc.listStopsToSql;

import publicTransportStop.stop.StopXmlUnmarshall;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListStopsToMySql implements ListStopsToSql {

    public ListStopsToMySql() {
    }

    @Override
    public void updateStopsTable(List<StopXmlUnmarshall> listStops, Connection con) throws SQLException {
        String query = "update stops set title = ?, adjacentstreet = ?, direction = ?, busesmunicipal = ?,busescommercial = ?, " +
                "busesprigorod = ?, busesseason = ?, busesspecial = ?,busesintercity = ?, trams = ?, trolleybuses = ?, " +
                "metros = ?,electrictrains = ?, rivertransports = ? where ks_id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (StopXmlUnmarshall sxu : listStops) {
                ps.setString(1, sxu.getTitle());
                ps.setString(2, sxu.getAdjacentStreet());
                ps.setString(3, sxu.getDirection());
                ps.setString(4, sxu.getBusesMunicipal());
                ps.setString(5, sxu.getBusesCommercial());
                ps.setString(6, sxu.getBusesPrigorod());
                ps.setString(7, sxu.getBusesSeason());
                ps.setString(8, sxu.getBusesSpecial());
                ps.setString(9, sxu.getBusesIntercity());
                ps.setString(10, sxu.getTrams());
                ps.setString(11, sxu.getTrolleybuses());
                ps.setString(12, sxu.getMetros());
                ps.setString(13, sxu.getElectricTrains());
                ps.setString(14, sxu.getRiverTransports());
                ps.setInt(15, sxu.getStopID());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public void insertListStopsToTable(List<StopXmlUnmarshall> listStops, Connection con) throws SQLException {
        String query = "insert into stops (ks_id, title, adjacentstreet, direction, busesmunicipal, busescommercial, busesprigorod, busesseason, busesspecial, busesintercity, trams, trolleybuses, metros, electrictrains, rivertransports) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            for (StopXmlUnmarshall sxu : listStops) {
                ps.setInt(1, sxu.getStopID());
                ps.setString(2, sxu.getTitle());
                ps.setString(3, sxu.getAdjacentStreet());
                ps.setString(4, sxu.getDirection());
                ps.setString(5, sxu.getBusesMunicipal());
                ps.setString(6, sxu.getBusesCommercial());
                ps.setString(7, sxu.getBusesPrigorod());
                ps.setString(8, sxu.getBusesSeason());
                ps.setString(9, sxu.getBusesSpecial());
                ps.setString(10, sxu.getBusesIntercity());
                ps.setString(11, sxu.getTrams());
                ps.setString(12, sxu.getTrolleybuses());
                ps.setString(13, sxu.getMetros());
                ps.setString(14, sxu.getElectricTrains());
                ps.setString(15, sxu.getRiverTransports());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    @Override
    public List<StopXmlUnmarshall> selectListStopsFromTable(Connection con) throws SQLException {
        List<StopXmlUnmarshall> listStops = new ArrayList();
        try (Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from stops");
            while (rs.next()) {
                StopXmlUnmarshall sxu = new StopXmlUnmarshall(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13),
                        rs.getString(14), rs.getString(15));
                listStops.add(sxu);
            }
        }
        return listStops;
    }
}

