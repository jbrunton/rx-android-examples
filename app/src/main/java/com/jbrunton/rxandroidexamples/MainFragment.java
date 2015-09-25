package com.jbrunton.rxandroidexamples;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static rx.Observable.interval;

public class MainFragment extends BaseFragment {
    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.action_no_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(getActivity(), NaiveFragment.class);
            }
        });

        view.findViewById(R.id.action_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(getActivity(), UnsubscribeFragment.class);
            }
        });

        view.findViewById(R.id.action_persist_cache).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(getActivity(), PersistCacheFragment.class);
            }
        });

        view.findViewById(R.id.action_persist_fragment).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ContainerActivity.start(getActivity(), PersistRetainedFragment.class);
            }
        });

        return view;
    }
}
