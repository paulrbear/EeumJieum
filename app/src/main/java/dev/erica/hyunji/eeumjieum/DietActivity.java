package dev.erica.hyunji.eeumjieum;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class DietActivity extends FragmentActivity {

    private Button program_btn, observ_report_btn, work_report_btn, notice_btn, schedule_btn;
    private TextView notice_lb, schedule_lb, program_lb, observ_report_lb, work_report_lb;
    private Animation menuup, menudown;
    private int selected_menu = 0;

    private String savedID;
    private int savedMode;

    ArrayList<DietListItem> data = new ArrayList<>();
    DietListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);


        //user mode and ID setting
        Intent intent = getIntent();
        savedMode = intent.getExtras().getInt("userMode");
        savedID = intent.getExtras().getString("userID");
        if(savedMode == 0) {
            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
            savedID = pref.getString("id", "");
            savedMode = pref.getInt("mode", 0);
        }

        notice_btn = (Button) findViewById(R.id.notice_btn);
        schedule_btn = (Button) findViewById(R.id.schedule_btn);
        program_btn = (Button) findViewById(R.id.program_btn);
        observ_report_btn = (Button) findViewById(R.id.observ_report_btn);
        work_report_btn = (Button) findViewById(R.id.work_report_btn);

        notice_lb = (TextView) findViewById(R.id.notice_label);
        schedule_lb = (TextView) findViewById(R.id.schedule_label);
        program_lb = (TextView) findViewById(R.id.program_label);
        observ_report_lb = (TextView) findViewById(R.id.observ_report_label);
        work_report_lb = (TextView) findViewById(R.id.work_report_label);

        //bottom pop-menu
        menuup = AnimationUtils.loadAnimation(this, R.anim.note_up_animation);
        menudown = AnimationUtils.loadAnimation(this, R.anim.note_down_animation);

        initlist();

    }

    public void initlist(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<DietListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month;

        articlelist = handler.getDietbyDay(day);
        ListView listView = (ListView) findViewById(R.id.diet_listview);

        Iterator iterator = articlelist.iterator();
        int i = 0;
        while (iterator.hasNext()){
            data.add((DietListItem) iterator.next());
        }

        adapter = new DietListAdapter(this, R.layout.diet_listview_item, data);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListenerOfDietList);
    }

    public void updateList(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        ArrayList<DietListItem> articlelist;

        Calendar mCal = Calendar.getInstance();
        int year = mCal.get(mCal.YEAR);
        int month = mCal.get(mCal.MONTH) + 1;

        String day = year + "/" + month;

        articlelist = handler.getDietbyDay(day);

        data.clear();

        Iterator iterator = articlelist.iterator();

        while (iterator.hasNext()){
            data.add((DietListItem) iterator.next());
        }
        adapter.notifyDataSetChanged();
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



    //fab button (wirte btn)
    public void onClick_fabbtn(View v){
        Intent intent = new Intent(this, WriteDietActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);
        intent.putExtra("mode", "write");
        intent.putExtra("articleKey", 0);
        startActivityForResult(intent,0);
        overridePendingTransition(0,0);     //activity transition animation delete
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 7:     //diet registration complete
                updateList();
                break;
            default:
                break;
        }
    }


    public void onClick_calenderview(View v){
        Intent intent = new Intent(this, DietCalnedarViewActivity.class);
        intent.putExtra("userID",savedID);
        intent.putExtra("userMode", savedMode);

        startActivity(intent);
        overridePendingTransition(0,0);     //activity transition animation delete
    }


}
