package dev.erica.hyunji.eeumjieum;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class JoinActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().setStatusBarColor(Color.parseColor("#F7F7F7"));
        }
        setContentView(R.layout.activity_join);
    }

    public void onClick_joinParent(View v){
        Intent intent = new Intent(getApplicationContext(), JoinParentActivity.class);
        startActivity(intent);
    }

    public void onClick_joinWorker(View v){
        Intent intent = new Intent(getApplicationContext(), JoinWorkerActivity.class);
        startActivity(intent);
    }

    public void onClick_backbtn(View v){
        finish();
    }


}
