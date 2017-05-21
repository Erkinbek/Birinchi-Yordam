package uz.itex.firstmedicalaid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class QonKetishi extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qon_ketishi_c);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
