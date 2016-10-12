package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;

public class ExtraImageActivity extends FragmentActivity {
    private ArrayList<Integer> mThumbIds= new ArrayList<>();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_image);

        Intent intent = getIntent();
        String photo = intent.getExtras().getString("photo");
        String mode = intent.getStringExtra("mode");

        String arr[] = photo.split("/");
        if(arr.length > 0){
            for (int i = 1; i < arr.length; i++) {
                mThumbIds.add(Integer.parseInt(arr[i]));
            }
        }

        GridView gridview = (GridView) findViewById(R.id.grid_view);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), ""+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ExtraImageDetailActivity.class);
                intent.putExtra("totalPhoto",mThumbIds.size());
                intent.putExtra("position", position+1);
                intent.putExtra("photourl",mThumbIds.get(position));
                startActivity(intent);
                overridePendingTransition(0,0);     //activity transition animation delete
            }
        });



        if(mode.equals("album")){
            TextView tv = (TextView) findViewById(R.id.article_title_tv);
            tv.setVisibility(View.VISIBLE);
            tv.setText(intent.getStringExtra("title"));
            tv = (TextView) findViewById(R.id.article_date_tv);
            tv.setVisibility(View.VISIBLE);
            tv.setText(intent.getStringExtra("day"));
            tv = (TextView) findViewById(R.id.top_title);
            tv.setText("앨범");

            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            p.addRule(RelativeLayout.BELOW, R.id.article_date_tv);
            gridview.setLayoutParams(p);
        }




    }

    public void onClick_backbtn(View v){
        finish();
    }


    public class ImageAdapter extends BaseAdapter{
        private Context mContext;

        public ImageAdapter(Context c){
            mContext = c;
        }
        @Override
        public int getCount() {
            return mThumbIds.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(320, 320));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                //imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            if(mThumbIds.size()==0) {
                return imageView;
            }
            imageView.setImageResource(mThumbIds.get(position));
            return imageView;

        }
    }



}
