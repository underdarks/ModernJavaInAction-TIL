package modernjavainaction.practice;


import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@BenchmarkMode(Mode.AverageTime)        //벤치마크 대상 메서드를 실행하는 데 걸린 평균 시간
@OutputTimeUnit(TimeUnit.MILLISECONDS)  //벤치마크 결과를 밀리초 단위로 출력
@Fork(value = 2, jvmArgs = {"-Xms4G","-Xmx4G"}) //4GB의 힙 공간을 제공한 환경에서 두 번 벤치마크를 수행해 결과의 신뢰성 확보
public class ParallelStreamBenchmark {
    private static final long N=10_000_000L;

    @Benchmark  //벤치마크 대상 메서드
    public long sequentialSum(){
        return Stream.iterate(1L, i-> i+1)
                .limit(N)
                .reduce(0L,Long::sum);
    }

    @TearDown(Level.Invocation) //매 번 벤치마크를 실행한 다음에는 가비지 컬렉터 동작 시도
    public void tearDown(){
        System.gc();
    }

    @Benchmark
    public long parallelRangedSum(){
        return LongStream.rangeClosed(1,N)
                .parallel()
                .reduce(0L,Long::sum);
    }

}
