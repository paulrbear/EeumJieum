package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
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

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SelectMultiRoomUserActivity extends FragmentActivity {
    ArrayList<UserListItem> data = new ArrayList<>();
    UserListAdapter adapter;
    String savedID;
    int savedMode;
    String mode;
    ArrayList<String> objectlist = new ArrayList<>();
    ListView listView;
    int[] room_user_count;
    boolean all_checked=false;
    boolean room1_checked=false;
    boolean room2_checked= false;
    boolean room3_checked= false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_multi_room_user);

        Intent intent = getIntent();
//        savedID = intent.getExtras().getString("userID");
 //       savedMode = intent.getExtras().getInt("userMode");
        mode = intent.getExtras().getString("mode");

        listView = (ListView) findViewById(R.id.userlistview);

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

    public void listinint(){
        MyDBHandler handler = MyDBHandler.open(getApplicationContext());
        List<RoomUserItem> userlist;
        String roomname[] = {"기쁨방", "믿음방", "은혜방"};
        room_user_count = new int[3];

        for(int i =0; i<roomname.length; i++) {
            userlist = handler.getRoomUser(roomname[i]);
            room_user_count[i] = userlist.size();
            Iterator iterator = userlist.iterator();

            while (iterator.hasNext()) {
                RoomUserItem tmpuser = (RoomUserItem) iterator.next();
                UserListItem tmp = new UserListItem(tmpuser.getuImg(), tmpuser.getName(), roomname[i]);
                data.add(tmp);
            }
        }

        adapter = new UserListAdapter(this, R.layout.room_userlist_item, data);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(itemClickListenerOfUserList);
    }

    public void  onClick_room1btn(View v){
        if(room1_checked) {
            for(int i = 0 ; i < room_user_count[0] ; i++) {
                listView.setItemChecked(i, false);
            }
            room1_checked = false;

            Button tmpbtn = (Button) findViewById(R.id.room1);
            tmpbtn.setBackgroundColor(Color.TRANSPARENT);

        }else{
            for(int i =  0 ; i < room_user_count[0]; i++) {
                listView.setItemChecked(i, true);
            }
            room1_checked = true;

            Button tmpbtn = (Button) findViewById(R.id.room1);
            tmpbtn.setBackgroundResource(R.drawable.shape_oval_room);

        }

        int count = listView.getCheckedItemCount();
        TextView tmp = (TextView) findViewById(R.id.total_count_tfd);
        tmp.setText("" + count);
    }

    public void  onClick_room2btn(View v){
        if(room2_checked) {
            for(int i =  room_user_count[0] ; i < room_user_count[0] + room_user_count[1] ; i++) {
                listView.setItemChecked(i, false);
            }
            room2_checked = false;

            Button tmpbtn = (Button) findViewById(R.id.room2);
            tmpbtn.setBackgroundColor(Color.TRANSPARENT);

        }else{
            for(int i =  room_user_count[0] ; i < room_user_count[0] + room_user_count[1]; i++) {
                listView.setItemChecked(i, true);
            }
            room2_checked = true;

            Button tmpbtn = (Button) findViewById(R.id.room2);
            tmpbtn.setBackgroundResource(R.drawable.shape_oval_room);

        }

        int count = listView.getCheckedItemCount();
        TextView tmp = (TextView) findViewById(R.id.total_count_tfd);
        tmp.setText("" + count);
    }

    public void  onClick_room3btn(View v){
        Button tmpbtn = (Button) findViewById(R.id.room3);
        if(room3_checked) {
            for(int i =  room_user_count[0] + room_user_count[1]; i < room_user_count[0] + room_user_count[1] + room_user_count[2]; i++) {
                listView.setItemChecked(i, false);
            }
            room3_checked = false;
            tmpbtn.setBackgroundColor(Color.TRANSPARENT);

        }else{
            for(int i =  room_user_count[0] + room_user_count[1]; i < room_user_count[0] + room_user_count[1] + room_user_count[2]; i++) {
                listView.setItemChecked(i, true);
            }
            room3_checked = true;
            tmpbtn.setBackgroundResource(R.drawable.shape_oval_room);
        }

        int count = listView.getCheckedItemCount();
        TextView tmp = (TextView) findViewById(R.id.total_count_tfd);
        tmp.setText("" + count);

    }


    public void onClick_allbtn(View v){
        if(all_checked) {
            for (int i = 0; i < listView.getCount(); i++) {
                listView.setItemChecked(i, false);
            }

            all_checked = false;
            room1_checked = false;
            room2_checked = false;
            room3_checked = false;
            Button tmpbtn = (Button) findViewById(R.id.room1);
            tmpbtn.setBackgroundColor(Color.TRANSPARENT);
            tmpbtn = (Button) findViewById(R.id.room2);
            tmpbtn.setBackgroundColor(Color.TRANSPARENT);
            tmpbtn = (Button) findViewById(R.id.room3);
            tmpbtn.setBackgroundColor(Color.TRANSPARENT);

        }else{
            for (int i = 0; i < listView.getCount(); i++) {
                listView.setItemChecked(i, true);
            }
            all_checked = true;
            room1_checked = true;
            room2_checked = true;
            room3_checked = true;
            Button tmpbtn = (Button) findViewById(R.id.room1);
            tmpbtn.setBackgroundResource(R.drawable.shape_oval_room);
            tmpbtn = (Button) findViewById(R.id.room2);
            tmpbtn.setBackgroundResource(R.drawable.shape_oval_room);
            tmpbtn = (Button) findViewById(R.id.room3);
            tmpbtn.setBackgroundResource(R.drawable.shape_oval_room);
        }

        int count = listView.getCheckedItemCount();
        TextView tmp = (TextView) findViewById(R.id.total_count_tfd);
        tmp.setText("" + count);
    }

    public void onClick_donebtn(View v){
        SparseBooleanArray booleans = listView.getCheckedItemPositions();
        int count = listView.getCheckedItemCount();
        Toast.makeText(getApplicationContext(), "count " + count, Toast.LENGTH_SHORT).show();

        for(int i = 0; i < data.size(); i++){
            if(booleans.get(i)) {
                String tmp = data.get(i).getName();
                objectlist.add(tmp);
            }
        }


        Intent intent = new Intent();

        intent.putStringArrayListExtra("result", objectlist);
        setResult(2, intent);       //user object selected = 2
        finish();


    }

    public void onClick_backbtn(View v){
        finish();
    }

    private AdapterView.OnItemClickListener itemClickListenerOfUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int count = listView.getCheckedItemCount();
            TextView tmp = (TextView) findViewById(R.id.total_count_tfd);
            tmp.setText("" + count);
        }
    };


}
