# Reactive, FRP
# 리액티브가 뭐야?
Reactive Programming
예전부터 있었던 것 아니냐
FRP (Functional Reactive Programming)
RFP (Reactive Functional Programming)
- 외부의 이벤트가 발생했을 때 거기에 대응하는 방법으로 코드를 작성하는 것


## Duality 쌍대성
## Observer Pattern
## Reactive Streams - 표준! - Java9에는 JDK에 api로 들어간다


이터레이터

- 옵저버 패턴
    - 옵저버 패턴이 주는 장점이 많다
    - 여러개의 옵저버가 한 이벤트를 수신하는 것도 가능
    - 멀티 스레드 상황에서도 옵저버 패턴을 이용하면 손쉽게 코딩이 가능

    - Reactive를 만든 마소의 엔지니어들은 gof의 Observer 패턴도 문제가 있다고 이야기
    - 1. Complete , 데이터를 다 던진 '끝' 이란 개념이 없다
    - 2. Error 에러를 표준으로 처리하는 에러 처리에 대한 내용이 패턴에 녹아있지 않음

- 확장된 옵저버 패턴

- www.reactive-streams.org
- reactivex.io



