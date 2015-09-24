package com.jbrunton.rxandroidexamples;

import com.trello.rxlifecycle.components.RxFragment;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class BaseFragment extends RxFragment {
    private static Map<String, Observable> observableCache = new HashMap<>();

    <T> Observable<T> cache(Observable<T> observable, String id) {
        String key = this.getClass().getName() + "/" + id;

        if (!observableCache.containsKey(key)) {
            Subject<T, T> subject = PublishSubject.create();

            observable.subscribe(subject);

            observableCache.put(key, subject.asObservable());
        }

        return observableCache.get(key);
    }

    <T> Observable<T> fetch(String id) {
        return observableCache.get(this.getClass().getName() + "/" + id);
    }

    <T> void bind(Observable<T> observable, Action1<T> onNext, Action0 onCompleted) {
        if (observable != null) {
            observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(this.<T>bindToLifecycle())
                    .doOnCompleted(onCompleted)
                    .subscribe(onNext);
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();

        if (getActivity().isFinishing()) {
            invalidateCache();
        }
    }

    void invalidateCache() {
        String key = this.getClass().getName();
        for (String k : observableCache.keySet()) {
            if (k.startsWith(key + "/")) {
                observableCache.remove(k);
            }
        }
    }
}
