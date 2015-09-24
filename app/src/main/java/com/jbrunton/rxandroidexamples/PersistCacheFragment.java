package com.jbrunton.rxandroidexamples;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class PersistCacheFragment extends TimerFragment {
    private static Observable<Long> cachedTimer;

    @Override public void onResume() {
        super.onResume();

        if (cachedTimer == null) {
            PublishSubject<Long> subject = PublishSubject.create();
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
