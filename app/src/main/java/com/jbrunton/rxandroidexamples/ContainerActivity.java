package com.jbrunton.rxandroidexamples;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jbrunton.rxandroidexamples.fragments.NaiveFragment;
import com.jbrunton.rxandroidexamples.fragments.PersistCacheFragment;
import com.jbrunton.rxandroidexamples.fragments.PersistRetainedFragment;
import com.jbrunton.rxandroidexamples.fragments.UnsubscribeFragment;

import java.util.HashMap;
import java.util.Map;

public class ContainerActivity extends AppCompatActivity {
    private final static String EXTRA_KEY = "FragmentType";
    private final static Map<String, Class<? extends Fragment>> FRAGMENT_TYPES = new HashMap<String, Class<? extends Fragment>>(){{
        put(NaiveFragment.class.getName(), NaiveFragment.class);
        put(UnsubscribeFragment.class.getName(), UnsubscribeFragment.class);
        put(PersistCacheFragment.class.getName(), PersistCacheFragment.class);
        put(PersistRetainedFragment.class.getName(), PersistRetainedFragment.class);
    }};

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        try {
            Class<? extends Fragment> fragmentClass = FRAGMENT_TYPES.get(getIntent().getStringExtra(EXTRA_KEY));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragmentClass.newInstance())
                    .commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static <T extends Fragment> void start(Context context, Class<T> fragmentClass) {
        Intent intent = new Intent(context, ContainerActivity.class)
                .putExtra(EXTRA_KEY, fragmentClass.getName());
        context.startActivity(intent);
    }
}
