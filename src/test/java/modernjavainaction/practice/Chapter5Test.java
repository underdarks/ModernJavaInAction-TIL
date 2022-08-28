package modernjavainaction.practice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static modernjavainaction.practice.DataManager.*;
import static org.assertj.core.api.Assertions.*;

public class Chapter5Test {

    @DisplayName("프레디케이트(불리언을 반환하는 함수)을 활용한 채식주의자 음식 필터링 하기")
    @Test
    public void doFilterByVegetarianDish() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        List<Dish> vegetarianDishes = dishes.stream()
                .filter(Dish::isVegetarian)             //프레디케이트(불리언을 반환하는 함수) 필터링
                .collect(toList());

        //then
        for (Dish vegetarianDish : vegetarianDishes) {
            System.out.println("vegetarianDish = " + vegetarianDish);
        }

        assertThat(vegetarianDishes.size()).isGreaterThan(0);
    }


    @DisplayName("고유 요소로 이루어진 스트림 짝수 필터링")
    @Test
    public void doFilterByUniqueElement() {
        //given
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);

        //when
        List<Integer> evenNumbers = numbers.stream()
                .filter(num -> num % 2 == 0)
                .distinct()         //중복 제거
                .collect(toList());

        //중복 제거 된 짝수 출력
        evenNumbers.stream()
                .forEach(System.out::println);

        //then
        assertThat(evenNumbers.size()).isEqualTo(2);
    }


    /**
     * Filter와 TakeWhile의 차이는 Filter는 모든 요소를 다 확인하지만 TakeWhile은 해당 요소가 조건에 대해 참이 아닐 경우 바로 거기서 연산을 멈춘다
     * takewhile은 소스가 정렬되어 있을때 사용하기 좋음
     */
    @DisplayName("TakeWhile을 활용한 칼로리 320이하인 요리 찾기")
    @Test
    public void findUnder320caloriesDishWithTakeWhile() {
        //given
        Collection<Dish> specialMenu = getSpecialMenu();

        //when
        List<Dish> under320CaloriesDishes = specialMenu.stream()
                .takeWhile(dish -> dish.getCalories() <= 320)    //칼로리순으로 정렬되어있기 때문에 해당 요소가 320 칼로리가 넘으면 takewhile 연산은 종료한다. 시간 복잡도를 줄일 수 있는 장점이 생긴다
                .collect(toList());


        for (Dish dish : under320CaloriesDishes) {
            System.out.println("dish = " + dish);
        }

        //then
        assertThat(under320CaloriesDishes.size()).isEqualTo(2);
    }

    /**
     * DropWhile은 TakeWhile과 정반대의 작업을 수행한다. dropwhile은 프레디케이트가 거짓이 되면 작업을 중단하고 남은 요소를 반환한다
     * dropWhile은 소스가 정렬되어 있을때 사용하기 좋음
     */
    @DisplayName("DropWhile을 활용한 칼로리 320이상인 요리 찾기")
    @Test
    public void findOver320caloriesDishWithDropWhile() {
        //given
        Collection<Dish> specialMenu = getSpecialMenu();

        //when
        List<Dish> over320CaloriesDishes = specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() <= 320)    //칼로리가 320 이상인 음식이 나오면 작업 중단 후 남은 요소들을 반환한다
                .collect(toList());


        for (Dish dish : over320CaloriesDishes) {
            System.out.println("dish = " + dish);
        }

        //then
        assertThat(over320CaloriesDishes.size()).isEqualTo(3);
    }

    @DisplayName("스트림 축소")
    @Test
    public void streamReduction() {
        //given
        Collection<Dish> specialMenu = getSpecialMenu();


        //when
        List<Dish> over300CaloriesDishes = specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() < 300)
                .limit(3)
                .collect(toList());


        for (Dish over300CaloriesDish : over300CaloriesDishes) {
            System.out.println("over300CaloriesDish = " + over300CaloriesDish);
        }


        //then
        assertThat(over300CaloriesDishes.size()).isEqualTo(3);
    }

    @DisplayName("요소 건너뛰기")
    @Test
    public void streamSkip() {
        //given
        Collection<Dish> specialMenu = getSpecialMenu();


        //when
        List<Dish> skipDishes = specialMenu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .skip(2)
                .collect(toList());

        for (Dish skipDish : skipDishes) {
            System.out.println("skipDish = " + skipDish);
        }

        //then
        assertThat(skipDishes.size()).isEqualTo(1);
    }

    @DisplayName("퀴즈 5-1 필터링")
    @Test
    public void quiz_5_1() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        List<Dish> twoMeatDishes = dishes.stream()
                .takeWhile(dish -> dish.getFoodType().equals(Dish.FoodType.MEAT))
                .limit(2)
                .collect(toList());

        for (Dish twoMeatDish : twoMeatDishes) {
            System.out.println("twoMeatDish = " + twoMeatDish);
        }

        //then
    }

    @DisplayName("각 단어가 포함하는 글자 수 반환 - 매핑 활용")
    @Test
    public void streamMapping() {
        //given
        List<String> words = Arrays.asList("Modern", "Java", "In", "Action");

        //when
        List<Integer> wordSize = words.stream()
                .map(String::length)
                .collect(toList());

        for (Integer size : wordSize) {
            System.out.println("size = " + size);
        }

        //then
    }

    @DisplayName("각 요리명의 길이를 반환")
    @Test
    public void getFoodNameLength() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        List<Integer> lengths = dishes.stream()
                .map(dish -> dish.getName().length())
                .collect(toList());

        //then
        for (Integer length : lengths) {
            System.out.println("length = " + length);
        }
    }

    @DisplayName("스트림 평면화")
    @Test
    public void streamFlattening() {
        //["H", "e", "l", "l", "o", "W", "o", "r", "l", "d"] 결과값이 나오게 해야한다
        //given
        List<String> words = Arrays.asList("Hello", "World");

        //1번째 시도
        List<String[]> list = words.stream()
                .map(s -> s.split(""))
                .distinct()
                .collect(toList());

        list.forEach(strings -> strings.toString());

        //2번째 시도
        String[] arrayOfWords = {"Hello", "World"};

        List<Stream<String>> collect = Arrays.stream(arrayOfWords)
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .collect(toList());

        //collect.forEach(System.out::println);

        //3번째 시도(flatMap 사용)
        List<String> result = words.stream()
                .map(s -> s.split(""))                  //각 단어를 개별 문자로 tokenizing("h","e","l" ....)
                .flatMap(strings -> Arrays.stream(strings))   //생성된 스트림을 하나의 스트림으로 평면화
                .distinct()
                .collect(toList());

        result.forEach(System.out::println);

    }

    @DisplayName("퀴즈 5-2-1")
    @Test
    public void quiz_5_2_1() {
        //given
        int[] nums = {1, 2, 3, 4, 5};

        //1번째 방법
        List<Integer> list = Arrays.stream(nums)
                .mapToObj(num -> num * num)  //intStream을 반환하므로 intStream은 toList()로 안됨
                .collect(toList());

        list.forEach(System.out::println);


        //2번째 방법
        List<Integer> list2 = Arrays.stream(nums)
                .map(num -> num * num)
                .boxed()        //intStream을 반환하므로 intStream은 toList()로 안됨 그래서 boxed로 IntStream -> Stream<Integer>로 변환
                .collect(toList());

        list2.forEach(System.out::println);

        //3번째 방법
        List<Integer> list3 = Arrays.asList(1, 2, 3, 4, 5).stream()
                .map(num -> num * num)
                .collect(toList());

        list3.forEach(System.out::println);
    }

    @DisplayName("퀴즈 5-2-2")
    @Test
    public void quiz_5_2_2() {
        //given
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 4);

        //when
        List<String> list = list1.stream()
                .flatMap(v1 -> list2.stream()
                        .map(v2 -> new String("(" + v1 + "," + v2 + ")")))//순서쌍 만들기
                .collect(toList());


        //then
        list.forEach(System.out::println);
    }


    @DisplayName("퀴즈 5-2-3")
    @Test
    public void quiz_5_2_3() {
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 4);

        //when
        List<String> list = list1.stream()
                .flatMap(v1 -> list2.stream()
                        .filter(v2 -> (v1 + v2) % 3 == 0)
                        .map(v2 -> "(" + v1 + "," + v2 + ")")   //순서쌍 만들기
                )
                .collect(toList());


        //then
        list.forEach(System.out::println);
    }

    //요소 하나라도 일치하는지 확인(쇼트서킷)
    @DisplayName("음식들 중에 채식요리가 적어도 하나라도 있는지 확인")
    @Test
    public void isVegetarainFoodAtLeastOne() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        boolean result = dishes.stream()
                .anyMatch(Dish::isVegetarian);  //boolean 반환하는 최종연산

        //then
        assertThat(result).isEqualTo(true);
    }

    //모든 요소 일치 확인(쇼트서킷)
    @DisplayName("모든 음식이 1000칼로리 이하인지 확인")
    @Test
    public void isAllFoodUnder1000Kcal() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        boolean result = dishes.stream()
                .allMatch(dish -> dish.getCalories()<=1000);


        //then
        assertThat(result).isEqualTo(true);
    }

    //모든 요소 불일치 확인(쇼트서킷, <-> AllMatch와 반대 연산)
    @DisplayName("모든 음식이 1000칼로리 이상인지 확인")
    @Test
    public void isAllFoodOver000Kcal() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        boolean result = dishes.stream()
                .noneMatch(dish -> dish.getCalories()>=1000);


        //then
        assertThat(result).isEqualTo(true);
    }

    @DisplayName("요소 검색")
    @Test
    public void ElementSearchByfindAny(){
        //given
        Collection<Dish> dishes = getDishList();

        //when
        dishes.stream()
                .filter(Dish::isVegetarian)
                .findAny()


        //then
    }



}
