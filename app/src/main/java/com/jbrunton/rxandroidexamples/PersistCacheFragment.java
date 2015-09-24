package com.jbrunton.rxandroidexamples;

import android.widget.Toast;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

public class PersistCacheFragment extends TimerFragment {
    private static Observable<Long> cachedObservable;

    @Override public void onResume() {
        super.onResume();

        if (cachedObservable == null) {
            PublishSubject<Long> subject = PublishSubject.create();
            createTimer().subscribe(subject);
            cachedObservable = subject;
        }

        cachedObservable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(onTick);
    }

    @Override public void onDestroy() {
        super.onDestroy();

        if (getActivity().isFinishing()) {
            cachedObservable = null;
        }
    }
}
