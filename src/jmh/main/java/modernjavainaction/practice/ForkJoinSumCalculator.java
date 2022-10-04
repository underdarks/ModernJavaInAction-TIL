package modernjavainaction.practice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

@Getter
@Slf4j
/**
 * RecursiveTask를 상속받은 서브클래스
 */
public class ForkJoinSumCalculator extends RecursiveTask<Long> {        //RecursiveTask를 상속받아 포크/조인 프레임워크에서 사용할 태스크 생성

    private final long[] numbers;
    private final int start;    //서브태스크에서 처리할 배열의 초기 위치
    private final int end;      //최종 위치
    public static final long THRESHOLD = 10_000;  //이 값 이하의 서브태스크는 더 이상 분할할 수 없다.

    /**
     * 메인 태스크의 서브태스크를 재귀적으로 만들 때 사용할 비공개 생성자
     */
    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    @Override
    protected Long compute() {
        int length = end - start;   //이 태스크에서 더할 배열의 길이
        if (length <= THRESHOLD) {  //분할할 만큼 길이가 충분하지 않으면 순차적으로 계산한다
            return computeSequentially();
        }

        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2); //서브 태스크 생성
        leftTask.fork();    //ForkJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기로 실행한다

        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);  //서브 태스크 생성
        Long rightResult = rightTask.compute();     //해당 서브태스크를 동기 실행한다. 이때 추가로 분할이 일어날 수 있다
        log.warn("rightResult = {}", rightResult);

        Long leftResult = leftTask.join();          //첫 번째 서브태스크의 결과를 읽거나 아직 결과가 없으면 기다린다.
        log.warn("leftResult = {}", leftResult);
        return leftResult + rightResult;
    }

    /**
     * 더 분할할 수 없을 때 서브태스크를 순차적으로 결과 계산하는 알고리즘
     */
    private Long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += numbers[i];
        }

        return sum;
    }

    public static long forkJoinSum(long n){
        long[] numbers = LongStream.rangeClosed(1, n).toArray();
        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);

        return new ForkJoinPool().invoke(task); //재정의한 compute 실행 됨
    }
}
