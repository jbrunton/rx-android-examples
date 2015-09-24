package com.jbrunton.rxandroidexamples;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trello.rxlifecycle.components.RxFragment;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;

import static rx.Observable.interval;

public abstract class TimerFragment extends RxFragment {
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    protected Observable<Long> createTimer() {
        return interval(1, TimeUnit.SECONDS);
    }

    protected final Action1<Long> onTick = new Action1<Long>() {
        @Override public void call(Long count) {
            updateTimer(count + 1);
        }
    };

    private void updateTimer(Long count) {
        ((TextView) getView().findViewById(R.id.label)).setText(String.valueOf(count));
    }
}
