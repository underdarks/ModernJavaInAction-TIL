package modernjavainaction.practice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.Collectors.*;

import static java.util.stream.Collectors.*;
import static modernjavainaction.practice.DataManager.*;
import static org.assertj.core.api.Assertions.*;

public class Chapter6 {


    @DisplayName("counting() 활용한 요리 계수 구하기")
    @Test
    public void t(){
        //given
        Long collect = menu.stream().count();

//        Long collect = menu.stream()
//                .collect(Collectors.counting());

        //when
        System.out.println("collect = " + collect);


        //then
        assertThat(collect).isEqualTo(9);
    }

    @DisplayName("가장 칼로리가 높은 요리 찾기")
    @Test
    public void findHighestCalFoodWithComparingInt(){
        //given
        Dish dish = menu.stream().max(Comparator.comparingInt(Dish::getCalories))
                .orElse(null);

        //when
        System.out.println("dish = " + dish);


        //then
    }

    @DisplayName("요약 연산을 할용한 음식 총 칼로리 구하기")
    @Test
    public void findTotalCalWithSummarization(){
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

    @DisplayName("요약 연산을 할용한 음식 평균 칼로리 구하기")
    @Test
    public void findAvgCalWithSummarization(){
        //given
        Double avgCal = menu.stream()
                .collect(averagingInt(Dish::getCalories));

        //when
        System.out.println("avgCal = " + Math.round(avgCal));

        //then
    }

    @DisplayName("하나의 요약 연산으로 모든 통계 정보를 볼수 있다")
    @Test
    public void intSummaryStatistics(){
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


    @DisplayName("모든 요리명을 연결한다")
    @Test
    public void connectAllFoodNames(){
        //given
        String allFoodNames = menu.stream()
                .map(Dish::getName)
                .collect(joining(", ","[nameList = ","]")); //joining 메서드는 내부적으로 StringBuilder를 이용하여 문자열을 하나로 만듬

        //when
        System.out.println("allFoodNames = " + allFoodNames);


        //then
    }

    @DisplayName("가장 칼로리가 높은 요리 찾기")
    @Test
    public void findHighestCalFoodWithReducing(){
        //given
        Dish highestCalDish = menu.stream()
                .collect(reducing(
                        (d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2
                ))
                .orElse(null);

        //when
        System.out.println("highestCalDish = " + highestCalDish);


        //then
    }
}
