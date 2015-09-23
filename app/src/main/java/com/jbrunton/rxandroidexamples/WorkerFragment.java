package com.jbrunton.rxandroidexamples;

import android.app.Fragment;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import static rx.Observable.interval;

public class WorkerFragment extends Fragment {
    private Subject<Long, Long> subject = PublishSubject.create();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        interval(1, TimeUnit.SECONDS)
                .take(10)
                .subscribe(subject);
    }

    public Observable<Long> getObservable() {
        return subject.asObservable();
    }
}
