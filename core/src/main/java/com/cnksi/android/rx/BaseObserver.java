
package com.cnksi.android.rx;


import com.cnksi.android.crash.SpiderMan;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.Functions;
import io.reactivex.observers.LambdaConsumerIntrospection;
import io.reactivex.plugins.RxJavaPlugins;

public final class BaseObserver<T> extends AtomicReference<Disposable>
        implements Observer<T>, Disposable, LambdaConsumerIntrospection {

    private static final long serialVersionUID = -7251123623727029452L;
    Consumer<? super T> onNext;
    Consumer<? super Throwable> onError;
    Action onComplete;
    Consumer<? super Disposable> onSubscribe;

    public BaseObserver(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                        Action onComplete,
                        Consumer<? super Disposable> onSubscribe, CompositeDisposable compositeDisposable) {
        super();
        this.onNext = onNext;
        this.onError = onError;
        this.onComplete = onComplete;
        this.onSubscribe = onSubscribe;
        if (compositeDisposable != null) {
            compositeDisposable.add(this);
        }
    }

    public BaseObserver(Consumer<? super T> onNext, CompositeDisposable compositeDisposable) {
        this(onNext,  SpiderMan::writeError, Functions.EMPTY_ACTION, Functions.emptyConsumer(), compositeDisposable);
    }

    public BaseObserver(Consumer<? super T> onNext) {
        this(onNext,  SpiderMan::writeError, Functions.EMPTY_ACTION, Functions.emptyConsumer(), null);
    }

    public BaseObserver(Consumer<? super T> onNext, Consumer<? super Throwable> onError, CompositeDisposable compositeDisposable) {
        this(onNext, onError, Functions.EMPTY_ACTION, Functions.emptyConsumer(), compositeDisposable);
    }

    public BaseObserver(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, CompositeDisposable compositeDisposable) {
        this(onNext, onError, onComplete, Functions.emptyConsumer(), compositeDisposable);
    }

    @Override
    public void onSubscribe(Disposable s) {
        if (DisposableHelper.setOnce(this, s)) {
            try {
                onSubscribe.accept(this);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                s.dispose();
                onError(ex);
            }
        }
    }

    @Override
    public void onNext(T t) {
        if (!isDisposed()) {
            try {
                onNext.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                get().dispose();
                onError(e);
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                onError.accept(t);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(new CompositeException(t, e));
            }
        }
    }

    @Override
    public void onComplete() {
        if (!isDisposed()) {
            lazySet(DisposableHelper.DISPOSED);
            try {
                onComplete.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
    }

    @Override
    public void dispose() {
        DisposableHelper.dispose(this);
    }

    @Override
    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    @Override
    public boolean hasCustomOnError() {
        return onError != Functions.ON_ERROR_MISSING;
    }
}
