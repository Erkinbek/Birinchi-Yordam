package uz.itex.firstmedicalaid.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import uz.itex.firstmedicalaid.R;
import uz.itex.firstmedicalaid.events.EventBus;
import uz.itex.firstmedicalaid.events.ExceptionHappenedEvent;
import uz.itex.firstmedicalaid.events.IEventSubscriber;
import uz.itex.firstmedicalaid.events.LocationSentEvent;
import uz.itex.firstmedicalaid.events.UnexpectedResponseEvent;
import uz.itex.firstmedicalaid.location.ILocationConsumer;
import uz.itex.firstmedicalaid.location.UniversalLocationProvider;
import uz.itex.firstmedicalaid.tasks.SendLocationTask;
import uz.itex.firstmedicalaid.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ILocationConsumer, IEventSubscriber {

    private String[] requiredPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final int PERMISSIONS_REQUEST_CODE = 10;

    private UniversalLocationProvider locationProvider;
    private Long requestId;
    private boolean isListening;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        locationProvider = new UniversalLocationProvider(this);
        final FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isListening) {
                    locationProvider.stopLocationProvider();
                    button.setImageResource(R.drawable.gps);
                    isListening = false;
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!PermissionUtils.isPermissionsGranted(MainActivity.this, requiredPermissions)) {
                        requestPermissions(requiredPermissions, PERMISSIONS_REQUEST_CODE);
                        return;
                    }
                }

                startListeningLocation();
                if (isListening) {
                    button.setImageResource(R.drawable.ic_close);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        EventBus.getInstance().subscribe(this, LocationSentEvent.class, UnexpectedResponseEvent.class, ExceptionHappenedEvent.class);
    }

    @Override
    protected void onPause() {
        super.onPause();

        EventBus.getInstance().unsubscribe(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.app_has_not_location_permissions, Toast.LENGTH_LONG).show();
                    return;
                }
            }

            startListeningLocation();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Class activityClass;
        switch (item.getItemId()) {
            case R.id.nav_kuyish:
                activityClass = Kuyish.class;
                break;

            case R.id.nav_elektr:
                activityClass = Elektr.class;
                break;

            case R.id.nav_zaxarlanish:
                activityClass = Zaxarlanish.class;
                break;

            case R.id.nav_qon:
                activityClass = QonKetishi.class;
                break;

            case R.id.nav_hushsizlik:
                activityClass = Hushsizlik.class;
                break;

            case R.id.nav_quyosh:
                activityClass = QuyoshUrishi.class;
                break;

            default:
                activityClass = null;
                break;
        }

        if (activityClass != null) {
            startActivity(new Intent(this, activityClass));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startListeningLocation() {
        requestId = System.currentTimeMillis();
        isListening = locationProvider.startLocationProvider(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        new SendLocationTask(requestId, location).execute();
    }

    @Override
    public void onEvent(Object event) {
        if (event instanceof LocationSentEvent) {
            Toast.makeText(this, R.string.location_sent, Toast.LENGTH_SHORT).show();
        } else if (event instanceof UnexpectedResponseEvent) {
            Toast.makeText(this, R.string.unexpected_reponse, Toast.LENGTH_LONG).show();
            Log.e("SERVER_RESPONSE_CODE", String.valueOf(((UnexpectedResponseEvent) event).getResponseCode()));
            Log.e("SERVER_RESPONSE_BODY", String.valueOf(((UnexpectedResponseEvent) event).getResponseBody()));
        } else if (event instanceof ExceptionHappenedEvent) {
            Exception exception = ((ExceptionHappenedEvent) event).getException();
            int messageResId = 0;
            if (exception instanceof IOException) {
                messageResId = R.string.connection_error;
            } else if (exception instanceof JSONException) {
                messageResId = R.string.could_not_create_request;

            }

            if (messageResId != 0) {
                Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show();
            }
            exception.printStackTrace();
        }
    }
}
