package uz.itex.firstmedicalaid.events;

/**
 * Created by Bakhtiyorbek Begmatov on 23.05.2017
 */

public class UnexpectedResponseEvent {

    private final int responseCode;
    private final String responseBody;

    public UnexpectedResponseEvent(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}
