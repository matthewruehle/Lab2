package com.mtruehle.photostreamer;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private SearchFragment sf;
    private FeedFragment ff;
    private FragmentManager fm;
    private FragmentTransaction t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf = new SearchFragment();
        ff = new FeedFragment();
        fm = getSupportFragmentManager();
        t = fm.beginTransaction();
        t.replace(R.id.container, sf);
        t.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_go_feed:
//                fm = getSupportFragmentManager();
                ff = new FeedFragment();
//                ff.updateSavedUrls();
                t = fm.beginTransaction();
                t.replace(R.id.container, ff);
                t.commit();
                break;
            case R.id.action_go_search:
//                fm = getSupportFragmentManager();
                t = fm.beginTransaction();
                t.replace(R.id.container, sf);
                t.commit();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
