package io.noep.reactive.lecture5;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Taehoon Yoo
 * User  : noep
 * Date  : 2018. 4. 15.
 * Time  : PM 11:04
 * Page  : http:noep.github.io
 * Email : noep@naver.com
 * Desc  :
 */
public class Ob {

    /**
     * 기능은 똑같은데 반대 방향으로 구현한다??
     */
    // Iterable <---> Observable 쌍대성 (duality)
    // Pull           Push


    /*
    Iterable<Integer> iter = () ->
            new Iterator<Integer>() {
                int i = 0;
                final static int MAX = 10;

                public boolean hasNext() {
                    return i < MAX;
                }

                public Integer next() {
                    return ++i;
                }
            };

    for (Integer i : iter) { // for-each
        System.out.println(i);
    }

    // java5 이전에 쓰던 세련된 방법
    for (Iterator<Integer> it = iter.iterator(); it.hasNext(); ) {
        System.out.println(it.next());
    }
    */

    static class IntObservable extends Observable implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <=10; i++) {
                setChanged();
                notifyObservers(i);                 // push
                //int i = it.next(); 이거에 대응     //pull

                // Data method() <-> void method(Data)

            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        // Observable
        // 와 자바안에 이런게 있었다니
        // Source로 생각하면 된다, Event Source
        //Source -> Event/Data -> Observer

        // Observer 받는 애
        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(Thread.currentThread().getName() + " " + arg);
            }
        };

        IntObservable io = new IntObservable();
        io.addObserver(ob);

        //io.run(); // 얘도 비동기적으로 실행도 된다 // 아래처럼
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(io);

        System.out.println(Thread.currentThread().getName() + " EXIT");
        es.shutdown();

        // 옵저버 패턴이 주는 장점이 많다
        // 여러개의 옵저버가 한 이벤트를 수신하는 것도 가능
        // 멀티 스레드 상황에서도 옵저버 패턴을 이용하면 손쉽게 코딩이 가능

        // Reactive를 만든 마소의 엔지니어들은 gof의 Observer 패턴도 문제가 있다고 이야기
        // 1. Complete , 데이터를 다 던진 '끝' 이란 개념이 없다
        // 2. Error 에러를 표준으로 처리하는 에러 처리에 대한 내용이 패턴에 녹아있지 않음

        // -> 확장된 옵저버 패턴
    }
}
