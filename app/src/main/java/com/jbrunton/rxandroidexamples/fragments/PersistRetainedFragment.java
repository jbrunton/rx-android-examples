package com.jbrunton.rxandroidexamples.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jbrunton.rxandroidexamples.ContainerActivity;
import com.jbrunton.rxandroidexamples.TimerFragment;

import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;

public class PersistRetainedFragment extends TimerFragment {
    private static final String WORKER_TAG = Worker.class.getName();

    @Override public void onResume() {
        super.onResume();

        Worker frag = (Worker) getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(WORKER_TAG);

        if (frag == null) {
            frag = new Worker();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(frag, WORKER_TAG)
                    .commit();
        }
    }

    public void subscribeTo(Observable<Long> timer) {
        Subscription subscription = timer.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Long>bindToLifecycle())
                .subscribe(onTick);
    }

    public static class Worker extends Fragment {
        private Observable<Long> cachedTimer;
        private PersistRetainedFragment master;

        @Override public void onAttach(Activity activity) {
            super.onAttach(activity);

            List<Fragment> frags = ((ContainerActivity) activity).getSupportFragmentManager().getFragments();
            for (Fragment f : frags) {
                if (f instanceof PersistRetainedFragment) {
                    master = (PersistRetainedFragment) f;
                }
            }
        }

        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);

            ReplaySubject<Long> subject = ReplaySubject.create(1);
            createTimer().subscribe(subject);
            cachedTimer = subject;
        }

        @Override public void onResume() {
            super.onResume();
            master.subscribeTo(cachedTimer);
        }
    }
}
