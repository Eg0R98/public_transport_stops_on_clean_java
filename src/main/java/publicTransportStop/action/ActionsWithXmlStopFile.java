package publicTransportStop.action;

import jakarta.xml.bind.JAXBException;
import publicTransportStop.Request;
import publicTransportStop.exceptions.ConnectException;
import publicTransportStop.stop.ConvertingStopXmlUnmarshallToStop;
import publicTransportStop.stop.Stop;
import publicTransportStop.stop.Stops;
import publicTransportStop.stop.StopsXmlUnmarshallRepository;
import publicTransportStop.timeUpdate.TimeUpdateUsingLocalFile;
import publicTransportStop.transformation.Unmarshalling;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ActionsWithXmlStopFile implements Actions {
    private Path xmlStops = Path.of("C:\\Users\\Егор\\IdeaProjects\\myProject\\src\\main\\java\\publicTransportStop\\stop\\Stops");
    private TimeUpdateUsingLocalFile timeUpdateUsingLocalFile = new TimeUpdateUsingLocalFile();

    public ActionsWithXmlStopFile() throws IOException {
    }

    public void update() throws ConnectException {
        try(InputStream in = Request.requestForUpdateXmlStopsFile()) {
            Files.copy(in, xmlStops, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ConnectException("С соединением проблемы");
        }
    }

    public void loadStops() throws JAXBException, ConnectException {
        try {
            if (timeUpdateUsingLocalFile.updateOrNot() || Files.notExists(xmlStops)) update();
            StopsXmlUnmarshallRepository sxur = Unmarshalling.unmarshallXmlStops(xmlStops);
            List<Stop> stops = ConvertingStopXmlUnmarshallToStop.convert(sxur.getListStopXmlUnmarshall());
            Stops.setListStops(stops);
        } catch (IOException e) {
            throw new ConnectException("С соединением проблемы");
        }

    }
}
