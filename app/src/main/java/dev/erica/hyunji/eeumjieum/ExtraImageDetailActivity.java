package dev.erica.hyunji.eeumjieum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ExtraImageDetailActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_image_detail);

        Intent intent = getIntent();
        int totalPhoto = intent.getExtras().getInt("totalPhoto");
        int photonum = intent.getExtras().getInt("position");
        int photoid = intent.getExtras().getInt("photourl");

        TextView tmp = (TextView) findViewById(R.id.photonumtfd);
        String str = photonum + "/" + totalPhoto;
        tmp.setText(str);

        ImageView tmpiv = (ImageView) findViewById(R.id.bigimage);
        tmpiv.setImageResource(photoid);
    }

    protected  void onClick_close(View v){
        finish();
    }
}
