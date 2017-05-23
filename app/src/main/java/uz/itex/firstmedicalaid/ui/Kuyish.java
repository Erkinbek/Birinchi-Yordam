package uz.itex.firstmedicalaid.ui;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import uz.itex.firstmedicalaid.R;

public class Kuyish extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kuyish_c);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
