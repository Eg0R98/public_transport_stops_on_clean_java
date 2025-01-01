package publicTransportStop.action;

import jakarta.xml.bind.JAXBException;
import publicTransportStop.exceptions.ConnectException;
import publicTransportStop.jdbc.connecton.ConnectingToDataBase;
import publicTransportStop.jdbc.connecton.ConnectingToMySQLDataBase;
import publicTransportStop.jdbc.creation.CreatorOfMySqlTables;
import publicTransportStop.jdbc.creation.CreatorOfTables;
import publicTransportStop.jdbc.listStopsToSql.ListStopsToMySql;
import publicTransportStop.jdbc.listStopsToSql.ListStopsToSql;
import publicTransportStop.jdbc.timeUpdateToSql.TimeUpdateToMySql;
import publicTransportStop.jdbc.timeUpdateToSql.TimeUpdateToSql;
import publicTransportStop.stop.ConvertingStopXmlUnmarshallToStop;
import publicTransportStop.stop.Stop;
import publicTransportStop.stop.StopXmlUnmarshall;
import publicTransportStop.stop.Stops;
import publicTransportStop.timeUpdate.Classifiers;
import publicTransportStop.timeUpdate.TimeUpdateUsingDataBase;
import publicTransportStop.transformation.Unmarshalling;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ActionsWithDataBase implements Actions {
    private TimeUpdateUsingDataBase timeUpdateUsingDataBase = new TimeUpdateUsingDataBase();
    private URL urlStops = new URL("https://tosamara.ru/api/v2/classifiers/stopsFullDB.xml");
    private ListStopsToSql listStopsToSql = new ListStopsToMySql();
    private TimeUpdateToSql timeUpdateToSql = new TimeUpdateToMySql();
    private List<StopXmlUnmarshall> listStopsFromURL = Unmarshalling.unmarshallXmlStops(urlStops).getListStopXmlUnmarshall();
    private ConnectingToDataBase connecting = new ConnectingToMySQLDataBase();

    public ActionsWithDataBase() throws MalformedURLException, SQLException, ClassNotFoundException, JAXBException {
    }

    public void update() throws ClassNotFoundException, ConnectException {
        try (Connection connection = connecting.connectToDataBase()) {
            listStopsToSql.updateStopsTable(listStopsFromURL, connection);
        } catch (SQLException e) {
            throw new ConnectException("Проблемы с базой данных");
        }
    }

    public void loadStops() throws JAXBException, ClassNotFoundException, ConnectException {
        try (Connection connection = connecting.connectToDataBase()) {
            createTables();
            if (timeUpdateUsingDataBase.updateOrNot()) update();
            List<StopXmlUnmarshall> listStopsFromDataBase = listStopsToSql.selectListStopsFromTable(connection);
            List<Stop> stops = ConvertingStopXmlUnmarshallToStop.convert(listStopsFromDataBase);
            Stops.setListStops(stops);
        } catch (SQLException | IOException e) {
            throw new ConnectException("С соединением проблемы");
        }

    }

    private void createTables() throws SQLException, ClassNotFoundException, MalformedURLException, JAXBException {
        CreatorOfTables creator = new CreatorOfMySqlTables();
        try (Connection connection = connecting.connectToDataBase()) {
            if (!creator.isExist("stops", connection)) {
                creator.createTableForListStops(connection);
                listStopsToSql.insertListStopsToTable(listStopsFromURL, connection);
            }
            if (!creator.isExist("time_update", connection)) {
                creator.createTableForTimeUpdate(connection);
                Classifiers classifiers = Unmarshalling.unmarshallTimeUpdate(new URL("https://tosamara.ru/api/v2/classifiers"));
                Double timeUpdate = classifiers.getTimeUpdate();
                timeUpdateToSql.insertTimeUpdateToTable(timeUpdate, connection);
            }
        }

    }
}
