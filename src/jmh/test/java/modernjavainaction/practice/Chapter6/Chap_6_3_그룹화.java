package modernjavainaction.practice.Chapter6;

import modernjavainaction.practice.Dish;
import modernjavainaction.practice.Other.DataManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.toCollection;
import static modernjavainaction.practice.Dish.FoodType.FISH;
import static modernjavainaction.practice.Other.DataManager.CaloricLevel.*;
import static modernjavainaction.practice.Other.DataManager.CaloricLevel.FAT;
import static modernjavainaction.practice.Other.DataManager.dishTags;
import static modernjavainaction.practice.Other.DataManager.menu;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * 6.3 - 그룹화 (210p)
 */
public class Chap_6_3_그룹화 {

    /**
     * 6.3 - 그룹화 (210p)
     */
    @DisplayName("groupingBy를 활용한 음식 종류별 그룹화")
    @Test
    public void groupingByFoodType() {
        //given
        Map<Dish.FoodType, List<Dish>> dishesByType = menu.stream()
                .collect(groupingBy(Dish::getType));

        //when
        dishesByType.forEach((foodType, dishes)
                -> System.out.println("foodType = " + foodType + ", dishes = " + dishes));


        //then
    }

    /**
     * 6.3 - 그룹화 (210p)
     */
    @DisplayName("칼로리 범위 별 칼로리 level 나누기")
    @Test
    public void divideCalLevelByRange() {
        //given
        Map<DataManager.CaloricLevel, List<Dish>> dishesByCalroricLevel = menu.stream()
                .collect(
                        groupingBy(dish -> {
                                    if (dish.getCalories() <= 400) return DIET;
                                    else if (dish.getCalories() <= 700) return NORAML;
                                    else return FAT;
                                }
                        )  //칼로리 범위 별 칼로리 level 설정
                );

        //when
        dishesByCalroricLevel.forEach((foodType, dishes)
                -> System.out.println("foodType = " + foodType + ", dishes = " + dishes));


        //then
    }

    /**
     * 6.3.1 - 그룹화된 요소 조작 (211p)
     */
    @DisplayName("Filter와 grouping을 활용한 500 칼로리가 넘는 음식 조회")
    @Test
    public void find500CalOverDishesWithFilterAndGrouping() {
        /**
         * Filter를 사용해서 그룹화 해버리면 Diet 그룹이 없어지게 된다.
         *  => 즉, Map에서 해당 키 자체가 사라져서 그룹 데이터를 확인할 수 없다.
         */
        //given
        Map<Dish.FoodType, List<Dish>> caloricDishesByType = menu.stream()
                .filter(dish -> dish.getCalories() > 500)
                .collect(groupingBy(Dish::getType));

        //when
        caloricDishesByType.forEach((foodType, dishes)
                -> System.out.println("foodType = " + foodType + ", dishes = " + dishes));


        //then
    }

    /**
     * 6.3.1 - 그룹화된 요소 조작 (212p)
     */
    @DisplayName("collect(grouping,filtering)을 활용한 500 칼로리가 넘는 음식 조회")
    @Test
    public void find500CalOverDishesWithcollect() {
        /**
         *  위의 코드와 다르게
         *  아래 코드를 실행했을 때 Fish 타입이 필터링이 되었더라도 map에 key로 존재하게 된다.
         *   => 즉, 필터링이 되었더라도 그룹(key)이 사라지진 않는다.
         */
        //given
        Map<Dish.FoodType, List<Dish>> caloricDishesByType = menu.stream()
                .collect(groupingBy(
                                Dish::getType,
                                filtering(dish -> dish.getCalories() > 500, toList())
                        )
                );

        //when
        caloricDishesByType.forEach((foodType, dishes)
                -> System.out.println("foodType = " + foodType + ", dishes = " + dishes));


        //then
    }

    /**
     * 6.3.1 - 그룹화된 요소 조작 (212p)
     */
    @DisplayName("mapping을 활용한 그룹화 된 요소 변환하는 작업")
    @Test
    public void transformElementWithMapping() {
        //given
        Map<Dish.FoodType, List<String>> dishNamesByType = menu.stream()
                .collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));  //type별 그룹화 후 음식 이름 추출

        //when
        dishNamesByType.forEach((foodType, name)
                -> System.out.println("foodType = " + foodType + ", name = " + name));

        //then
        assertThat(dishNamesByType.get(FISH).contains("salmon")).isTrue();
    }

    /**
     * 6.3.1 - 그룹화된 요소 조작 (213p)
     */
    @DisplayName("grouping-flatMapping을 활용한 태그 추출")
    @Test
    public void findTagWithGroupingAndFlatMapping() {
        //given
        Map<Dish.FoodType, Set<String>> dishNamesByType = menu.stream()
                .collect(groupingBy(
                        Dish::getType,
                        flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet()))
                );

        //when
        dishNamesByType.forEach((foodType, set)
                -> System.out.println("foodType = " + foodType + ", tag = " + set));

        //then
        assertThat(dishNamesByType.get(FISH).size()).isEqualTo(4);
    }


    /**
     * 6.3.2 - 다수준 그룹화 (213p)
     */
    @DisplayName("다수준 그룹화 예시")
    @Test
    public void groupingMultiLevel() {

        /**
         * 음식별로 그룹화 후 그 그룹에서 칼로리별로 또 그룹화 한다.
         */
        //given
        Map<Dish.FoodType, Map<DataManager.CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream()
                .collect(groupingBy(
                        Dish::getType,      //첫 번째 분류 함수
                        groupingBy(dish -> {    // 두번째 분류 함수
                            if (dish.getCalories() <= 400)
                                return DIET;
                            else if (dish.getCalories() <= 700)
                                return NORAML;
                            else return FAT;
                        })
                ));


        //when
        dishesByTypeCaloricLevel.forEach((foodType, caloricLevelListMap)
                -> System.out.println("foodType = " + foodType + ", " + caloricLevelListMap.toString()));

        //then
        assertThat(dishesByTypeCaloricLevel.size()).isEqualTo(3);
    }

    /**
     * 6.3.3 - 서브그룹으로 데이터 수집 (215p)
     */
    @DisplayName("종류별 요리의 수 구하기")
    @Test
    public void findNumofDishesType() {
        //given
        Map<Dish.FoodType, Long> typesCount = menu.stream()
                .collect(groupingBy(Dish::getType, counting()));

        //when
        typesCount.forEach((foodType, count)
                -> System.out.println("foodType = " + foodType + ", count=" + count));


        //then
        assertThat(typesCount.get(FISH)).isEqualTo(2);
    }

    /**
     * 6.3.3 - 서브그룹으로 데이터 수집 (216p)
     */
    @DisplayName("종류별 요리의 총 칼로리 구하기")
    @Test
    public void findTotalCalTypeOfDish() {
        //given
        Map<Dish.FoodType, Integer> totalCalTypeDish = menu.stream()
                .collect(groupingBy(Dish::getType, summingInt(Dish::getCalories)));

        //when
        totalCalTypeDish.forEach((foodType, totalCal)
                -> System.out.println("foodType = " + foodType + ", totalCal=" + totalCal));


        //then
        assertThat(totalCalTypeDish.get(FISH)).isEqualTo(750);
    }

    /**
     * 6.3.3 - 서브그룹으로 데이터 수집 (216p)
     */
    @DisplayName("요리 종류별 가장 높은 칼로리를 가진 요리 찾기")
    @Test
    public void findHighestCalFoodByDishType() {
        //given
        Map<Dish.FoodType, Optional<Dish>> highestCalFoodByType = menu.stream()
                .collect(groupingBy(
                                Dish::getType,
                                maxBy(Comparator.comparingInt(Dish::getCalories))   //maxBy메서드와 comparingInt를 활용한 칼로리 비교
                        )
                );

        //when
        highestCalFoodByType.forEach((foodType, dish)
                -> System.out.println("foodType = " + foodType + ", dish = " + dish.get().getName()));


        //then
        assertThat(highestCalFoodByType.get(FISH).get().getName().contains("salmon")).isTrue();
    }

    /**
     * 6.3.3 - 서브그룹으로 데이터 수집 (216p)
     */
    @DisplayName("각 서브그룹에서 가장 칼로리가 높은 요리 찾기")
    @Test
    public void findHighestCalFoodByInSubGroup() {
        //given
        Map<Dish.FoodType, Dish> highestCalFoods = menu.stream()
                .collect(groupingBy(
                                Dish::getType,  //분류 기준
                                collectingAndThen(   //collectingAndThen으로 적용할 컬렉터와 변환 함수를 인수로 받아 다른 컬렉터를 반환한다.
                                        maxBy(Comparator.comparingInt(Dish::getCalories)),
                                        Optional::get   //변환 함수
                                )
                        )

                );


        //when
        highestCalFoods.forEach((name, dishes)
                -> System.out.println("name = " + name + ", dish=" + dishes.toString()));


        //then
        assertThat(highestCalFoods.get(FISH).getName()).isEqualTo("salmon");
    }


    /**
     * 6.3.3 - 서브그룹으로 데이터 수집 (218p)
     * grouping by와 함께 사용하는 다른 컬렉터 예시
     */
    @DisplayName("각 요리 형식에 존재하는 모든 CaloricLevel 구하기")
    @Test
    public void getCalLevelByDishType() {
        //given
        Map<Dish.FoodType, Set<DataManager.CaloricLevel>> foodsbyType = menu.stream()
                .collect(groupingBy(
                                Dish::getType,
                                mapping(dish -> {   //Dish -> CaloricLevel로 매핑해준다
                                            if (dish.getCalories() <= 400) return DIET;
                                            else if (dish.getCalories() <= 700) return NORAML;
                                            else return FAT;
                                        }
                                        , toSet()
                                )
                        )
                );

        //when
        foodsbyType.forEach((foodType, caloricLevels)
                -> System.out.println("foodType = " + foodType + ", caloricLevel = " + caloricLevels));


        //then
        assertThat(foodsbyType.get(FISH).contains(DIET)).isTrue();
    }

    /**
     * 6.3.3 - 서브그룹으로 데이터 수집 (218p)
     */
    @DisplayName("toCollection 활용한 각 요리 형식에 존재하는 모든 CaloricLevel 구하기")
    @Test
    public void getCalLevelByDishTypeBytoCollection() {
        //given
        Map<Dish.FoodType, HashSet<DataManager.CaloricLevel>> caloricLevelsByType = menu.stream()
                .collect(groupingBy(
                                Dish::getType,
                                mapping(dish -> {
                                    if (dish.getCalories() <= 400) return DIET;
                                    else if (dish.getCalories() <= 700) return NORAML;
                                    else return FAT;
                                }, toCollection(HashSet::new))
                        )
                );

        //when
        caloricLevelsByType.forEach((foodType, caloricLevels)
                -> System.out.println("foodType = " + foodType + " calLevel = " + caloricLevels));

        //then
    }
}
