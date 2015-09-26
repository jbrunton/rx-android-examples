package com.jbrunton.rxandroidexamples;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jbrunton.rxandroidexamples.fragments.NaiveFragment;
import com.jbrunton.rxandroidexamples.fragments.PersistCacheFragment;
import com.jbrunton.rxandroidexamples.fragments.PersistRetainedFragment;
import com.jbrunton.rxandroidexamples.fragments.UnsubscribeFragment;

import static rx.Observable.interval;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.action_no_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(MainActivity.this, NaiveFragment.class);
            }
        });

        findViewById(R.id.action_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(MainActivity.this, UnsubscribeFragment.class);
            }
        });

        findViewById(R.id.action_persist_cache).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(MainActivity.this, PersistCacheFragment.class);
            }
        });

        findViewById(R.id.action_persist_fragment).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(MainActivity.this, PersistRetainedFragment.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
