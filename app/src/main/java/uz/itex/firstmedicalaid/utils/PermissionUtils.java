package uz.itex.firstmedicalaid.utils;

import android.content.Context;
import android.support.v4.app.ActivityCompat;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Bakhtiyorbek Begmatov on 23.05.2017
 */

public class PermissionUtils {

    public static boolean isPermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
}
