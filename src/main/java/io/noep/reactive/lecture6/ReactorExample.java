package io.noep.reactive.lecture6;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Created by Taehoon Yoo
 * User  : noep
 * Date  : 2018. 4. 17.
 * Time  : AM 12:05
 * Page  : http:noep.github.io
 * Email : noep@naver.com
 * Desc  :
 */
public class ReactorExample {
    public static void main(String[] args) {
        Flux.<Integer>create(e -> {
            e.next(1);
            e.next(2);
            e.next(3);
            e.complete();
        })
        .log()
        .map(s -> s * 10)
        .reduce(0, (a,b) -> a + b)
        .log()
        .subscribe(System.out::println);
    }
}
