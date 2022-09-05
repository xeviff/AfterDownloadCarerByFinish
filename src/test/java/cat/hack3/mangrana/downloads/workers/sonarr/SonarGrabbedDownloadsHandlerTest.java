package cat.hack3.mangrana.downloads.workers.sonarr;

import cat.hack3.mangrana.config.ConfigFileLoader;
import cat.hack3.mangrana.exception.IncorrectWorkingReferencesException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class SonarGrabbedDownloadsHandlerTest {

    ConfigFileLoader configFileLoader;

    SonarGrabbedDownloadsHandler sonarGrabbedDownloadsHandler;

    public SonarGrabbedDownloadsHandlerTest() throws IncorrectWorkingReferencesException {
        configFileLoader = new ConfigFileLoader();
    }

    @Test
    public void testJobResume () throws IOException {
        sonarGrabbedDownloadsHandler = new SonarGrabbedDownloadsHandler(configFileLoader);
        String job1 = "Power Book III - Raising Kanan S02E04 ..";
        String job2 = "Rick y Morty S06E01 HMAX WEB-DL 1080p ..";
        String job3 = "P-Valley S02E10 1080p AMZN WEB-DL ESP-..";
        sonarGrabbedDownloadsHandler.jobsState.put(job1, "initiated");
        sonarGrabbedDownloadsHandler.jobsState.put(job2, "initiated");
        sonarGrabbedDownloadsHandler.jobsState.put(job3, "initiated");

        sonarGrabbedDownloadsHandler.reportDelayCounter = 11;
        System.out.println("now should be printed");
        sonarGrabbedDownloadsHandler.resumeJobsLogPrint();
        Assert.assertTrue(sonarGrabbedDownloadsHandler.sameResumeAlreadyPrinted());

        sonarGrabbedDownloadsHandler.jobsState.put(job3, "finished");
        Assert.assertFalse(sonarGrabbedDownloadsHandler.sameResumeAlreadyPrinted());

        sonarGrabbedDownloadsHandler.reportDelayCounter = 11;
        System.out.println("now should be printed again");
        sonarGrabbedDownloadsHandler.resumeJobsLogPrint();
        Assert.assertTrue(sonarGrabbedDownloadsHandler.sameResumeAlreadyPrinted());

        sonarGrabbedDownloadsHandler.jobsState.remove(job1);
        Assert.assertFalse(sonarGrabbedDownloadsHandler.sameResumeAlreadyPrinted());

        sonarGrabbedDownloadsHandler.reportDelayCounter = 11;
        System.out.println("and again");
        sonarGrabbedDownloadsHandler.resumeJobsLogPrint();

        System.out.println("finally should not be printed, and that's all, bye!");
        sonarGrabbedDownloadsHandler.reportDelayCounter = 11;
        sonarGrabbedDownloadsHandler.resumeJobsLogPrint();
    }


}