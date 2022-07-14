package cat.hack3.mangrana.google.api.client;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.TeamDrive;
import org.o7planning.googledrive.example.GoogleDriveUtils;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static cat.hack3.mangrana.utils.Output.log;

public class QuickTester {

    public static void main(String[] args) throws IOException {
        new QuickTester()
                .listTDs();
    }

    public QuickTester() throws IOException {
        service = GoogleDriveUtils.getDriveService();
    }

    Drive service;

    private void listTDs() throws IOException {
        List<TeamDrive> list = Optional.ofNullable(
                service.teamdrives().list().execute().getTeamDrives())
                        .orElseThrow(() -> new NoSuchElementException("No TDs found :("));

        list.forEach(td -> log(td.getName()));
        log("Ok, if you have been seeing some TDs in the output, that means GoogleDriveUtils works \uD83D\uDC4D");
    }

}
