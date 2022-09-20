package modernjavainaction.practice;

import java.util.*;

import static modernjavainaction.practice.Dish.builder;

/**
 * 실습에 필요한 데이터 생성 및 관리하는 클래스
 */
public class DataManager {

    public static final Collection<Dish> menu = getDishList();
    public static final Collection<Dish> specialMenu = getSpecialMenu();

    public static final Map<String, List<String>> dishTags= initMap();


    /**
     * Dish 인스턴스 생성
     */
    public static Dish createDishInstacne(String name, boolean vegetarian, int calories, Dish.FoodType foodType) {
        return builder()
                .name(name)
                .vegetarian(vegetarian)
                .calories(calories)
                .type(foodType)
                .build();
    }

    /**
     * Dish List 반환
     */
    public static Collection<Dish> getDishList() {
        return Arrays.asList(
                createDishInstacne("pork", false, 800, Dish.FoodType.MEAT),
                createDishInstacne("beef", false, 700, Dish.FoodType.MEAT),
                createDishInstacne("chicken", false, 400, Dish.FoodType.MEAT),

                createDishInstacne("french", true, 530, Dish.FoodType.OTHER),
                createDishInstacne("rice", true, 350, Dish.FoodType.OTHER),
                createDishInstacne("season fruit", true, 120, Dish.FoodType.OTHER),
                createDishInstacne("pizza", true, 550, Dish.FoodType.OTHER),

                createDishInstacne("prawns", false, 300, Dish.FoodType.FISH),
                createDishInstacne("salmon", false, 450, Dish.FoodType.FISH)
        );
    }

    /**
     * 특별한 요리 리스트 목록 반환
     */
    public static Collection<Dish> getSpecialMenu() {
        return Arrays.asList(
                createDishInstacne("season fruit", true, 120, Dish.FoodType.OTHER),
                createDishInstacne("prawns", false, 300, Dish.FoodType.FISH),
                createDishInstacne("rice", true, 350, Dish.FoodType.OTHER),
                createDishInstacne("chicken", false, 400, Dish.FoodType.MEAT),
                createDishInstacne("french fries", true, 530, Dish.FoodType.OTHER)
        );
    }

    /**
     * 칼로리 수준 Type
     */
    public enum CaloricLevel {
        DIET,
        NORAML,
        FAT
    }

    public static Map initMap(){
        HashMap<Object, Object> map = new HashMap<>();

        map.put("pork",Arrays.asList("greasy","salty"));
        map.put("beef",Arrays.asList("salty","roasted"));
        map.put("chicken",Arrays.asList("fried","crisp"));

        map.put("french",Arrays.asList("greasy","fried"));
        map.put("rice",Arrays.asList("light","natural"));
        map.put("season fruit",Arrays.asList("fresh","natural"));
        map.put("pizza",Arrays.asList("tasty","salty"));

        map.put("prawns",Arrays.asList("tasty","roasted"));
        map.put("salmon",Arrays.asList("delicious","fresh"));

        return map;
    }



}
