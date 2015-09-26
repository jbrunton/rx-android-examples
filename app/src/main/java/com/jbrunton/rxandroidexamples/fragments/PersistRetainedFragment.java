package com.jbrunton.rxandroidexamples.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.jbrunton.rxandroidexamples.TimerFragment;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class PersistRetainedFragment extends TimerFragment {
    private Observable<Long> timer;

    @Override public void onResume() {
        super.onResume();

        Worker frag = (Worker) getActivity()
                .getFragmentManager()
                .findFragmentByTag("worker");

        if (frag == null) {
            frag = new Worker();
            getActivity().getFragmentManager().beginTransaction()
                    .add(frag, "worker")
                    .commit();
        }
    }

    public void setTimer(Observable<Long> timer) {
        timer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(onTick);
    }

    public static class Worker extends Fragment {
        private Subject<Long, Long> timerSubject;
        private Subscription subscription;
        private PersistRetainedFragment master;

        @Override public void onAttach(Activity activity) {
            super.onAttach(activity);
            master = (PersistRetainedFragment) activity.getFragmentManager().findFragmentByTag("container");
        }

        @Override public void onDetach() {
            super.onDetach();
            master = null;
        }

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            Observable<Long> timer = createTimer();
            timerSubject = PublishSubject.create();
            subscription = timer.subscribe(timerSubject);
        }

        @Override public void onResume() {
            super.onResume();
            master.setTimer(timerSubject.asObservable());
        }
    }
}
