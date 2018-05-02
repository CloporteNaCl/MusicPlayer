package com.centralesupelec.ipfs.ipfsplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Button;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ViewFlipper mViewFlipper;
    private byte currentView;
    private Button btnPlayer;
    private ArrayList<HashMap<String, String>> playlist;

    private EditText etInput;
    private ImageButton btnAdd;
    private ListView lvItem;
    private ArrayList<String> itemArrey;
    private ArrayAdapter<String> itemAdapter;
    public static String title2;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_decouvrir:
                    mTextMessage.setText(R.string.title_decouvrir);
                    return true;
                case R.id.navigation_rechercher:
                    mTextMessage.setText(R.string.title_rechercher);
                    return true;
                case R.id.navigation_playlists:
                    mTextMessage.setText(R.string.title_playlists);
                    return true;
                case R.id.navigation_parametres:
                    mTextMessage.setText(R.string.title_parametres);
                    return true;
            }
            return false;
        }
    };


    ListView mainList;

    private String[] listContent;
    public static MediaPlayer mp;
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initializing variables

        SongsManager sm = new SongsManager();
        playlist = sm.getPlayList();
        listContent = new String[playlist.size()];
        for(int i = 0; i < playlist.size(); i++) {
            listContent[i] = playlist.get(i).get("songTitle");
        }
        mainList = (ListView) findViewById(R.id.songs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContent);
        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
/*                if (mp != null){
                    mp.reset();
                    mp.release();
                }*/

                mp = MediaPlayer.create(getApplicationContext(), Uri.parse(playlist.get(i).get("songPath")));
                title = listContent[i];

                Intent playSongIntent = new Intent(getApplicationContext(), AndroidBuildingMusicPlayerActivity.class);
                playSongIntent.putExtra("songIndex", i);
                startActivityForResult(playSongIntent, 100);

            }
        });

        mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);

        /*mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);*/


        btnPlayer = (Button) findViewById(R.id.btnPlayer);

        btnPlayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(), AndroidBuildingMusicPlayerActivity.class);
                startActivityForResult(i, 100);
            }
        });

        setUpView();
    }

    private void setUpView() {
        etInput = (EditText) findViewById(R.id.editText_input);
        btnAdd = (ImageButton) findViewById(R.id.btn_addPlaylist);
        lvItem = (ListView) findViewById(R.id.playlists_list);

        itemArrey = new ArrayList<String>();
        itemArrey.clear();

        itemAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemArrey);
        lvItem.setAdapter(itemAdapter);


        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                title2 = lvItem.getItemAtPosition(position).toString();
                Intent in = new Intent(getApplicationContext(), Songslist.class);
                startActivityForResult(in, 100);
            }

        });






        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addItemList();
            }
        });

        etInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    addItemList();
                }
                return true;
            }
        });
    }

    protected void addItemList() {
        if (isInputValid(etInput)) {
            itemArrey.add(0,etInput.getText().toString());
            etInput.setText("");
            itemAdapter.notifyDataSetChanged();
        }
    }

    protected boolean isInputValid(EditText etInput2) {
        if (etInput2.getText().toString().trim().length()<1) {
            etInput2.setError("Please Enter Item");
            return false;
        } else {
            return true;
        }
    }

    public void onNavBarClicked(View view) {
        String tag = (String) view.getTag();

        switch(tag) {
            case "discover":
                mViewFlipper.setDisplayedChild(0);
                currentView = 0;
                break;
            case "search":
                mViewFlipper.setDisplayedChild(1);
                currentView = 1;
                break;
            case "playlists":
                mViewFlipper.setDisplayedChild(2);
                currentView = 2;
                break;
            case "settings":
                mViewFlipper.setDisplayedChild(3);
                currentView = 3;
                break;
            default:
                // Stay on same view
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();


    }

}