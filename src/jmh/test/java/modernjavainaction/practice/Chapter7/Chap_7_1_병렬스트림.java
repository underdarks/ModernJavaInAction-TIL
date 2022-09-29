package modernjavainaction.practice.Chapter7;

import modernjavainaction.practice.Accumulator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.*;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

/**
 * p.242 7.1 병렬 스트림
 */
@BenchmarkMode(Mode.AverageTime)        //벤치마크 대상 메서드를 실행하는 데 걸린 평균 시간
@OutputTimeUnit(TimeUnit.MILLISECONDS)  //벤치마크 결과를 밀리초 단위로 출력
@Fork(value = 2, jvmArgs = {"-Xms4G","-Xmx4G"}) //4GB의 힙 공간을 제공한 환경에서 두 번 벤치마크를 수행해 결과의 신뢰성 확보
public class Chap_7_1_병렬스트림 {
    /**
     * 병렬 스트림이란 각각의 스레드에서 처리할 수 있도록 스트림 요소를 여러 청크로 분할한 스트림이다.
     * 따라서, 병렬 스트림을 이용하면 모든 멀티코어 프로세서가 각각의 청크를 처리하도록 할당할 수 있다.
     */

    /**
     * 7.1 - 병렬 스트림 (242p)
     */
    @DisplayName("1부터 n까지의 모든 숫자의 합을 순차적으로 구하기")
    @Test
    public void sequentialSum() {
        //given
        StopWatch stopWatch = new StopWatch();
        int n = 100000000;

        stopWatch.start("1부터 n 순차 스트림");
        //when
        Long sum = Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);

        stopWatch.stop();
        System.out.println("수행시간(s) = " + stopWatch.getTotalTimeSeconds()); //수행시간(s) = 0.001501099
        System.out.println("sum = " + sum);

        //then
//        assertThat(sum).isEqualTo(55);
    }

    /**
     * 7.1.1 - 순차 스트림을 병렬 스트림으로 변환하기 (243p)
     * - 병렬 스트림은 리듀싱 연산을 여러 청크에 병렬로 수해할 수 있음.
     * - 마지막으로 리듀싱 연산으로 생성된 부분 결과를 다시 리듀싱 연산으로 합쳐서 전체 스트림의 리듀싱 결과를 도출함
     */
    @DisplayName("1부터 n까지의 모든 숫자의 합을 병렬적으로 구하기")
    @Test
    public void parallelSum() {
        //given
        StopWatch stopWatch = new StopWatch();
        int n = 100_000_000;

        stopWatch.start("1부터 n 순차 스트림");
        //when
        Long sum = Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()     //스트림을 병렬 스트림으로 변환
                .reduce(0L, Long::sum);
//                .collect(reducing(0L,Long::sum))

        stopWatch.stop();
        System.out.println("수행시간(s) = " + stopWatch.getTotalTimeSeconds());   //수행시간(s) = 0.011898, 순차스트림보다 10배 이상차이
        System.out.println("sum = " + sum);


        //then
//        assertThat(sum).isEqualTo(55);
    }

    @DisplayName("순차 스트림 / 병렬 스트림 파이프라인 영향도")
    @Test
    public void explain() {
        /**
         * parallel과 sequential 두 메서드 중 최종적으로 호출된 메서드가 전체 파이프라인에 영향을 미친다.
         * 아래 코드에서는 parallet()이 마지막 호출이므로 파이프라인은 전체적으로 병렬로 실행된다.
         */
//        Stream.of()
//                .parallel()
//                .filter()
//                .sequential()
//                .map()
//                .parallel()
//                .reduce()

    }

    /**
     * 7.1.2 스트림 성능 측정 (250p)
     *
     * - 같은 parallel()을 사용한 병렬 스트림이더라도 올바른 자료구조를 선택해야 병렬 실행도 최적의 성능을 발휘할 수 있다.
     * - 아래 코드 돌리면 위의 parallelSum() 테스트 코드, 순차 스트림 보다도 훨씬 더 빠르다!!!!
     *
     * - 여기서는 LongStream.rangeClosed은 long을 직접 사용하므로 박싱, 언박싱 오버헤드가 사라짐
     * - LongStream.rangeClosed는 쉽게 청크로 분할할 수 있는 숫자 범위를 생산함(ex. 1~20 범위의 숫자를 각각 1~5, 6~10, 11~15, 16~20 범위로 숫자 분할 가능하다)
     *
     * - 병렬화는 완전 꽁짜는 아니다.
     * - 병렬화를 이용하려면 스트림을 재귀적으로 분할해야 하고, 각 서브스트림을 서로 다른 스레드의 리듀싱 연산으로 할당하고, 이를 하나의 값으로 합쳐야 한다(멀티코어 간의 데이터 이동 비용은 비싸다)
     * - 병렬화는 상황에 맞게 잘 써야한다!(잘못 쓰면 이슈발생)
     */
    @Benchmark
    @DisplayName("1부터 n까지의 모든 숫자의 합을 병렬적으로 구하기 - 최적의 자료구조를 사용한 병렬 스트림")
    @Test
    public void parallelRangedSum() {
        //given
        StopWatch stopWatch = new StopWatch();
        long N = 100_000_000L;

        stopWatch.start("1부터 n 병렬 스트림");
        //when
        long sum = LongStream.rangeClosed(1, N)
                .parallel()
                .reduce(0L, Long::sum);

        stopWatch.stop();
        System.out.println("수행시간(s) = " + stopWatch.getTotalTimeSeconds());   //수행시간(s) = 0.011898, 순차스트림보다 10배 이상차이
        System.out.println("sum = " + sum);

        //then
    }

    @TearDown(Level.Invocation) //매 번 벤치마크를 실행한 다음에는 가비지 컬렉터 동작 시도
    public void tearDown(){
        System.gc();
    }

    /**
     * 7.1.3 - 병렬 스트림의 올바른 사용법
     *
     * - 아래 코드는 순차 실행 할 수 있또록 구현되어 있으므로 병렬로 실행하면 대참사가 일어남
     * - 특히 total에 접근할 때마다 (다수 스레드가 동시 데이터 접근하는) 데이터 레이스 문제가 일어난다.
     * - 동기화 문제를 해결하다보면 결국 병렬화라는 특성이 없어져 성능적으로 효과 보지 못한다.
     * */
    @DisplayName("병렬로 실행 시 문제있는 코드")
    @Test
    public void sideEffectSum(){
        //given
        StopWatch stopWatch = new StopWatch();
        long n = 100_000_000L;

        stopWatch.start("1부터 N까지 순차 누적 합");

        //when
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1,n)
                .forEach(accumulator::add);


        stopWatch.stop();
        System.out.println("수행시간(s) = " + stopWatch.getTotalTimeSeconds());   //수행시간(s) = 0.011898, 순차스트림보다 10배 이상차이
        System.out.println("accumulator.total = " + accumulator.total);

        //then
//        assertThat(accumulator.total).isEqualTo(55);
    }

    /**
     * 7.1.3 - 병렬 스트림의 올바른 사용법
     *
     *  - 순차 스트림을 병렬로 사용할 경우 잘못된 예시를 보여준다.
     *  - 여러 스레드간의 하나의 리소스에 접근함에 따라 Race Condition 문제(여러 스레드가 동시에 데이터에 접근할 때 실행 순서에 따라 값이 달라지는 현상)가 발생하게 됨
     * */
    @DisplayName("")
    @Test
    public void sideEffectParallelSum(){
        //given
        StopWatch stopWatch = new StopWatch();
        long n = 100_000_000L;

        stopWatch.start("1부터 N까지 병렬 누적 합");

        //when
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1,n)
                .parallel()
                .forEach(accumulator::add);


        stopWatch.stop();
        System.out.println("수행시간(s) = " + stopWatch.getTotalTimeSeconds());   //수행시간(s) = 0.011898, 순차스트림보다 10배 이상차이
        System.out.println("accumulator.total = " + accumulator.total);         //올바른 결과값(5000000050000000)이 출력 안됨, actual : 650527279667665

        //then
//        assertThat(accumulator.total).isEqualTo(55);
    }



}
