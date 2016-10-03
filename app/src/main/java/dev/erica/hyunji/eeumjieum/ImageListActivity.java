package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ImageListActivity extends FragmentActivity {
    List<Integer> selectedPicture;
    GridView gridView;
    boolean[] selected;
    int len;
    String mode;
    private ImageAdapter mAdapter;

    int image_for = 0;

    public Integer[] mThumbIds = {
            R.drawable.s_pic_1, R.drawable.s_pic_2, R.drawable.s_pic_3,
            R.drawable.s_pic_1, R.drawable.s_pic_2, R.drawable.s_pic_3,
            R.drawable.s_pic_1, R.drawable.s_pic_2, R.drawable.s_pic_3,
            R.drawable.s_pic_1, R.drawable.s_pic_2, R.drawable.s_pic_3,
            R.drawable.s_pic_1, R.drawable.s_pic_2, R.drawable.s_pic_3,
            R.drawable.s_pic_1, R.drawable.s_pic_2, R.drawable.s_pic_3
    };


    class ViewHolder{
        ImageView imageview;
        ImageView selectedimg;
        TextView selectedtxt;
        int id;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);


        Intent intent = getIntent();
        mode = intent.getExtras().getString("mode");

        if(mode.equals("program")) {
            LinearLayout tll = (LinearLayout) findViewById(R.id.top_titlebar_color_layout);
            tll.setBackgroundResource(R.color.colorCyanMain);
            TextView tv = (TextView) findViewById(R.id.total_limit_tfd);
            tv.setText("/50");
            tv = (TextView) findViewById(R.id.title_tfd);
            tv.setText("프로그램 일지");
        }else if(mode.equals("diet")){
            LinearLayout tll = (LinearLayout) findViewById(R.id.top_titlebar_color_layout);
            tll.setBackgroundResource(R.color.colorCyanMain);
            TextView tv = (TextView) findViewById(R.id.title_tfd);
            tv.setText("식단 선택");

            image_for = intent.getExtras().getInt("imgfor");

        }else {
            LinearLayout tll = (LinearLayout) findViewById(R.id.top_titlebar_color_layout);
            tll.setBackgroundResource(R.color.colorCyanMain);
            TextView tv = (TextView) findViewById(R.id.total_limit_tfd);
            tv.setText("/50");
            tv = (TextView) findViewById(R.id.title_tfd);
            tv.setText("사진 선택");
        }

        gridView = (GridView) findViewById(R.id.grid_view);

        mAdapter = new ImageAdapter(this);
        gridView.setAdapter(mAdapter);

        selectedPicture = new ArrayList<>();

        len = gridView.getCount();
        selected = new boolean[len];
        Arrays.fill(selected, false);

    }

    public int getSelectedCount(){
        int count=0;
        for(int i =0; i<len; i++){
            if(selected[i]){
                count++;
            }
        }
        return count;
    }

    public void onClick_donebtn(View v){
        int len = gridView.getCount();
        int count = 0;
        for(int i=0; i<len; i++){
            if(selected[i]){
                count++;
            }
        }
        if(count == 0){
            Toast.makeText(getApplicationContext(), "image not selected", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent();
        ArrayList<Integer> picID = new ArrayList<>();
        Iterator iterator = selectedPicture.iterator();
        while(iterator.hasNext()){
            Integer tmp = (Integer) iterator.next();
            System.out.println("SelectedPicture id " + mThumbIds[tmp].toString());
            picID.add(mThumbIds[tmp]);
        }

        intent.putIntegerArrayListExtra("result", picID);
        intent.putExtra("imgfor", image_for);   //diet phto classification
        setResult(1, intent);       //photo selected = 1
        finish();
    }
    public void onClick_cancelbtn(View v){
        Toast.makeText(getApplicationContext(), "image select canceled", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        ArrayList<Integer> picID = new ArrayList<>();
        intent.putIntegerArrayListExtra("result", picID);
        setResult(RESULT_OK, intent);
        finish();
    }


    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;

        public boolean[] selection ;
        int pre_selected = -1;

    /*R.drawable.pic_9,
    R.drawable.pic_10, R.drawable.pic_11, R.drawable.pic_12,
    R.drawable.pic_13, R.drawable.pic_14, R.drawable.pic_15,
    R.drawable.pic_16, R.drawable.pic_17, R.drawable.pic_18,
    R.drawable.pic_19, R.drawable.pic_20, R.drawable.pic_21,
    R.drawable.pic_22, R.drawable.pic_23*/



        public ImageAdapter(Context c){
            mContext = c;
            mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            selection = new boolean[mThumbIds.length];
            Arrays.fill(selection, false);
        }

        @Override
        public int getCount() {
            return mThumbIds.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.galleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
                holder.selectedimg = (ImageView) convertView.findViewById(R.id.selectedImg);
                holder.selectedtxt = (TextView) convertView. findViewById(R.id.selectedTxt);

                convertView.setTag(holder);

                holder.imageview.setLayoutParams(new FrameLayout.LayoutParams(220,220));
                holder.imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.selectedimg.setLayoutParams(new FrameLayout.LayoutParams(220,220));
                holder.selectedimg.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.imageview.setId(position);
            holder.selectedimg.setId(position);
            holder.selectedtxt.setId(position);


            if(holder.id == pre_selected){
                holder.selectedimg.setVisibility(View.GONE);
                holder.selectedtxt.setVisibility(View.GONE);
            }

            holder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if(mode.equals("diet")){

                        for(int i = 0; i <len; i ++){
                            if (selected[i]){
                                pre_selected = i;
                                selected[i] = false;

                                break;
                            }
                        }
                        for(Iterator<Integer> it = selectedPicture.iterator(); it.hasNext();) {
                            Integer val = it.next();
                            if(val.equals(id)) {
                                it.remove();
                            }
                        }

                        selected[id] = true;
                        holder.selectedimg.setVisibility(View.VISIBLE);
                        holder.selectedtxt.setVisibility(View.VISIBLE);
                        holder.selectedimg.bringToFront();
                        holder.selectedtxt.bringToFront();

                        TextView tfd = (TextView) findViewById(R.id.selected_count_tfd);
                        tfd.setText("1");

                        selectedPicture.add(id);
                        notifyDataSetChanged();
                    }


                   else if(!selected[id]){
                        selected[id] = true;
                        holder.selectedimg.setVisibility(View.VISIBLE);
                        holder.selectedtxt.setVisibility(View.VISIBLE);
                        holder.selectedimg.bringToFront();
                        holder.selectedtxt.bringToFront();
                        holder.selectedtxt.setText(""+getSelectedCount());

                       FrameLayout fd = (FrameLayout) findViewById(R.id.current_select_layout);
                       fd.setVisibility(View.VISIBLE);
                       TextView tfd = (TextView) findViewById(R.id.selected_count_tfd);
                       tfd.setText(""+getSelectedCount());

                       selectedPicture.add(id);

                    }
                }
            });

            holder.selectedimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if(mode.equals("diet")){

                    }else if(selected[id]){
                        selected[id] = false;
                        holder.selectedimg.setVisibility(View.GONE);
                        holder.selectedtxt.setVisibility(View.GONE);
                        //holder.imageview.bringToFront();

                        FrameLayout fd = (FrameLayout) findViewById(R.id.current_select_layout);

                        if(getSelectedCount() == 0){
                            fd.setVisibility(View.GONE);
                        }else{
                            fd.setVisibility(View.VISIBLE);
                            TextView tfd = (TextView) findViewById(R.id.selected_count_tfd);
                            tfd.setText(""+getSelectedCount());
                        }
                        for(Iterator<Integer> it = selectedPicture.iterator(); it.hasNext();) {
                            Integer val = it.next();
                            if(val.equals(id)) {
                                it.remove();
                            }
                        }

                    }
                }
            });

            holder.imageview.setImageResource(mThumbIds[position]);
            holder.id = position;
            return convertView;

        }

    }

}
