package com.way.capture.fragment;

import android.support.annotation.NonNull;

import com.way.capture.data.DataInfo;
import com.way.capture.utils.RxSchedulers;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;


/**
 * Created by android on 16-12-1.
 */

public class ScreenshotPresenter extends ScreenshotContract.Presenter {
    @NonNull
    private final CompositeDisposable mSubscriptions;
    private final ScreenshotContract.Model mModel;

    public ScreenshotPresenter(ScreenshotContract.View view) {
        mView = view;
        mSubscriptions = new CompositeDisposable();
        mModel = new ScreenshotModel();
    }

    @Override
    public void getData(int type) {
        mSubscriptions.clear();
        DisposableObserver<List<DataInfo>> observer = new DisposableObserver<List<DataInfo>>() {
            @Override
            public void onNext(List<DataInfo> data) {
                mView.onLoadFinished(data);
            }

            @Override
            public void onError(Throwable e) {
                mView.onError(e);
            }

            @Override
            public void onComplete() {

            }
        };
        mModel.getData(type).compose(RxSchedulers.<List<DataInfo>>io_main()).subscribe(observer);

        mSubscriptions.add(observer);
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }
}
