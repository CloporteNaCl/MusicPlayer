package com.centralesupelec.ipfs.ipfsplayer;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.Button;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private ViewFlipper mViewFlipper;
    private byte currentView;
    private Button btnPlayer;
    private ImageButton btnTest;

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

    final String[] listContent = {"Lorelei", "Fear of the Dark"};
    final int[] resID = {R.raw.lorelei, R.raw.fearofthedark};
    public static MediaPlayer mp;
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Initializing variables
        mainList = (ListView) findViewById(R.id.songs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listContent);
        mainList.setAdapter(adapter);

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
/*                if (mp != null){
                    mp.reset();
                    mp.release();
                }*/


                mp = MediaPlayer.create(getApplicationContext(), resID[i]);
                title = listContent[i];

                Intent in = new Intent(getApplicationContext(), AndroidBuildingMusicPlayerActivity.class);
                startActivityForResult(in, 100);

                int songIndex = 1;

                in.putExtra("songIndex", songIndex);
                setResult(100, in);

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


        btnTest = (ImageButton) findViewById(R.id.btn_test);

        btnTest.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), PlayListManagerActivity.class);
                startActivityForResult(in, 100);

            }

        });



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
