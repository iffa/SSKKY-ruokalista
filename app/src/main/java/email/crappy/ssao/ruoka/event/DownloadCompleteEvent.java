package email.crappy.ssao.ruoka.event;

/**
 * @author Santeri 'iffa'
 */
public class DownloadCompleteEvent {
    private boolean success;
    private String reason;

    public DownloadCompleteEvent(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    public boolean wasSuccessful() {
        return success;
    }

    public String getReason() {
        return reason;
    }
}
