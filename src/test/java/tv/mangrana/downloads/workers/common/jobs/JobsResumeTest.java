package tv.mangrana.downloads.workers.common.jobs;

import org.testng.Assert;
import org.testng.annotations.Test;
import tv.mangrana.jobs.JobFileManager;

public class JobsResumeTest {

    JobsResume jobsResume = new JobsResume();
    
    @Test
    public void testJobResume () throws InterruptedException {
        String job1 = "Power Book III - Raising Kanan S02E04 ..";
        String job2 = "Rick y Morty S06E01 HMAX WEB-DL 1080p ..";
        String job3 = "P-Valley S02E10 1080p AMZN WEB-DL ESP- ..";
        jobsResume.put(JobFileManager.JobFileType.SONARR_JOBS, "1", job1, "initiated");
        Thread.sleep(1500);
        jobsResume.put(JobFileManager.JobFileType.SONARR_JOBS, "2", job2, "initiated");
        Thread.sleep(1500);
        jobsResume.put(JobFileManager.JobFileType.SONARR_JOBS, "3", job3, "initiated");
        Thread.sleep(1500);

        jobsResume.reportDelayCounter = 11;
        System.out.println("now should be printed");
        jobsResume.resumeJobsLogPrint();
        Assert.assertTrue(jobsResume.sameResumeAlreadyPrinted());

        jobsResume.put(JobFileManager.JobFileType.SONARR_JOBS, "3", job3, "finished");
        Thread.sleep(1500);
        Assert.assertFalse(jobsResume.sameResumeAlreadyPrinted());

        jobsResume.reportDelayCounter = 11;
        System.out.println("now should be printed again");
        jobsResume.resumeJobsLogPrint();
        Assert.assertTrue(jobsResume.sameResumeAlreadyPrinted());

        String job4 = "Señor de los anillos - el cuento de -...";
        jobsResume.put(JobFileManager.JobFileType.RADARR_JOBS, "4", job4, "initiated");
        Assert.assertFalse(jobsResume.sameResumeAlreadyPrinted());

        jobsResume.reportDelayCounter = 11;
        System.out.println("and again");
        jobsResume.resumeJobsLogPrint();

        System.out.println("finally should not be printed, and that's all, bye!");
        jobsResume.reportDelayCounter = 11;
        jobsResume.resumeJobsLogPrint();
    }

}