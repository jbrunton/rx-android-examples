package com.jbrunton.rxandroidexamples.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbrunton.rxandroidexamples.TimerFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class NaiveFragment extends TimerFragment {
    @Override public void onResume() {
        super.onResume();

        // We never unsubscribe from the observable. Thus, when onTick is invoked after a config
        // change (when the original fragment instance is null), we get an NPE trying to invoke
        // findViewById() on the null fragment.
        createTimer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onTick);
    }
}
