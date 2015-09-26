package com.jbrunton.rxandroidexamples.fragments;

import android.widget.Toast;

import com.jbrunton.rxandroidexamples.TimerFragment;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UnsubscribeFragment extends TimerFragment {
    @Override public void onResume() {
        super.onResume();

        // Use the RxLifecycle bindToLifecycle() method to ensure we unsubscribe during onPause().
        // We don't persist the observable across config changes - but this approach may be
        // suitable for idempotent, cacheable web requests.
        createTimer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(onTick);
    }
}
