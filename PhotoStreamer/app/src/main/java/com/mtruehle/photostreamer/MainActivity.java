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
    private boolean currentlyInFeed; // toggle to keep from switching fragments unnecessarily.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sf = new SearchFragment();
        ff = new FeedFragment();
        fm = getSupportFragmentManager();
        t = fm.beginTransaction();
        t.replace(R.id.container, sf);
        currentlyInFeed = false;
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
                switchToFeed();
                break;
            case R.id.action_go_search:
                switchToSearch();
                break;
            default:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchToFeed() {
        if (!currentlyInFeed) {
            t = fm.beginTransaction();
            t.replace(R.id.container, ff);
            currentlyInFeed = true;
            t.commit();
        }
    }



    public void switchToSearch()  {
        if (currentlyInFeed) {
            t = fm.beginTransaction();
            t.replace(R.id.container, sf);
            currentlyInFeed = false;
            t.commit();
        }
    }
}
