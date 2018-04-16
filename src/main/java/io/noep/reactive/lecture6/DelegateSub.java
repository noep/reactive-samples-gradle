package io.noep.reactive.lecture6;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by Taehoon Yoo
 * User  : noep
 * Date  : 2018. 4. 16.
 * Time  : PM 11:26
 * Page  : http:noep.github.io
 * Email : noep@naver.com
 * Desc  :
 */
public class DelegateSub<T, R> implements Subscriber<T> {

    Subscriber sub;

    public DelegateSub(Subscriber<? super R> sub) {
        this.sub = sub;

    }

    @Override
    public void onSubscribe(Subscription s) {
        sub.onSubscribe(s);
    }

    @Override
    public void onNext(T i) {
        sub.onNext(i);
    }

    @Override
    public void onError(Throwable t) {
        sub.onError(t);
    }

    @Override
    public void onComplete() {
        sub.onComplete();
    }
}
