package io.noep.reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Taehoon Yoo
 * User  : noep
 * Date  : 2018. 4. 15.
 * Time  : PM 11:54
 * Page  : http:noep.github.io
 * Email : noep@naver.com
 * Desc  :
 */
public class PubSub {
    // 메인은 psvm
    public static void main(String[] args) throws InterruptedException {
        // Publisher  <- Observable

        // Subscriber <- Observer

        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);
        // java 9에도 있음, reactiveStreams에도 있음
        // 사실 두개 똑같다

        ExecutorService es = Executors.newCachedThreadPool();

        Publisher p = new Publisher() { // Publisher는 데이터를 주는 쪽
            @Override
            public void subscribe(Subscriber subscriber) {

                Iterator<Integer> it = itr.iterator();


                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        es.submit(() -> {
                            int i = 0;
                            try {
                                while (i++ < n) {
                                    if (it.hasNext()) {
                                        subscriber.onNext(it.next());
                                    } else {
                                        subscriber.onComplete();
                                        break;
                                    }
                                }
                            } catch (RuntimeException e) {
                                subscriber.onError(e);
                            }
                        });
                    }
                    @Override
                    public void cancel() {
                    }
                });

            }
        };

        Subscriber<Integer> s = new Subscriber<Integer>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                this.subscription = subscription;
                this.subscription.request(Long.MAX_VALUE);
            }

            // 버퍼 사이즈를 만들어 두고 능력껏 호출

            int bufferSize = 2;

            @Override
            public void onNext(Integer item) {
                System.out.println("onNext " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError" + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        p.subscribe(s);

        // publisher와 subscriber는 sequential하게 데이터가 날라오는것으로 예상
        // onNext가 멀티스레드로 도는건 매우 복잡했다고

        es.awaitTermination(10, TimeUnit.SECONDS);
        es.shutdown();
    }
}
