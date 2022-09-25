package modernjavainaction.practice.Chapter6;

import modernjavainaction.practice.Dish;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static modernjavainaction.practice.Other.DataManager.*;
import static modernjavainaction.practice.Other.DataManager.CaloricLevel.*;
import static modernjavainaction.practice.Dish.*;
import static modernjavainaction.practice.Dish.FoodType.*;
import static org.assertj.core.api.Assertions.*;

/**
 * p 201. 리듀싱과 요약
 */
public class Chap_6_2_리듀싱과요약 {

    /**
     * 6.2.0 - 요리 개수 구하기 (202p)
     */
    @DisplayName("counting() 활용한 요리 계수 구하기")
    @Test
    public void getFoodCount() {
        //given
//        Long collect = menu.stream().count();

        Long foodCount = menu.stream()
                .collect(Collectors.counting());

        //when
        System.out.println("foodCount = " + foodCount);


        //then
        assertThat(foodCount).isEqualTo(9);
    }

    /**
     * 6.2.1 - 스트림값에서 최대값 최소값 검색 (202p)
     */
    @DisplayName("가장 칼로리가 높은 요리 찾기")
    @Test
    public void findHighestCalFoodWithComparingInt() {
        //given
        /**
         * 스트림 요소를 비교하는 데 Compareator.comparingint를 사용하고 파라미터로 Dish의 칼로리로 비교한다
         * 그 후 maxBy를 사용하여 파라미터로 Compertator를 넘긴 후 maxBy는 Reducing 사용하여 최대값을 구한다
         */
        Dish dish = menu.stream()
                .collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)))
                .orElse(null);

        //when
        System.out.println("dish = " + dish);


        //then
        assertThat(dish).isNotNull();
    }

    /**
     * 6.2.2 - 요약 연산 (203p)
     */
    @DisplayName("요약 연산을 할용한 음식 총 칼로리 구하기")
    @Test
    public void findTotalCalWithSummarization() {
        /**
         * 1. summingInt는 객체를 int로 매핑하는 함수를 인수로 받는다.
         * 2. 인수로 전달 된 함수는 객체를 int로 매핑한 컬렉터를 반환한다.
         * 3. 그 후 summingInt가 collect메서드로 전달되면 합계 작업을 수행한다.
         */
        //given
        Integer totalCalories = menu.stream()
                .collect(summingInt(Dish::getCalories));

//        int sum = menu.stream()
//                .mapToInt(Dish::getCalories)
//                .sum();
        //when
        System.out.println("totalCalories = " + totalCalories);

        //then
    }

    /**
     * 6.2.2 - 요약 연산 (203p)
     */
    @DisplayName("요약 연산을 할용한 음식 평균 칼로리 구하기")
    @Test
    public void findAvgCalWithSummarization() {
        //given
        Double avgCal = menu.stream()
                .collect(averagingInt(Dish::getCalories));

        //when
        System.out.println("avgCal = " + Math.round(avgCal));

        //then
    }

    /**
     * 6.2.2 - 요약 연산 (203p)
     */
    @DisplayName("하나의 요약 연산으로 모든 통계 정보를 볼수 있다")
    @Test
    public void intSummaryStatistics() {
        //given
        IntSummaryStatistics menuStatistics = menu.stream()
                .collect(summarizingInt(Dish::getCalories));

        //when
        System.out.println("menuStatistics = " + menuStatistics);
        System.out.println("menuStatistics.getSum() = " + menuStatistics.getSum());
        System.out.println("menuStatistics.getSum() = " + menuStatistics.getCount());
        System.out.println("menuStatistics.getAverage() = " + menuStatistics.getAverage());
        System.out.println("menuStatistics.getMax() = " + menuStatistics.getMax());
        System.out.println("menuStatistics.getMin() = " + menuStatistics.getMin());
        System.out.println("menuStatistics.getClass() = " + menuStatistics.getClass());

        //then
    }


    /**
     * 6.2.3 - 문자열 연결 (204p)
     */
    @DisplayName("모든 요리명을 연결한다")
    @Test
    public void connectAllFoodNames() {
        //given
        String allFoodNames = menu.stream()
                .map(Dish::getName)
                .collect(joining(", ", "[nameList = ", "]")); //joining 메서드는 내부적으로 StringBuilder를 이용하여 문자열을 하나로 만듬

        //when
        System.out.println("allFoodNames = " + allFoodNames);


        //then
    }

    /**
     * 6.2.4 - 범용 리듀싱 요약 연산 (205p)
     */
    @DisplayName("여러개의 인수를 가진 reducing을 사용하여 가장 칼로리가 높은 요리 찾기")
    @Test
    public void findHighestCalWithReducing() {
        //given
        Integer totalCal = menu.stream()
                .collect(reducing(0,     //초기값
                        Dish::getCalories,      //변환 함수
                        Integer::sum));         //합계 함수


        //when
        System.out.println("totalCal = " + totalCal);

        //then
    }

    /**
     * 6.2.4 - 범용 리듀싱 요약 연산 (206p)
     */
    @DisplayName("하나의 인수를 가진 reducing을 사용하여 가장 칼로리가 높은 요리 찾기")
    @Test
    public void findHighestCalFoodWithReducing() {
        //given
        Dish highestCalDish = menu.stream()
                .collect(reducing(
                        (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2
                ))//인수를 하나를 가진 reducing 컬렉터는 시작값이 없으므로 빈값이 존재할 수 있을 경우를 대비해 Optional을 반환한다
                .orElse(null);

        //when
        System.out.println("highestCalDish = " + highestCalDish);

        //then
    }

    /**
     * 6.2.4 - 범용 리듀싱 요약 연산 (206p)
     */
    @DisplayName("collect와 reduce의 차이점")
    @Test
    public void collect_reduce() {
        /**
         * collect와 reduce는 같은 기능 구현할 수 있다. collect로 하는것을 reduce로도 할 수 있다. (*크게 차이점이 없어 보인다)
         * 아래의 reduce를 잘못 활용한 예를 볼 수 있다.
         * 여러 스레드가 동시에 같은 데이터 구조를 고치면 리스트 자체가 망가져버려 리듀싱 연산을 병렬로 수핼할 수 없다는 점이 문제이다.
         * 이러한 문제를 해결하려면 새로운 리스트를 할당해야 하고 객체를 할당하느라 성능이 저하 될것이다.
         * 따라서 가변 컨테이너 작업이면서 병렬성을 확보하려면 collect 메서드로 리듀싱 연산을 구현하는 것이 바람직하다.
         */

        //given
        Stream<Integer> stream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
        List<Integer> numbers = stream.reduce(new ArrayList<Integer>(),
                (List<Integer> l, Integer e) -> {
                    l.add(e);
                    return l;
                },
                (List<Integer> l1, List<Integer> l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
        );

        //when
        numbers.forEach(System.out::println);


        //then
    }

    /**
     * 6.2.4 - 범용 리듀싱 요약 연산 (207p)
     */
    @DisplayName("컬렉셤 프레임워크 유연성을 활용한 음식의 최대 칼로리 구하기")
    @Test
    public void collectionFWFlexible() {
        /**
         * 자바 8의 함수형 프로그래밍에서는 하나의 연산을 다양한 방법으로 해결할 수 있다
         * 또한 스트림 인터페이스에서 직접 제공하는 메서드를 이용하는것에 비해 컬렉터(collect)를 이용하는 코드가 더 복잡한 사실도 보여줌
         * 코드가 좀 더 복잡한 대신 재사용성과 커스터마이징 가능성을 제공하는 높은 수준의 추상화와 일반화를 얻을 수 있다.
         *
         *  => 결과적으로 문제 해결하는 다양한 방법을 확인 후 해당 문제에 특화딘 해결책을 골라 해결하자!
         *  => 아래 코드를 예로 들면 3번쨰 방법인 mapToInt를 활용한 IntStream을 통해 자동 언박싱 연산을 수행하여 Integer -> int로 변환하는 과정을 피해 성능까지 좋은 최적의 선택이 될 수 있다.
         */


        //1번째 방법
        Integer totalCal1 = menu.stream()
                .collect(reducing(0,     //초기값
                        Dish::getCalories,      //변환 함수
                        Integer::sum));//합계 함수

        //2번째 방법
        Integer totalCal2 = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum);

        //3번째 방법
        int totalCal3 = menu.stream()
                .mapToInt(Dish::getCalories)    //intStream 활용
                .sum();

        //then
        assertThat(totalCal1).isEqualTo(totalCal2).isEqualTo(totalCal3);

    }

    /**
     * 퀴즈 6.1 (206p)
     */
    @DisplayName("리듀싱으로 문자열 연결하기")
    @Test
    public void quiz_6_1() {
        //given
        String shortMenu = menu.stream()
                .map(Dish::getName)
                .collect(joining());


        String shortMenu1 = menu.stream()
                .map(Dish::getName)
                .collect(reducing(
                        (d1, d2) -> d1 + d2
                )).get();

//        Dish dish = menu.stream()
//                .collect(reducing(
//                        (d1, d2) -> d1.getName() + d2.getName()
//                ))
//                .get();

        String shortMenu3 = menu.stream()
                .collect(reducing("",   //초기값
                        Dish::getName,         //변환 함수
                        (d1, d2) -> d1 + d2));    //합계 함수

        //then
        assertThat(shortMenu).isEqualTo(shortMenu1).isEqualTo(shortMenu3);
    }

}
