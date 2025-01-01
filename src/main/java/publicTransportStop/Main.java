package publicTransportStop;

import jakarta.xml.bind.JAXBException;
import publicTransportStop.action.Actions;
import publicTransportStop.action.ActionsWithDataBase;
import publicTransportStop.cache.Cache;
import publicTransportStop.exceptions.ConnectException;
import publicTransportStop.exceptions.NotMatchException;
import publicTransportStop.stop.Stop;
import publicTransportStop.stop.Stops;
import publicTransportStop.timeUpdate.Classifiers;
import publicTransportStop.timeUpdate.File;
import publicTransportStop.transformation.Unmarshalling;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Actions actions = new ActionsWithDataBase();
            actions.loadStops();
            while (true) {
                String stopTitle = Input.inputTitle(scanner);
                List<Stop> matches = Stops.getMatches(stopTitle);
                Input.printMatches(matches);
                int numberOfMatches = Input.chooseNumberOfMatches(scanner);
                int stopNumber = Stops.getStopID(matches.get(numberOfMatches - 1));
                String responseFromCache = Cache.searchAndGet(stopNumber);
                if (responseFromCache != null) {
                    Input.printResponse(responseFromCache);
                } else {
                    String responseFromServer = Request.httpRequest(stopNumber);
                    Input.printResponse(responseFromServer);
                    Cache.add(stopNumber, responseFromServer);
                }
                boolean stop = Input.isStopProgram(scanner);
                if (stop) break;
            }
        } catch (NotMatchException | ConnectException | JAXBException | SQLException | ClassNotFoundException |
                 IOException e) {
            Input.printExceptionMessage(e);
        }
    }
}

