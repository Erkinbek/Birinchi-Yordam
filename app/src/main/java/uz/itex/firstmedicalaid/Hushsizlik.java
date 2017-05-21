package uz.itex.firstmedicalaid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Hushsizlik extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hushsizlik_c);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
