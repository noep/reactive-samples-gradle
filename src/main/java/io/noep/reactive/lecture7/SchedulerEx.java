package io.noep.reactive.lecture7;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class SchedulerEx {

    // Reactive Streams
    public static void main(String[] args) {

        Publisher<Integer> pub = sub -> {
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {

                    log.debug("reuqest");
                    sub.onNext(1);
                    sub.onNext(2);
                    sub.onNext(3);
                    sub.onNext(4);
                    sub.onNext(5);
                    sub.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };

        // subscribe on
        Publisher<Integer> subOnPub = sub -> {
            ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                @Override
                public String getThreadNamePrefix() {
                    return "subOn-";
                }
            });

            es.execute(() -> {
                pub.subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        sub.onSubscribe(s);

                    }

                    @Override
                    public void onNext(Integer integer) {
                        sub.onNext(integer);

                    }

                    @Override
                    public void onError(Throwable t) {
                        sub.onError(t);
                        es.shutdown();
                    }

                    @Override
                    public void onComplete() {
                        sub.onComplete();
                        es.shutdown();
                    }
                });
            });
        };

        // publish on
        // 소비는  빠른데 생성이 느린 경우
        Publisher<Integer> pubOnPub = sub -> {

            subOnPub.subscribe(new Subscriber<Integer>() {
                ExecutorService es = Executors.newSingleThreadExecutor(new CustomizableThreadFactory() {
                    @Override
                    public String getThreadNamePrefix() {
                        return "pubOn-";
                    }
                });

                @Override
                public void onSubscribe(Subscription s) {
                    sub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    es.execute(() -> sub.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    es.execute(() -> onError(t));
                    es.shutdown();
                }

                @Override
                public void onComplete() {
                    es.execute(() -> sub.onComplete());
                    es.shutdown();
                }
            });

        };


        // sub
        pubOnPub.subscribe(new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.debug("onNext {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        });

        System.out.println("exit");
    }
}
