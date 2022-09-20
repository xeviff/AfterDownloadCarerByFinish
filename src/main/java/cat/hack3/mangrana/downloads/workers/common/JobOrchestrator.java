package cat.hack3.mangrana.downloads.workers.common;

public interface JobOrchestrator {

    public boolean isWorkingWithAJob();

    public boolean isJobWorking(String jobTitle);

    public void jobInitiated(String jobTitle);

    public void jobHasFileName(String jobTitle);

    public void jobWorking(String jobTitle);

    public void jobFinished(String jobTitle, String fileName);

    public void jobError(String jobTitle, String fileName);

}
