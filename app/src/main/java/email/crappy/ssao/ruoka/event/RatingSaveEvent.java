package email.crappy.ssao.ruoka.event;

/**
 * @author Santeri 'iffa'
 */
public class RatingSaveEvent {
    String description;
    String opinion;

    public RatingSaveEvent(String description, String opinion) {
        this.description = description;
        this.opinion = opinion;
    }

    public String getDescription() {
        return description;
    }

    public String getOpinion() {
        return opinion;
    }
}
