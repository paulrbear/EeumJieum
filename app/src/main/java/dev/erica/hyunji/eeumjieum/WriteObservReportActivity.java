package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WriteObservReportActivity extends FragmentActivity {
    ArrayList<UserListItem> data = new ArrayList<>();
    int selectedRoom = 0;
    UserListAdapter adapter;
    String savedID;
    int savedMode;
    int selected_date = 0;
    String mode;
    String writername;
    String writerroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_observ_report);

        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("userMode");
        mode = intent.getExtras().getString("mode");
        selected_date = intent.getExtras().getInt("selected_date");

        if(mode.equals("search")){
            TextView tmp = (TextView) findViewById(R.id.top_title);
            tmp.setText("일지 검색");
        }//else "write"--> sekect observe report object

        listinint();

        LinearLayout tmpBG = (LinearLayout) findViewById(R.id.content_bg);
        tmpBG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                EditText et = (EditText)findViewById(R.id.search_name);
                imm.hideSoftInputFromWindow(et.getWindowToken(),0);
            }
        });

    }


    private AdapterView.OnItemClickListener itemClickListenerOfUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String objectName = data.get(position).getName();
            Toast.makeText(getApplicationContext(), objectName, Toast.LENGTH_SHORT).show();
            Intent intent;

            if(mode.equals("write")) {
                intent = new Intent(getApplicationContext(), WriteObservReport2Activity.class);
            }else {         //search
                intent = new Intent(getApplicationContext(), SearchObservReportActivity.class);
            }

            intent.putExtra("userID",savedID);
            intent.putExtra("objectName", objectName);
            intent.putExtra("userMode", savedMode);
            intent.putExtra("selected_date", selected_date);
            switch (selectedRoom){
                case 1: intent.putExtra("selectedRoom", "기쁨방");
                    break;
                case 2: intent.putExtra("selectedRoom", "믿음방");
                    break;
                case 3: intent.putExtra("selectedRoom", "은혜방");
                    break;
            }

            startActivityForResult(intent,0);
            overridePendingTransition(0,0);     //activity transition animation delete
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 1:     //observereport registration complete
                setResult(1);
                finish();
                break;
            default:
                setResult(-1);
                finish();
                break;
        }
    }




    public void listinint(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<RoomUserItem> userlist;

        userlist = handler.getRoomUser("기쁨방");
        ListView listView = (ListView) findViewById(R.id.userlistview);

        Iterator iterator = userlist.iterator();

        while (iterator.hasNext()){
            RoomUserItem tmpuser = (RoomUserItem) iterator.next();
            UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), "기쁨방");
            data.add(tmp);
        }

        adapter = new UserListAdapter(this, R.layout.room_userlist_item, data);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(itemClickListenerOfUserList);


        Button tmp_btn1 = (Button) findViewById(R.id.room1);
        Button tmp_btn2 = (Button) findViewById(R.id.room2);
        Button tmp_btn3 = (Button) findViewById(R.id.room3);

        switch (selectedRoom){
            case 1:
                break;
            case 2:
                tmp_btn2.setBackgroundResource(android.R.color.transparent);
                break;
            case 3:
                tmp_btn3.setBackgroundResource(android.R.color.transparent);
                break;
        }

        selectedRoom = 1;
        tmp_btn1.setBackgroundResource(R.drawable.shape_oval_room);


    }
    public void listViewSet(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<RoomUserItem> userlist ;

        data.clear();

        if(selectedRoom == 1) {
            userlist = handler.getRoomUser("기쁨방");
            Iterator iterator = userlist.iterator();

            while (iterator.hasNext()) {
                RoomUserItem tmpuser = (RoomUserItem) iterator.next();
                UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), "기쁨방");
                data.add(tmp);
            }
        }else if(selectedRoom == 2) {
            userlist = handler.getRoomUser("믿음방");
            Iterator iterator = userlist.iterator();
            while (iterator.hasNext()) {
                RoomUserItem tmpuser = (RoomUserItem) iterator.next();
                UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), "믿음방");
                data.add(tmp);
            }

        }else if(selectedRoom == 3){
            userlist = handler.getRoomUser("은혜방");
            Iterator iterator = userlist.iterator();
            while (iterator.hasNext()) {
                RoomUserItem tmpuser = (RoomUserItem) iterator.next();
                UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), "은혜방");
                data.add(tmp);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void onClick_room1btn(View v){
        Button tmp_btn1 = (Button) findViewById(R.id.room1);
        Button tmp_btn2 = (Button) findViewById(R.id.room2);
        Button tmp_btn3 = (Button) findViewById(R.id.room3);

        switch (selectedRoom){
            case 1:
                return;
            case 2:
                tmp_btn2.setBackgroundResource(android.R.color.transparent);
                break;
            case 3:
                tmp_btn3.setBackgroundResource(android.R.color.transparent);
                break;
        }

        selectedRoom = 1;
        tmp_btn1.setBackgroundResource(R.drawable.shape_oval_room);
        listViewSet();
    }
    public void onClick_room2btn(View v){
        Button tmp_btn1 = (Button) findViewById(R.id.room1);
        Button tmp_btn2 = (Button) findViewById(R.id.room2);
        Button tmp_btn3 = (Button) findViewById(R.id.room3);

        switch (selectedRoom){
            case 1:
                tmp_btn1.setBackgroundResource(android.R.color.transparent);
                break;
            case 2:
                return;
            case 3:
                tmp_btn3.setBackgroundResource(android.R.color.transparent);
                break;
        }

        selectedRoom = 2;
        tmp_btn2.setBackgroundResource(R.drawable.shape_oval_room);
        listViewSet();
    }
    public void onClick_room3btn(View v) {
        Button tmp_btn1 = (Button) findViewById(R.id.room1);
        Button tmp_btn2 = (Button) findViewById(R.id.room2);
        Button tmp_btn3 = (Button) findViewById(R.id.room3);

        switch (selectedRoom) {
            case 1:
                tmp_btn1.setBackgroundResource(android.R.color.transparent);
                break;
            case 2:
                tmp_btn2.setBackgroundResource(android.R.color.transparent);
                break;
            case 3:
                return;
        }

        selectedRoom = 3;
        tmp_btn3.setBackgroundResource(R.drawable.shape_oval_room);
        listViewSet();
    }

    public void onClick_backbtn(View v){
        finish();
    }

}
