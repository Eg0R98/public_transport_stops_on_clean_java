package publicTransportStop.timeUpdate;

import jakarta.xml.bind.JAXBException;
import publicTransportStop.jdbc.connecton.ConnectingToDataBase;
import publicTransportStop.jdbc.connecton.ConnectingToMySQLDataBase;
import publicTransportStop.jdbc.timeUpdateToSql.TimeUpdateToMySql;
import publicTransportStop.jdbc.timeUpdateToSql.TimeUpdateToSql;
import publicTransportStop.transformation.Unmarshalling;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

public class TimeUpdateUsingDataBase implements TimeUpdate {
    private URL urlTimeUpdate = new URL("https://tosamara.ru/api/v2/classifiers");
    private TimeUpdateToSql timeUpdateToSql = new TimeUpdateToMySql();
    private ConnectingToDataBase connecting = new ConnectingToMySQLDataBase();

    public TimeUpdateUsingDataBase() throws MalformedURLException {
    }

    @Override
    public boolean updateOrNot() throws IOException, JAXBException, SQLException, ClassNotFoundException {
        try (Connection connection = connecting.connectToDataBase()) {
          Classifiers classifiers =  Unmarshalling.unmarshallTimeUpdate(urlTimeUpdate);
            Double oldTimeUpdate = timeUpdateToSql.selectTimeUpdateFromTable(connection);
            Double newTimeUpdate = classifiers.getTimeUpdate();
            if ((oldTimeUpdate == null || newTimeUpdate > oldTimeUpdate) && newTimeUpdate != null) {
                oldTimeUpdate = newTimeUpdate;
                timeUpdateToSql.updateTimeUpdateTable(oldTimeUpdate, connection);
                return true;
            }
        }
        return false;
    }
}


