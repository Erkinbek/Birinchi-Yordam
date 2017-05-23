package uz.itex.firstmedicalaid.tasks;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import uz.itex.firstmedicalaid.Constants;
import uz.itex.firstmedicalaid.events.EventBus;
import uz.itex.firstmedicalaid.events.ExceptionHappenedEvent;
import uz.itex.firstmedicalaid.events.LocationSentEvent;
import uz.itex.firstmedicalaid.events.UnexpectedResponseEvent;

/**
 * Created by Bakhtiyorbek Begmatov on 23.05.2017
 */

public class SendLocationTask extends AsyncTask<Void, String, Void> {

    private Long requestId;
    private Location location;

    public SendLocationTask(Long requestId, Location location) {
        this.requestId = requestId;
        this.location = location;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            URL url = new URL(Constants.SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            JSONObject json = new JSONObject();
            json.put("id", requestId);
            json.put("lat", location.getLatitude());
            json.put("long", location.getLongitude());

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(json.toString());

            Looper.prepare();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                EventBus.getInstance().postEvent(new LocationSentEvent());
            } else {
                EventBus.getInstance().postEvent(new UnexpectedResponseEvent(
                        connection.getResponseCode(),
                        connection.getResponseMessage()
                ));
            }
            Looper.loop();

        } catch (IOException | JSONException e) {
            Looper.prepare();
            EventBus.getInstance().postEvent(new ExceptionHappenedEvent(e));
            Looper.loop();
        }

        return null;
    }
}
