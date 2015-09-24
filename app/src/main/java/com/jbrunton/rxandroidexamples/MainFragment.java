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
    private TextView displayCount;
    private static final String retainObservableId = "retain";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind(this.<Long>fetch(retainObservableId), onTick, onCompleted);
    }

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

        displayCount = (TextView) view.findViewById(R.id.display_count);

        return view;
    }

    private void countNoUnsubscribe() {
        displayCount.setText("Counting...");
        createTimer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(onCompleted)
                .subscribe(onTick);
    }

    private void countUnsubscribe() {
        displayCount.setText("Counting...");
        bind(createTimer(), onTick, onCompleted);
    }

    private void countRetain() {
        displayCount.setText("Counting...");
        bind(cache(createTimer(), retainObservableId), onTick, onCompleted);
    }

    private Observable<Long> createTimer() {
        return interval(1, TimeUnit.SECONDS)
                .take(20);
    }

    private final Action1<Long> onTick = new Action1<Long>() {
        @Override public void call(Long x) {
            displayCount.setText("Count: " + x);
        }
    };

    private final Action0 onCompleted = new Action0() {
        @Override public void call() {
            displayCount.setText(getString(R.string.select_button));
        }
    };
}
