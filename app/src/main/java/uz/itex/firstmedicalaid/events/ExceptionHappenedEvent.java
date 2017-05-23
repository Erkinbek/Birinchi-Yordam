package uz.itex.firstmedicalaid.events;

/**
 * Created by Bakhtiyorbek Begmatov 23.05.2017
 */

public class ExceptionHappenedEvent {

    private final Exception exception;

    public ExceptionHappenedEvent(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
