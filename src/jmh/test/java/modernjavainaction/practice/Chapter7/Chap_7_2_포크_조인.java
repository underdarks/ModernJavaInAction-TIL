package modernjavainaction.practice.Chapter7;

import modernjavainaction.practice.ForkJoinSumCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * 7.2 포크/조인 프레임워크 (255p)
 */
public class Chap_7_2_포크_조인 {

    /**
     * 7.2.1 RecursiveTask 활용 (255p)
     *
     *  - 포크/조인 프레임워크에서는 서브태스크를 스레드 풀의 작업자 스레드에 분산 할당하는 Executorservice 인터페이스를 구현한다.
     *  - 스레드 풀을 이용하려면 RecursiveTask<R>의 서브클래스를 만들어야 한다. 여기서 R은 병렬화 된 태스크의 결과 또는 결과가 없을 때 사용하는 RecursiveAction 형식이다.
     *  - RecursiveTask를 정의하려면 추상 메서드 compute를 구현해야 한다.
     */
    @DisplayName("스레드 풀을 이용한 태스크 병렬화 ")
    @Test
    public void recursiveTask(){
        //given
        StopWatch stopWatch = new StopWatch();
        long n = 100_000_000L;

        stopWatch.start("1부터 N까지 스레드 풀을 이용한 태스크 병렬화");
        long result = ForkJoinSumCalculator.forkJoinSum(n);

        stopWatch.stop();
        System.out.println("수행시간(s) = " + stopWatch.getTotalTimeSeconds());   //수행시간(s) = 0.011898, 순차스트림보다 10배 이상차이

        //then
        System.out.println("result = " + result);
    }

    /**
     * 7.2.2 포크/조인 프레임워크를 제대로 사용하는 방법
     *
     *  -
     */
}
