package io.noep.reactive.lecture7;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEx {

    public static void main(String[] args) throws InterruptedException {

//        Flux.range(1, 10)
//                .publishOn(Schedulers.newSingle("pub"))
//                .log()
//                .subscribeOn(Schedulers.newSingle("sub"))
//                .subscribe(System.out::print);
//        System.out.println("exit");


        /*
         * user Thread 없이 daemon Thread만 있으면 jvm이 강종
         */
//        Flux.interval(Duration.ofMillis(500)) //Daemon Thread
//                .subscribe(s -> {
//                    log.debug("onNext:{}", s);
//                });
//        log.debug("exit");
//        TimeUnit.SECONDS.sleep(5);


//        Executors.newSingleThreadExecutor().execute(() -> { // User Thread
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//            }
//            System.out.println("Hello");
//
//        });
//        System.out.println("exit");


        Flux.interval(Duration.ofMillis(200)) //Daemon Thread
                .take(10)
                .subscribe(s -> {
                    log.debug("onNext:{}", s);
                });
        log.debug("exit");
        TimeUnit.SECONDS.sleep(5);
    }
}
