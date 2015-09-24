package com.jbrunton.rxandroidexamples;

import android.widget.Toast;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UnsubscribeFragment extends TimerFragment {
    @Override public void onResume() {
        super.onResume();

        createTimer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(onTick);
    }
}
