package dev.erica.hyunji.eeumjieum;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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


        for(int i =0; i<roomname.length; i++) {
            userlist = handler.getRoomUser(roomname[i]);
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
