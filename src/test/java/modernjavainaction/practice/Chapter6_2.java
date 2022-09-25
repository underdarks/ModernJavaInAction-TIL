package modernjavainaction.practice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.DataInput;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;
import static modernjavainaction.practice.DataManager.*;
import static modernjavainaction.practice.Dish.*;
import static modernjavainaction.practice.Dish.FoodType.*;
import static org.assertj.core.api.Assertions.*;

public class Chapter6_2 {
    /**
     * p 219. 6.4분할부터 시작~
     */


    @DisplayName("채식 요리/채식이 아닌 요리 분류")
    @Test
    public void classifyVegetrianFood() {
        //given
        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));   //분할 함수

        /**
         * filter를 사용한 채식 주의자 음식 구분
         * 단, 위의 partitioningBy와 다르게 채식주의자가 아닌 음식들을 구분하지 못한다
         */
        List<Dish> vegetarianFoods = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(toList());

        //when
        partitionedMenu.forEach((isVegetfood, dishes) -> System.out.println("isVegetfood = " + isVegetfood + ", food = " + dishes));

        //then
        //채식주의자 음식이 있는지 검증
        assertThat(partitionedMenu.get(true).size()).isGreaterThan(0);
    }

    @DisplayName("채식 요리/채식이 아닌 요리 타입별로 구분")
    @Test
    public void classifyVegetrianFoodByType() {
        //given
        Map<Boolean, Map<FoodType, List<Dish>>> vegetrianDishesByType = menu.stream()
                .collect(partitioningBy(    //분류
                                Dish::isVegetarian,         //프리디케이트(분할 함수)
                                groupingBy(Dish::getType)   //그룹화
                        )
                );  //6.3.1 다수준 그릅화와 비슷

        //when
        vegetrianDishesByType.forEach((isVegetfood, foodTypeListMap) -> System.out.println("isVegetfood = " + isVegetfood + ", foodTypeDish = " + foodTypeListMap));


        //then
        //채식 요리중 생선 음식은 없어야 한다
        assertThat(vegetrianDishesByType.get(true).get(FISH)).isNull();
    }

    @DisplayName("채식/비채식 요리 중 가장 칼로리가 높은 요리 찾기")
    @Test
    public void findHighCalFoodInGroup() {
        //given
        Map<Boolean, Dish> highCalFoodinGroupByVegetarian = menu.stream()
                .collect(partitioningBy(
                                Dish::isVegetarian,
                                collectingAndThen(
                                        maxBy(Comparator.comparingInt(Dish::getCalories)),
                                        Optional::get
                                )
                        )
                );

        //when
        highCalFoodinGroupByVegetarian.forEach((isVegetfood, dish) -> System.out.println("isVegetfood = " + isVegetfood + ", food = " + dish));


        //then
        assertThat(highCalFoodinGroupByVegetarian.get(true).getName()).isEqualTo("pizza");
    }

    /**
     * expect : 채식/비채식 으로 분류 후 거기서 다시 칼로리가 500넘는 음식인지 아닌지로 한번 더 분류
     */
    @DisplayName("퀴즈 6-2-1")
    @Test
    public void quiz_6_2_1() {
        //given
        Map<Boolean, Map<Boolean, List<Dish>>> result = menu.stream()
                .collect(partitioningBy(
                                Dish::isVegetarian,
                                partitioningBy(dish -> dish.getCalories() > 500)
                        )
                );

        //when
        result.forEach((isVegetfood, booleanListMap) -> System.out.println("isVegetfood = " + isVegetfood + ", foods =" + booleanListMap));


        //then
        //채식 음식 중 칼로리가 500넘는 음식들이 있는지 확인
        assertThat(result.get(true).get(true).size()).isGreaterThan(0);
    }

    /**
     * expect : 컴파일 안됨
     * actual : partitioningBy는 프레디케이트(boolean을 반환하는 함수)를 요구하므로 컴파일 안됨
     */
    @DisplayName("퀴즈 6-2-2")
    @Test
    public void quiz_6_2_2() {
        //given
//        menu.stream()
//                .collect(partitioningBy(
//                                Dish::isVegetarian,
//                                partitioningBy(Dish::getType)
//                        )
//                );


        //when


        //then
    }

    @DisplayName("퀴즈 6-2-3")
    @Test
    public void quiz_6_2_3() {
        //given
        Map<Boolean, Long> result = menu.stream()
                .collect(partitioningBy(
                        Dish::isVegetarian,
                        counting()
                ));

        //when
        result.forEach((isVegetfood, count) -> System.out.println("isVegetfood = " + isVegetfood + ", count = " + count));


        //then
        assertThat(result.get(true)).isEqualTo(4);
    }

}

