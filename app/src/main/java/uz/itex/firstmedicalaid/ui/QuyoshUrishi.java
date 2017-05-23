package uz.itex.firstmedicalaid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uz.itex.firstmedicalaid.R;

public class QuyoshUrishi extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quyosh_urishi_c);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
