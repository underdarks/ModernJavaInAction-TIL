package modernjavainaction.practice;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static modernjavainaction.practice.Dish.*;
import static org.assertj.core.api.Assertions.*;


public class DishTest {

    @DisplayName("Dish 클래스 생성")
    @Test
    public Dish createDishInstacne(String name, boolean vegetarian, int calories, FoodType foodType) {
        return builder()
                .name(name)
                .vegetarian(vegetarian)
                .calories(calories)
                .foodType(foodType)
                .build();
    }

    private Collection<Dish> getDishList() {
        return Arrays.asList(
                createDishInstacne("pork", false, 800, FoodType.MEAT),
                createDishInstacne("beef", false, 700, FoodType.MEAT),
                createDishInstacne("chicken", false, 400, FoodType.MEAT),

                createDishInstacne("french", true, 530, FoodType.OTHER),
                createDishInstacne("rice", true, 350, FoodType.OTHER),
                createDishInstacne("season fruit", true, 120, FoodType.OTHER),
                createDishInstacne("pizza", true, 550, FoodType.OTHER),

                createDishInstacne("prawns", false, 300, FoodType.FISH),
                createDishInstacne("salmon", false, 450, FoodType.FISH)
        );
    }


    @DisplayName("칼로리 300이하인 음식들의 요리명을 낮은 칼로리 기준으로 정렬 후 반환한다")
    @Test
    public void getlowCaloriesFoodName() {
        //given
        Collection<Dish> dishes = getDishList();
        Instant start = Instant.now();

        //when
        //400칼로리보다 적은 음식들의 이름
        List<String> lowCaloriesDishNames = dishes.stream()
                .filter(dish -> dish.getCalories() < 400)               //칼로리가 400이하인 음식들만 필터링
                .sorted(Comparator.comparing(Dish::getCalories))        //칼로리 순으로 정렬(오름차순)
                .map(Dish::getName)                                     //칼로리의 이름만 추출
                .collect(Collectors.toList());                          //리스트화

        for (String lowCaloriesDishName : lowCaloriesDishNames) {
            System.out.println("lowCaloriesDishName = " + lowCaloriesDishName);
        }

        Instant finish = Instant.now();
        System.out.println("Stream 소요 시간(ms) = " + Duration.between(start, finish).toMillis());

        //then
        assertThat(lowCaloriesDishNames.size()).isEqualTo(3);
    }

    @DisplayName("칼로리 300이하인 음시들의 요리명을 낮은 칼로리 기준으로 정렬 후 반환한다(ParallelStream 사용)")
    @Test
    public void getlowCaloriesFoodNameWithParallelStream() {
        //given
        Collection<Dish> dishes = getDishList();
        Instant start = Instant.now();

        //when
        //병렬 처리
        List<String> lowCaloriesDishNames = dishes.parallelStream()
                .filter(dish -> dish.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(Collectors.toList());

        Instant finish = Instant.now();
        System.out.println("ParallelStream 소요 시간(ms) = " + Duration.between(start, finish).toMillis());

        //then
        assertThat(lowCaloriesDishNames.size()).isEqualTo(3);

    }

    @DisplayName("300 칼로리 이상인 음식들의 요리명을 반환한다 ")
    @Test
    public void getHighCaloriesFoodName() {
        //given
        Collection<Dish> dishes = getDishList();
        Instant start = Instant.now();

        //when
        List<String> highCaloriesFoodNames = dishes.stream()
                .filter(dish -> dish.getCalories() > 300)       //300 칼로리 이상인 음식들 필터
                .map(Dish::getName)                             //요리명 추출
                .limit(3)                                       //상위 3개만 선택
                .collect(Collectors.toList());

        for (String highCaloriesFoodName : highCaloriesFoodNames) {
            System.out.println("highCaloriesFoodName = " + highCaloriesFoodName);
        }

        Instant finish = Instant.now();
        System.out.println("Stream 소요 시간(ms) = " + Duration.between(start, finish).toMillis());


        //then
        assertThat(highCaloriesFoodNames.size()).isEqualTo(3);
    }

}
