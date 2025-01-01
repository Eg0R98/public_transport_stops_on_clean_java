package publicTransportStop.timeUpdate;

import jakarta.xml.bind.JAXBException;
import publicTransportStop.transformation.Unmarshalling;

import java.io.IOException;
import java.net.URL;

public class TimeUpdateUsingLocalFile implements TimeUpdate {
    private Double oldTimeUpdate = ReadingWritingTimeUpdate.readFromFile();
    private URL urlTimeUpdate = new URL("https://tosamara.ru/api/v2/classifiers");

    public TimeUpdateUsingLocalFile() throws IOException {
    }

    @Override
    public boolean updateOrNot() throws IOException, JAXBException {
      Classifiers classifiers = Unmarshalling.unmarshallTimeUpdate(urlTimeUpdate);
        Double newTimeUpdate = classifiers.getTimeUpdate();
        if (oldTimeUpdate == null || newTimeUpdate > oldTimeUpdate) {
            oldTimeUpdate = newTimeUpdate;
            ReadingWritingTimeUpdate.writeToFile(oldTimeUpdate);
            return true;
        }
        return false;
    }


}
