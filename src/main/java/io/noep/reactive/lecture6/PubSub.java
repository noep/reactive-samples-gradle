package io.noep.reactive.lecture6;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Taehoon Yoo
 * User  : noep
 * Date  : 2018. 4. 16.
 * Time  : PM 10:49
 * Page  : http:noep.github.io
 * Email : noep@naver.com
 * Desc  :
 */
@Slf4j
public class PubSub {
    public static void main(String[] args) {
        Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));

        Publisher<Integer> mapPub = mapPub(pub, s -> s * 10);
        Publisher<String> mapPub2 = mapPub(pub, s -> "[" + s + "]");
//        Publisher<Integer> sumPub = sumPub(pub);
        // a는 init과 같은 타입이어야 함
        Publisher<StringBuilder> reducePub = reducePub(pub, new StringBuilder(), (a, b) -> a.append(b).append(","));
//        reducePub.subscribe(logSub());



//        mapPub.subscribe(logSub());
//        mapPub2.subscribe(logSub());
        reducePub.subscribe(logSub());
    }


    private static <T, R> Publisher<R> reducePub(Publisher<T> pub, R init, BiFunction<R, T, R> bf) {
        return sub -> pub.subscribe(new DelegateSub<T, R>(sub) {
            R result = init;

            @Override
            public void onNext(T integer) {
                result = bf.apply(result, integer);
            }

            @Override
            public void onComplete() {
                sub.onNext(result);
                sub.onComplete();
            }
        });
    }

    private static Publisher<Integer> sumPub(Publisher<Integer> pub) {
        return sub -> pub.subscribe(new DelegateSub<Integer, Integer>(sub) {
            int sum = 0;

            @Override
            public void onNext(Integer integer) {
                sum += integer;
            }

            @Override
            public void onComplete() {
                /**
                 * onComplete에도 onNext를 호출할 수 있다
                 */
                sub.onNext(sum);
                sub.onComplete();
            }
        });
    }

    /**
     * 이거에 비해 쉽게 구현된 게 Operator다
     */
    // T -> R
    private static <T, R> Publisher<R> mapPub(Publisher<T> pub, Function<T, R> f) {
        return sub -> {
            //pub.subscribe(sub); //이렇게 하면 기존이랑 똑같이 동작
            pub.subscribe(new DelegateSub<T, R>(sub) {
                @Override
                public void onNext(T i) {
                    sub.onNext(f.apply(i));
                }
            });

        };
    }

    private static <T> Subscriber<T> logSub() {
        return new Subscriber<T>() {
            // 정상 (최초)
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe");
                s.request(Long.MAX_VALUE);

            }

            // 정상 (계속)
            @Override
            public void onNext(T i) {
                log.debug("onNext : {}", i);
            }

            // 에러
            @Override
            public void onError(Throwable t) {
                log.debug("onError : {}", t);
            }

            // 완료
            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        };
    }

    private static Publisher<Integer> iterPub(List<Integer> iter) {
        // Publisher 데이터 발생
        return sub -> {
            // Subscriber 데이터 받는놈
            sub.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    try {
                        iter.forEach(sub::onNext);
                        sub.onComplete();
                    } catch (Throwable t) {
                        sub.onError(t);
                    }

                }

                @Override
                public void cancel() {

                }
            });
        };
    }
}
