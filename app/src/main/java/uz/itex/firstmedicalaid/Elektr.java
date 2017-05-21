package uz.itex.firstmedicalaid;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Elektr extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elektr_c);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
