# 성능테스트

## 소개

- Redis 를 활용한 Spring WebFlux 와 Undertow 의 기본 성능 테스트
- 추가로 Quarkus 및 Ktor 테스트

## 구성

- JDK21
- SpringBoot 3.2.1
- wrk 명령어를 사용하여 부하 인입
- Redis 에서 key 3개를 조회해 보는 기능 테스트

## 테스트 PC 사양

- 11th Gen Intel(R) Core(TM) i7-11700F @ 2.50GHz
- Memory: 32GB
- Docker Container 설정: cpus=2, memory=512m
- JVM Heap: 256m

## 결과

### Undertow (cpus=2, memory=512m, heap=256m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    28.20ms   25.71ms  84.09ms   82.51%
    Req/Sec     3.70k   258.91     5.01k    75.67%
  1326591 requests in 1.00m, 237.85MB read
Requests/sec:  22084.04
Transfer/sec:      3.96MB

# 500 connections #2
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    27.97ms   25.60ms  80.33ms   82.09%
    Req/Sec     3.76k   149.15     4.97k    73.97%
  1348024 requests in 1.00m, 241.69MB read
Requests/sec:  22455.20
Transfer/sec:      4.03MB

# 500 connections #3
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    27.92ms   25.69ms  80.49ms   81.64%
    Req/Sec     3.79k   270.76    15.16k    93.97%
  1358260 requests in 1.00m, 243.52MB read
Requests/sec:  22600.20
Transfer/sec:      4.05MB

# 1000 connections #1
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.40ms   33.38ms  87.61ms   55.80%
    Req/Sec     3.75k   192.89     7.07k    72.89%
  1345136 requests in 1.00m, 241.17MB read
Requests/sec:  22382.73
Transfer/sec:      4.01MB

# 1000 connections #2
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.81ms   33.47ms  93.12ms   55.60%
    Req/Sec     3.72k   189.98     6.42k    76.06%
  1332982 requests in 1.00m, 238.99MB read
Requests/sec:  22183.17
Transfer/sec:      3.98MB

# 1000 connection #3
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.62ms   33.43ms  88.38ms   55.81%
    Req/Sec     3.73k   217.17     5.42k    65.39%
  1337991 requests in 1.00m, 239.89MB read
Requests/sec:  22264.66
Transfer/sec:      3.99MB
```

### Undertow (cpus=4, memory=1024m, heap=512m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    17.46ms   18.28ms 167.84ms   80.55%
    Req/Sec     7.12k   586.57     8.61k    76.61%
  2549689 requests in 1.00m, 457.14MB read
Requests/sec:  42476.29
Transfer/sec:      7.62MB

# 500 connections #2
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    17.29ms   17.93ms  75.20ms   79.31%
    Req/Sec     7.21k   312.18    12.35k    98.17%
  2585765 requests in 1.00m, 463.60MB read
Requests/sec:  43042.90
Transfer/sec:      7.72MB

# 500 connections #3
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    17.39ms   18.06ms  94.76ms   79.40%
    Req/Sec     7.18k   711.37    10.23k    60.97%
  2572175 requests in 1.00m, 461.17MB read
Requests/sec:  42849.84
Transfer/sec:      7.68MB

# 1000 connections #1
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    26.73ms   21.30ms 160.59ms   80.61%
    Req/Sec     7.09k   737.63    10.47k    59.81%
  2538537 requests in 1.00m, 455.14MB read
Requests/sec:  42247.26
Transfer/sec:      7.57MB

# 1000 connections #2
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    26.57ms   21.13ms  82.99ms   80.76%
    Req/Sec     7.12k   267.79     9.83k    93.75%
  2550374 requests in 1.00m, 457.26MB read
Requests/sec:  42461.73
Transfer/sec:      7.61MB

# 1000 connections #3
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    26.64ms   21.20ms  86.58ms   80.73%
    Req/Sec     7.10k   349.83    13.12k    98.64%
  2545815 requests in 1.00m, 456.44MB read
Requests/sec:  42366.63
Transfer/sec:      7.60MB

```

### Undertow (cpus=8, memory=1024m, heap=512m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.38ms    4.68ms  88.32ms   88.26%
    Req/Sec    14.62k     1.26k   16.73k    91.58%
  5235701 requests in 1.00m, 0.92GB read
Requests/sec:  87192.70
Transfer/sec:     15.63MB

# 500 connections #2
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.79ms    7.26ms 149.31ms   91.79%
    Req/Sec    14.42k     1.46k   17.08k    87.64%
  5166853 requests in 1.00m, 0.90GB read
Requests/sec:  86065.10
Transfer/sec:     15.43MB

# 500 connections #3
Running 1m test @ http://localhost:19001/undertow
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     6.62ms    5.68ms 116.84ms   89.47%
    Req/Sec    14.40k     1.37k   17.23k    91.83%
  5156612 requests in 1.00m, 0.90GB read
Requests/sec:  85855.26
Transfer/sec:     15.39MB

# 1000 connections #1
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    12.31ms    7.27ms 140.67ms   87.61%
    Req/Sec    14.19k     1.50k   15.49k    92.72%
  5082713 requests in 1.00m, 0.89GB read
Requests/sec:  84616.35
Transfer/sec:     15.17MB

# 1000 connections #2
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    12.21ms    6.35ms 117.69ms   86.58%
    Req/Sec    14.13k     1.31k   15.21k    94.31%
  5061979 requests in 1.00m, 0.89GB read
Requests/sec:  84255.24
Transfer/sec:     15.11MB

# 1000 connections #3
Running 1m test @ http://localhost:19001/undertow
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    12.38ms    6.51ms 122.44ms   87.26%
    Req/Sec    13.95k     1.40k   17.05k    87.00%
  4999489 requests in 1.00m, 0.88GB read
Requests/sec:  83216.48
Transfer/sec:     14.92MB

```

### Webflux (cpus=2, memory=512m, heap=256m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    30.33ms   29.16ms 148.76ms   65.03%
    Req/Sec     3.54k   291.54     5.43k    80.81%
  1269668 requests in 1.00m, 130.77MB read
Requests/sec:  21133.14
Transfer/sec:      2.18MB

# 500 connections #2
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    31.04ms   30.00ms 195.20ms   65.29%
    Req/Sec     3.48k   396.68     6.03k    89.14%
  1246507 requests in 1.00m, 128.39MB read
Requests/sec:  20749.19
Transfer/sec:      2.14MB

# 500 connections #3
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    30.75ms   29.91ms 177.52ms   65.43%
    Req/Sec     3.56k   504.11     6.14k    73.50%
  1274338 requests in 1.00m, 131.25MB read
Requests/sec:  21227.19
Transfer/sec:      2.19MB

# 1000 connections #1
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    56.05ms   45.11ms 298.23ms   57.06%
    Req/Sec     3.13k   488.36     4.51k    73.56%
  1120099 requests in 1.00m, 115.37MB read
Requests/sec:  18643.47
Transfer/sec:      1.92MB

# 1000 connections #2
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    56.82ms   45.68ms 310.98ms   57.69%
    Req/Sec     3.10k   643.87     5.64k    81.82%
  1107741 requests in 1.00m, 114.09MB read
Requests/sec:  18433.60
Transfer/sec:      1.90MB

# 1000 connections #3
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    54.67ms   43.60ms 262.94ms   54.61%
    Req/Sec     3.19k   496.07     4.20k    76.78%
  1142813 requests in 1.00m, 117.71MB read
Requests/sec:  19019.59
Transfer/sec:      1.96MB


```

### Webflux (cpus=4, memory=1024m, heap=512m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.81ms   22.10ms  94.22ms   79.72%
    Req/Sec     7.11k   653.60    33.24k    89.50%
  2547254 requests in 1.00m, 262.36MB read
Requests/sec:  42398.67
Transfer/sec:      4.37MB

# 500 connections #2
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.88ms   22.18ms  95.17ms   79.68%
    Req/Sec     7.11k     1.05k   11.99k    66.47%
  2547777 requests in 1.00m, 262.41MB read
Requests/sec:  42441.73
Transfer/sec:      4.37MB

# 500 connections #3
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    19.09ms   21.00ms  91.93ms   79.98%
    Req/Sec     7.06k   313.77     8.28k    71.47%
  2530129 requests in 1.00m, 260.60MB read
Requests/sec:  42120.89
Transfer/sec:      4.34MB

# 1000 connections #1
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    29.12ms   27.44ms 201.76ms   59.16%
    Req/Sec     7.05k   404.62     9.10k    78.51%
  2524452 requests in 1.00m, 260.01MB read
Requests/sec:  42005.91
Transfer/sec:      4.33MB

# 1000 connections #2
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    29.26ms   27.64ms 266.63ms   59.79%
    Req/Sec     7.01k   626.62    11.40k    90.89%
  2513078 requests in 1.00m, 258.84MB read
Requests/sec:  41823.03
Transfer/sec:      4.31MB

# 1000 connections #3
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    28.93ms   27.12ms 183.89ms   59.41%
    Req/Sec     7.09k   569.28    13.41k    90.22%
  2539767 requests in 1.00m, 261.59MB read
Requests/sec:  42271.28
Transfer/sec:      4.35MB

```

### Webflux (cpus=8, memory=1024m, heap=512m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     8.20ms    8.89ms 134.13ms   87.25%
    Req/Sec    12.65k     1.32k   36.47k    89.28%
  4532893 requests in 1.00m, 466.87MB read
Requests/sec:  75485.24
Transfer/sec:      7.77MB

# 500 connections #2
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     8.27ms    8.95ms 128.32ms   87.31%
    Req/Sec    12.51k     1.22k   15.23k    78.72%
  4483479 requests in 1.00m, 461.78MB read
Requests/sec:  74681.73
Transfer/sec:      7.69MB

# 500 connections #3
Running 1m test @ http://localhost:19002/webflux
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     8.26ms    8.90ms 128.12ms   87.16%
    Req/Sec    12.55k     1.28k   15.09k    84.55%
  4497603 requests in 1.00m, 463.24MB read
Requests/sec:  74907.10
Transfer/sec:      7.72MB

# 1000 connections #1
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.18ms   20.49ms 194.07ms   85.46%
    Req/Sec    10.22k     1.95k   14.11k    74.55%
  3653435 requests in 1.00m, 376.29MB read
Requests/sec:  60810.41
Transfer/sec:      6.26MB

# 1000 connections #2
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.43ms   21.36ms 215.66ms   86.65%
    Req/Sec    10.18k     2.22k   14.24k    73.54%
  3641970 requests in 1.00m, 375.11MB read
Requests/sec:  60639.90
Transfer/sec:      6.25MB

# 1000 connections #3
Running 1m test @ http://localhost:19002/webflux
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.37ms   20.71ms 196.19ms   85.56%
    Req/Sec    10.14k     2.01k   14.57k    72.60%
  3614987 requests in 1.00m, 372.33MB read
Requests/sec:  60168.99
Transfer/sec:      6.20MB

```

### Quarkus (cpus=2, memory=512m, heap=256m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    13.82ms   15.33ms  96.59ms   81.03%
    Req/Sec     9.73k     1.00k   14.65k    69.53%
  3486942 requests in 1.00m, 405.70MB read
Requests/sec:  58081.99
Transfer/sec:      6.76MB

# 500 connections #2
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    13.74ms   15.23ms  69.46ms   81.23%
    Req/Sec     9.78k   488.97    11.75k    72.47%
  3503914 requests in 1.00m, 407.67MB read
Requests/sec:  58347.79
Transfer/sec:      6.79MB

# 500 connections #3
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    13.83ms   15.40ms  93.20ms   81.24%
    Req/Sec     9.74k     1.08k   15.41k    72.69%
  3489703 requests in 1.00m, 406.02MB read
Requests/sec:  58128.27
Transfer/sec:      6.76MB

# 1000 connections #1
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.73ms   16.57ms  93.17ms   81.53%
    Req/Sec     9.25k     0.86k   12.66k    74.11%
  3315439 requests in 1.00m, 385.75MB read
Requests/sec:  55188.82
Transfer/sec:      6.42MB

# 1000 connections #2
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    21.02ms   16.58ms 109.82ms   81.40%
    Req/Sec     9.06k   581.09    10.61k    70.39%
  3244270 requests in 1.00m, 377.47MB read
Requests/sec:  54007.23
Transfer/sec:      6.28MB

# 1000 connections #3
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    20.92ms   16.60ms 102.45ms   81.57%
    Req/Sec     9.13k   667.14    15.71k    79.31%
  3271846 requests in 1.00m, 380.67MB read
Requests/sec:  54450.61
Transfer/sec:      6.34MB

```

### Quarkus (cpus=4, memory=1024m, heap=512m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.67ms    4.93ms  45.53ms   78.12%
    Req/Sec    17.36k     1.18k   20.79k    71.36%
  6219763 requests in 1.00m, 723.66MB read
Requests/sec: 103615.52
Transfer/sec:     12.06MB

# 500 connections #2
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.88ms    5.07ms  44.18ms   78.29%
    Req/Sec    16.68k     0.98k   18.77k    76.33%
  5973509 requests in 1.00m, 695.01MB read
Requests/sec:  99476.99
Transfer/sec:     11.57MB

# 500 connections #3
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.88ms    5.11ms  48.74ms   78.27%
    Req/Sec    16.73k     0.87k   19.36k    73.08%
  5991671 requests in 1.00m, 697.12MB read
Requests/sec:  99807.41
Transfer/sec:     11.61MB

# 1000 connections #1
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.45ms    9.37ms 130.13ms   78.62%
    Req/Sec    16.19k     1.82k   20.37k    69.78%
  5801147 requests in 1.00m, 674.95MB read
Requests/sec:  96620.08
Transfer/sec:     11.24MB

# 1000 connections #2
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.48ms    9.59ms 135.48ms   78.37%
    Req/Sec    16.23k     1.63k   20.47k    73.33%
  5811450 requests in 1.00m, 676.15MB read
Requests/sec:  96740.49
Transfer/sec:     11.26MB

# 1000 connections #3
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    11.65ms   10.03ms 130.21ms   78.11%
    Req/Sec    16.23k     1.74k   20.48k    72.44%
  5813732 requests in 1.00m, 676.42MB read
Requests/sec:  96746.50
Transfer/sec:     11.26MB

```

### Quarkus (cpus=8, memory=1024m, heap=512m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.77ms    9.26ms 116.51ms   88.88%
    Req/Sec    25.42k     3.65k   59.63k    75.36%
  9108673 requests in 1.00m, 1.03GB read
Requests/sec: 151671.25
Transfer/sec:     17.65MB

# 500 connections #2
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.64ms    8.94ms 115.74ms   88.75%
    Req/Sec    25.66k     3.57k   82.64k    76.24%
  9194275 requests in 1.00m, 1.04GB read
Requests/sec: 153087.12
Transfer/sec:     17.81MB

# 500 connections #3
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.78ms    9.36ms 117.87ms   89.00%
    Req/Sec    25.61k     3.57k   33.24k    92.33%
  9174601 requests in 1.00m, 1.04GB read
Requests/sec: 152795.81
Transfer/sec:     17.78MB

# 1000 connections #1
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.36ms   11.31ms 134.10ms   87.59%
    Req/Sec    24.46k     3.15k   30.39k    76.38%
  8756289 requests in 1.00m, 0.99GB read
Requests/sec: 145729.27
Transfer/sec:     16.96MB

# 1000 connections #2
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.49ms   11.76ms 173.67ms   88.27%
    Req/Sec    24.08k     3.24k   29.50k    74.79%
  8615206 requests in 1.00m, 0.98GB read
Requests/sec: 143430.30
Transfer/sec:     16.69MB

# 1000 connections #3
Running 1m test @ http://localhost:19003/quarkus
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.39ms   11.31ms 134.59ms   87.76%
    Req/Sec    24.15k     3.18k   29.72k    77.61%
  8652979 requests in 1.00m, 0.98GB read
Requests/sec: 143997.32
Transfer/sec:     16.75MB

```

### Ktor  (cpus=2, memory=512m, heap=256m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19005/ktor
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    16.04ms   17.20ms  95.24ms   80.89%
    Req/Sec     7.96k     0.94k   11.88k    71.17%
  2852778 requests in 1.00m, 285.67MB read
Requests/sec:  47497.00
Transfer/sec:      4.76MB

# 500 connections #2
Running 1m test @ http://localhost:19005/ktor
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    15.65ms   17.01ms 111.60ms   80.91%
    Req/Sec     8.32k     1.01k   11.80k    69.64%
  2979726 requests in 1.00m, 298.38MB read
Requests/sec:  49635.59
Transfer/sec:      4.97MB

# 500 connections #3
Running 1m test @ http://localhost:19005/ktor
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    15.47ms   16.77ms 111.76ms   80.83%
    Req/Sec     8.39k   697.89    11.37k    76.67%
  3006897 requests in 1.00m, 301.10MB read
Requests/sec:  50062.55
Transfer/sec:      5.01MB

# 1000 connections #1
Running 1m test @ http://localhost:19005/ktor
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    24.48ms   20.86ms 133.03ms   75.43%
    Req/Sec     7.95k   714.89    11.54k    82.83%
  2848927 requests in 1.00m, 285.28MB read
Requests/sec:  47408.40
Transfer/sec:      4.75MB

# 1000 connections #2
Running 1m test @ http://localhost:19005/ktor
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    24.66ms   20.86ms 139.31ms   75.14%
    Req/Sec     7.87k     1.03k   13.36k    88.96%
  2817271 requests in 1.00m, 282.11MB read
Requests/sec:  46893.95
Transfer/sec:      4.70MB

# 1000 connections #3
Running 1m test @ http://localhost:19005/ktor
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    24.57ms   20.62ms 134.68ms   74.91%
    Req/Sec     7.86k     0.96k   11.61k    77.83%
  2816997 requests in 1.00m, 282.08MB read
Requests/sec:  46893.01
Transfer/sec:      4.70MB

```

### Ktor  (cpus=4, memory=1024m, heap=512m)

```shell
# 500 connections #1
Running 1m test @ http://localhost:19005/ktor
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.80ms   10.00ms  63.73ms   84.41%
    Req/Sec    11.55k     0.95k   14.34k    73.08%
  4137792 requests in 1.00m, 414.34MB read
Requests/sec:  68924.79
Transfer/sec:      6.90MB

# 500 connections #2
Running 1m test @ http://localhost:19005/ktor
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.65ms    9.85ms  63.44ms   84.56%
    Req/Sec    11.69k   477.37    13.24k    77.86%
  4187086 requests in 1.00m, 419.28MB read
Requests/sec:  69721.90
Transfer/sec:      6.98MB

# 500 connections #3
Running 1m test @ http://localhost:19005/ktor
  6 threads and 500 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     9.61ms    9.76ms  63.83ms   84.58%
    Req/Sec    11.70k     1.00k   16.47k    74.14%
  4190005 requests in 1.00m, 419.57MB read
Requests/sec:  69787.65
Transfer/sec:      6.99MB

# 1000 connections #1
Running 1m test @ http://localhost:19005/ktor
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    16.32ms   12.73ms  79.89ms   73.23%
    Req/Sec    11.34k     0.96k   14.78k    73.61%
  4059923 requests in 1.00m, 406.54MB read
Requests/sec:  67597.65
Transfer/sec:      6.77MB

# 1000 connections #2
Running 1m test @ http://localhost:19005/ktor
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    16.32ms   12.68ms  78.89ms   73.25%
    Req/Sec    11.31k   425.49    13.26k    69.25%
  4054249 requests in 1.00m, 405.98MB read
Requests/sec:  67482.82
Transfer/sec:      6.76MB

# 1000 connections #3
Running 1m test @ http://localhost:19005/ktor
  6 threads and 1000 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    16.32ms   12.75ms  78.76ms   73.24%
    Req/Sec    11.34k     1.33k   15.03k    67.86%
  4063199 requests in 1.00m, 406.87MB read
Requests/sec:  67658.59
Transfer/sec:      6.78MB

```

### Ktor  (cpus=8, memory=1024m, heap=512m)

```shell

# 500 connections #1
Running 1m test @ http://localhost:19005/ktor
6 threads and 500 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     5.63ms    8.48ms 121.37ms   89.63%
Req/Sec    22.78k     2.54k   28.07k    75.75%
8162216 requests in 1.00m, 817.33MB read
Requests/sec: 135931.15
Transfer/sec:     13.61MB

# 500 connections #2
Running 1m test @ http://localhost:19005/ktor
6 threads and 500 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     5.54ms    8.09ms 121.09ms   89.27%
Req/Sec    22.87k     2.77k   29.08k    86.95%
8188764 requests in 1.00m, 819.99MB read
Requests/sec: 136367.67
Transfer/sec:     13.66MB

# 500 connections #3
Running 1m test @ http://localhost:19005/ktor
6 threads and 500 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     5.54ms    8.36ms 160.76ms   89.80%
Req/Sec    22.78k     2.05k   28.27k    79.22%
8151819 requests in 1.00m, 816.29MB read
Requests/sec: 135763.50
Transfer/sec:     13.59MB

# 1000 connections #1
Running 1m test @ http://localhost:19005/ktor
6 threads and 1000 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     9.62ms   10.49ms 135.45ms   87.56%
Req/Sec    21.91k     3.74k   28.58k    59.97%
7832724 requests in 1.00m, 784.34MB read
Requests/sec: 130402.24
Transfer/sec:     13.06MB

# 1000 connections #2
Running 1m test @ http://localhost:19005/ktor
6 threads and 1000 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     9.75ms   10.99ms 135.84ms   88.16%
Req/Sec    21.86k     4.04k   28.38k    63.16%
7816246 requests in 1.00m, 782.69MB read
Requests/sec: 130065.53
Transfer/sec:     13.02MB

# 1000 connections #3
Running 1m test @ http://localhost:19005/ktor
6 threads and 1000 connections
Thread Stats   Avg      Stdev     Max   +/- Stdev
Latency     9.58ms   10.34ms 137.63ms   87.37%
Req/Sec    21.93k     3.85k   28.47k    64.06%
7842564 requests in 1.00m, 785.32MB read
Requests/sec: 130533.61
Transfer/sec:     13.07MB
```
