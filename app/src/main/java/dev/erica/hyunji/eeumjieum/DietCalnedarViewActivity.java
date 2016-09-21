package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class DietCalnedarViewActivity extends FragmentActivity {

    private String savedID;
    private int savedMode;

    private TextView month_tv;
    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;

    private int current_year, current_month;

    ArrayList<DietListItem> data = new ArrayList<>();
    DietCalendarViewListAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_calnedar_view);

        //user mode and ID setting
        Intent intent = getIntent();
        savedMode = intent.getExtras().getInt("userMode");
        savedID = intent.getExtras().getString("userID");
        if(savedMode == 0) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            savedID = pref.getString("id", "");
            savedMode = pref.getInt("mode", 0);
        }


        month_tv = (TextView) findViewById(R.id.month_tv);
        gridView = (GridView) findViewById(R.id.gridview);

        initlist();
        initcalendarView();
    }

    public void initlist(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<DietListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        current_year = year;
        current_month = month;

        String day = year + "/" + month;

        articlelist = handler.getDietbyDay(day);
        ListView listView = (ListView) findViewById(R.id.diet_listview);

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            data.add((DietListItem) iterator.next());
        }

        adapter = new DietCalendarViewListAdapter(this, R.layout.diet_listview_item2, data);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListenerOfDietList);
    }

    public void updatelist(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<DietListItem> articlelist;

        int year = current_year;
        int month = current_month ;

        String day = year + "/" + month;

        articlelist = handler.getDietbyDay(day);

        data.clear();

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            data.add((DietListItem) iterator.next());
        }
        adapter.notifyDataSetChanged();
    }


    public void initcalendarView(){

        mCal = Calendar.getInstance();

        int year = mCal.get(Calendar.YEAR);
        int month = mCal.get(Calendar.MONTH)+1;
        current_year = year;
        current_month = month;
        if(month < 10){
            String str = "0" + month + "월";
            month_tv.setText(str);
        }else{
            String str = month + "월";
            month_tv.setText(str);
        }

        dayList = new ArrayList<>();

        setCalendarDate(year, month);
        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

    }

    private void setCalendarDate(int year, int month) {

        MyDBHandler handler = MyDBHandler.open(getApplicationContext());

        if(month < 10){
            String str = "0" + month + "월";
            month_tv.setText(str);
        }else{
            String str = month + "월";
            month_tv.setText(str);
        }

        dayList.clear();

        //이번달 1일 무슨요일인지 판단 mCal.set(Year,Month,Day)


        //gridview 요일 표시

        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");



        mCal.set(year,month-1,1);
        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        //1일 - 요일 매칭 시키기 위해 공백 add
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }

        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            int day = i + 1;
            String strday = year + "/" + month + "/" + day;
            boolean tmpbool = handler.isScheduleExist(strday);
            dayList.add(String.valueOf(day));
        }
    }


    public void onClick_backbtn(View v){
        finish();
    }

    public void onClick_prevbtn(View v){
        if(current_month == 1){
            current_year = current_year - 1;
            current_month = 12;
            setCalendarDate(current_year, current_month);
            String str = String.valueOf(current_month) + "월";
            month_tv.setText(str);
        }else {
            current_month = current_month - 1;
            setCalendarDate(current_year, current_month);
            if(current_month<10){
                String str ="0" + String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }else{
                String str = String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }

        }
        gridAdapter.notifyDataSetChanged();
        updatelist();
    }

    public void onClick_nextbtn(View v){
        if(current_month == 12){
            current_year = current_year + 1;
            current_month = 1;
            setCalendarDate(current_year, current_month);
            if(current_month<10){
                String str ="0" + String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }else{
                String str = String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }
        }else {
            current_month = current_month + 1;
            setCalendarDate(current_year, current_month);
            if(current_month<10){
                String str ="0" + String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }else{
                String str = String.valueOf(current_month) + "월";
                month_tv.setText(str);
            }
        }
        gridAdapter.notifyDataSetChanged();
        updatelist();
    }

    private class GridAdapter extends BaseAdapter {
        private List<String> list;
        private LayoutInflater inflater;

        private class ViewHolder {
            TextView day_tv;
        }

        public GridAdapter(Context context, List<String> list){
            this.list = list;
            this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = inflater.inflate(R.layout.calendar_gridview_item, parent, false);
                holder = new ViewHolder();
                holder.day_tv = (TextView) convertView.findViewById(R.id.gridview_day_tv);
                  convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            String tmpstr = "" + getItem(position);
            holder.day_tv.setText(tmpstr);

            holder.day_tv.setTextColor(Color.WHITE);
            holder.day_tv.setBackground(null);


            mCal = Calendar.getInstance();
            //오늘 day 가져옴
            if( (mCal.get(Calendar.YEAR) == current_year) && ( mCal.get(Calendar.MONTH) == (current_month-1) )  ) {
                Integer today = mCal.get(Calendar.DAY_OF_MONTH);
                String sToday = String.valueOf(today);
                if (sToday.equals(getItem(position))) { //오늘 day 텍스트 컬러 변경
                    holder.day_tv.setTextColor(getResources().getColor(R.color.colorSelectFont));
                    holder.day_tv.setBackgroundResource(R.drawable.shape_oval_white);

                }
            }

            return convertView;

        }
    }


    private AdapterView.OnItemClickListener itemClickListenerOfDietList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //String objectName = data.get(position).getObjectname();
            int articleKey = data.get(position).getArticlekey();
            //Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), WriteDietActivity.class);

            intent.putExtra("userID", savedID);
            intent.putExtra("userMode", savedMode);
            intent.putExtra("articleKey", articleKey);
            intent.putExtra("mode", "detailview");

            startActivity(intent);
            overridePendingTransition(0,0);     //activity transition animation delete

        }
    };


}
