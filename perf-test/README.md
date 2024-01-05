# 성능테스트

## 소개

- Redis 를 활용한 WebFlux 와 Undertow 의 기본 성능 테스트

## 구성
- JDK21, SpringBoot 3.2.1
- M1 MacBook Pro 14 32G
- JVM HeapSize 128M, ActiveProcessorCount 를 2개로 제약
- wrk 명령어를 사용하여 부하 인입
- VisualVM 을 통한 모니터링
- Redis 에서 key 3개를 조회해 보는 기능 테스트

```shell
# thread: 6
# connection: 500
# duration: 5 minutes

# undertow
wrk -t 6 -c 500 -d 300s http://localhost:19001/undertow

# webflux
wrk -t 6 -c 500 -d 300s http://localhost:19002/webflux
```

## 결과

### Undertow

```shell
Running 5m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    22.17ms    4.11ms 180.85ms   88.29%
    Req/Sec     3.77k   462.25     4.78k    75.08%
  6752558 requests in 5.00m, 1.18GB read
  Socket errors: connect 0, read 726, write 0, timeout 0
Requests/sec:  22504.78
Transfer/sec:      4.03MB
```

![undertow_visualvm](./undertow_visualvm.png)

### Webflux

```shell
Running 5m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    12.90ms    6.15ms 272.27ms   78.74%
    Req/Sec     6.55k   754.23    10.96k    81.65%
  11738155 requests in 5.00m, 1.18GB read
  Socket errors: connect 0, read 1611, write 28, timeout 0
Requests/sec:  39119.18
Transfer/sec:      4.03MB
```

![webflux_visualvm](./webflux_visualvm.png)
