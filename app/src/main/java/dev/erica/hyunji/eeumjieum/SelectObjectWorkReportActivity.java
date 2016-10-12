package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectObjectWorkReportActivity extends FragmentActivity {
    ArrayList<UserListItem> data = new ArrayList<>();
    ArrayList<Integer> statusdata = new ArrayList<>();       //  normal 0/ out 1/ hospital 2/ etc 3
    int selectedRoom = 0;
    UserListAdapter2 adapter;
    String savedID;
    int savedMode;
    String string_selected_day;

    String mode;//write/modify
    String current_room;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_object_work_report);

        Intent intent = getIntent();
        savedID = intent.getExtras().getString("userID");
        savedMode = intent.getExtras().getInt("userMode");
        string_selected_day = intent.getExtras().getString("selectedDay");
        mode = intent.getExtras().getString("mode");

        if(mode.equals("modify")){
            current_room = intent.getExtras().getString("currentRoom");
            statusdata = intent.getExtras().getIntegerArrayList("currentStatus");
            listappend();
        }else {
            listinint();
        }
    }

    public void listappend(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<RoomUserItem> userlist;

        userlist = handler.getRoomUser(current_room);
        ListView listView = (ListView) findViewById(R.id.userlistview);

        Iterator iterator = userlist.iterator();

        while (iterator.hasNext()){
            RoomUserItem tmpuser = (RoomUserItem) iterator.next();
            UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), current_room);
            data.add(tmp);
        }

        adapter = new UserListAdapter2(this, R.layout.room_userlist_workreport_item, data, statusdata);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListenerOfUserList);


        Button tmp_btn1 = (Button) findViewById(R.id.room1);
        Button tmp_btn2 = (Button) findViewById(R.id.room2);
        Button tmp_btn3 = (Button) findViewById(R.id.room3);

        if(current_room.equals("기쁨방")){
            selectedRoom = 1;
            tmp_btn1.setBackgroundResource(R.drawable.shape_oval_room);
            tmp_btn2.setBackgroundResource(android.R.color.transparent);
            tmp_btn2.setTextColor(getResources().getColor(R.color.colorMidGray));
            tmp_btn2.setClickable(false);
            tmp_btn3.setBackgroundResource(android.R.color.transparent);
            tmp_btn3.setTextColor(getResources().getColor(R.color.colorMidGray));
            tmp_btn3.setClickable(false);

        }else if(current_room.equals("믿음방")){
            selectedRoom = 2;
            tmp_btn2.setBackgroundResource(R.drawable.shape_oval_room);
            tmp_btn1.setBackgroundResource(android.R.color.transparent);
            tmp_btn1.setTextColor(getResources().getColor(R.color.colorMidGray));
            tmp_btn1.setClickable(false);
            tmp_btn3.setBackgroundResource(android.R.color.transparent);
            tmp_btn3.setTextColor(getResources().getColor(R.color.colorMidGray));
            tmp_btn3.setClickable(false);

        }else if(current_room.equals("은혜방")){
            selectedRoom = 3;
            tmp_btn3.setBackgroundResource(R.drawable.shape_oval_room);
            tmp_btn1.setBackgroundResource(android.R.color.transparent);
            tmp_btn1.setTextColor(getResources().getColor(R.color.colorMidGray));
            tmp_btn1.setClickable(false);
            tmp_btn2.setBackgroundResource(android.R.color.transparent);
            tmp_btn2.setTextColor(getResources().getColor(R.color.colorMidGray));
            tmp_btn2.setClickable(false);

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
            statusdata.add(0);
        }

        adapter = new UserListAdapter2(this, R.layout.room_userlist_workreport_item, data, statusdata);
        listView.setAdapter(adapter);
        //listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
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

    private AdapterView.OnItemClickListener itemClickListenerOfUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int tmpstatus = adapter.getStatus(position);
            tmpstatus = (tmpstatus+1)%4;
            statusdata.set(position, tmpstatus);

            adapter.notifyDataSetChanged();
        }
    };


    public void listViewSet(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<RoomUserItem> userlist ;

        data.clear();
        statusdata.clear();

        if(selectedRoom == 1) {
            userlist = handler.getRoomUser("기쁨방");
            Iterator iterator = userlist.iterator();

            while (iterator.hasNext()) {
                RoomUserItem tmpuser = (RoomUserItem) iterator.next();
                UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), "기쁨방");
                data.add(tmp);
                statusdata.add(0);
            }
        }else if(selectedRoom == 2) {
            userlist = handler.getRoomUser("믿음방");
            Iterator iterator = userlist.iterator();
            while (iterator.hasNext()) {
                RoomUserItem tmpuser = (RoomUserItem) iterator.next();
                UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), "믿음방");
                data.add(tmp);
                statusdata.add(0);
            }

        }else if(selectedRoom == 3){
            userlist = handler.getRoomUser("은혜방");
            Iterator iterator = userlist.iterator();
            while (iterator.hasNext()) {
                RoomUserItem tmpuser = (RoomUserItem) iterator.next();
                UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), "은혜방");
                data.add(tmp);
                statusdata.add(0);
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
    public void onClick_donebtn(View v){
        if(mode.equals("modify")){
            Intent tmp = new Intent();
            tmp.putExtra("StatusList", statusdata);
            setResult(200, tmp);
            finish();

        }else {
            Intent intent;
            intent = new Intent(getApplicationContext(), WriteWorkReportActivity.class);

            intent.putExtra("userID",savedID);
            intent.putParcelableArrayListExtra("UserListItem", data);
            intent.putExtra("StatusList",statusdata);
            intent.putExtra("userMode", savedMode);
            intent.putExtra("mode", "write");
            intent.putExtra("selectedDay", string_selected_day);

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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 3:     //work report registration complete
                setResult(3);
                finish();
                break;
            default:
                setResult(-1);
                finish();
                break;
        }
    }

}
