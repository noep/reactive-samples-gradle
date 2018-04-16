package io.noep.reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Taehoon Yoo
 * User  : noep
 * Date  : 2018. 4. 17.
 * Time  : AM 12:19
 * Page  : http:noep.github.io
 * Email : noep@naver.com
 * Desc  :
 */
@SpringBootApplication
public class HelloApp {

    @RestController
    public static class Controller {
        @RequestMapping("/hello")
        public Publisher<String> hello(String name) {
            return new Publisher<String>() {
                @Override
                public void subscribe(Subscriber<? super String> s) {
                    s.onSubscribe(new Subscription() {
                        @Override
                        public void request(long n) {
                            s.onNext("Hello " + name);
                            s.onComplete();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
                }
            };
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloApp.class, args);
    }
}
