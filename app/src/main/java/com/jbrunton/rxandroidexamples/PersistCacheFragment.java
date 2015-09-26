package com.jbrunton.rxandroidexamples;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;

public class PersistCacheFragment extends TimerFragment {
    private static Observable<Long> cachedTimer;

    @Override public void onResume() {
        super.onResume();

        if (cachedTimer == null) {
            // Using a ReplaySubject with a capacity of 1 gives us a hot observable that always
            // emits the previous value, so that we don't have to store state across config changes
            // in the fragment.
            ReplaySubject<Long> subject = ReplaySubject.create(1);
            createTimer().subscribe(subject);
            cachedTimer = subject;
        }

        cachedTimer
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(onTick);
    }

    @Override public void onDestroy() {
        super.onDestroy();

        if (getActivity().isFinishing()) {
            cachedTimer = null;
        }
    }
}
