package uz.itex.firstmedicalaid.location;

import android.location.Location;

/**
 * Created by Bakhtiyorbek Begmatov on 23.05.2017
 */

public interface ILocationConsumer {

    void onLocationChanged(Location location);
}
