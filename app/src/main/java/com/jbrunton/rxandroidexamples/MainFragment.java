package com.jbrunton.rxandroidexamples;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle.components.RxFragment;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static rx.Observable.interval;

public class MainFragment extends RxFragment {
    private TextView displayCount;
    private static String TAG = WorkerFragment.class.getName();

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        view.findViewById(R.id.action_no_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                countNoUnsubscribe();
            }
        });

        view.findViewById(R.id.action_unsubscribe).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                countUnsubscribe();
            }
        });

        view.findViewById(R.id.action_retain).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                countRetain();
            }
        });

        displayCount = (TextView) view.findViewById(R.id.display_count);

        countRetain();

        return view;
    }

    private void countNoUnsubscribe() {
        displayCount.setText("Counting...");
        interval(1, TimeUnit.SECONDS)
                .take(10)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnCompleted(new Action0() {
                    @Override public void call() {
                        displayCount.setText(getString(R.string.select_button));
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long x) {
                        displayCount.setText("Count: " + x);
                    }
                });
    }

    private void countUnsubscribe() {
        displayCount.setText("Counting...");
        interval(1, TimeUnit.SECONDS)
                .take(10)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .doOnCompleted(new Action0() {
                    @Override public void call() {
                        displayCount.setText(getString(R.string.select_button));
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long x) {
                        displayCount.setText("Count: " + x);
                    }
                });
    }

    private void countRetain() {
        getObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .doOnCompleted(new Action0() {
                    @Override public void call() {
                        displayCount.setText(getString(R.string.select_button));
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long x) {
                        displayCount.setText("Count: " + x);
                    }
                });
    }

    private Observable<Long> getObservable() {
        FragmentManager fm = getActivity().getFragmentManager();
        WorkerFragment frag = (WorkerFragment) fm.findFragmentByTag(TAG);
        if (frag == null) {
            frag = new WorkerFragment();
            fm.beginTransaction().add(frag, TAG).commit();
        }
        return frag.getObservable();
    }
}
