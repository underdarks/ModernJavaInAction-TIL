package modernjavainaction.practice;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static modernjavainaction.practice.Dish.*;
import static org.assertj.core.api.Assertions.*;


public class DishTest {

    @DisplayName("Dish 클래스 생성")
    @Test
    public Dish createDishInstacne(String name, int calories, boolean vegetarian, FoodType foodType){
        return builder()
                .name(name)
                .calories(calories)
                .vegetarian(vegetarian)
                .foodType(foodType)
                .build();
    }


    @DisplayName("저칼로리의 요리명을 낮은 칼로리 기준으로 정렬 후 반환")
    @Test
    public void SortBylowCaloriesfoodAndReturnFoodName(){
        //given
        Dish dish1 = createDishInstacne("음식A", 100, false, FoodType.OTHER);
        Dish dish2 = createDishInstacne("음식B", 300, false, FoodType.OTHER);
        Dish dish3 = createDishInstacne("음식C", 200, false, FoodType.OTHER);

        List<Dish> dishes=new ArrayList<>();
        dishes.add(dish1);
        dishes.add(dish2);
        dishes.add(dish3);

        //when
        //400칼로리보다 적은 음식들의 이름
        List<String> lowCaloriesDishNames = dishes.stream()
                .filter(dish -> dish.getCalories() < 400)               //칼로리가 400이아힝 음식들만 필터링
                .sorted(Comparator.comparing(Dish::getCalories))        //칼로리 순으로 정렬(오름차순)
                .map(Dish::getName)                                     //칼로리의 이름만 추출
                .collect(Collectors.toList());

        for (String lowCaloriesDishName : lowCaloriesDishNames) {
            System.out.println("lowCaloriesDishName = " + lowCaloriesDishName);
        }

        //then
        assertThat(lowCaloriesDishNames.size()).isEqualTo(3);


    }

}
