package modernjavainaction.practice;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static modernjavainaction.practice.DataManager.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


public class Chapter4 {


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

    /**
     * 스트림은 한 번만 탐색할 수 있다. 즉, 탐색된 스트림의 요소는 소비된다.
     */
    @DisplayName("스트림은 딱 한번만 탐색할 수 있다")
    @Test
    public void streamCanOnlyOneSearch(){
        List<String> title = Arrays.asList("Java8", "In", "Action");
        Stream<String> s=title.stream();
        s.forEach(System.out::println);

        //java.lang.IllegalStateException: stream has already been operated upon or closed 예외 발생 -> 스트림은 한 번만 탐색 가능
        assertThrows(IllegalStateException.class,() ->{
            s.forEach(System.out::println);
        });
    }

    @DisplayName("컬렉션 외부 반복")
    @Test
    public void collectionExternalLoop(){
        Collection<Dish> dishes = getDishList();

        //외부 반복
        for (Dish dish : dishes) {
            System.out.println("dish.getName() = " + dish.getName());
        }
    }

    @DisplayName("컬렉션 내부 반복")
    @Test
    public void collectionInternalLoop(){
        Collection<Dish> dishes = getDishList();

        Iterator<Dish> iterator = dishes.iterator();
        while(iterator.hasNext()){  //명시적 반복
            System.out.println("iterator = " + iterator.next().getName());
        }

    }

    @DisplayName("스트림 내부 반복")
    @Test
    public void streamInternalLoop(){
        Collection<Dish> dishList = getDishList();

        List<String> names = dishList.stream()
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    @DisplayName("퀴즈 4-1 리팩토링")
    @Test
    public void quiz_4_1(){
        //리팩토링 전 코드
        Collection<Dish> dishes = getDishList();
        List<String> highCaloriesDishes=new ArrayList<>();
        Iterator<Dish> iterator = dishes.iterator();
        while (iterator.hasNext()){
            Dish dish = iterator.next();

            if(dish.getCalories() > 300){
                highCaloriesDishes.add(dish.getName());
            }
        }

        for (String highCaloriesDish : highCaloriesDishes) {
            System.out.println("highCaloriesDish = " + highCaloriesDish);
        }

        //리팩토링 후 코드
        List<String> collect = dishes.stream()
                .filter(dish -> dish.getCalories() > 300)   //중간 연산
                .map(Dish::getName)                         //중간 연산
                .collect(Collectors.toList());              //최종 연산

        for (String s : collect) {
            System.out.println("refactoring food = " + s);
        }

    }

    @DisplayName("중간 연산")
    @Test
    public void intermediateOperation(){
        Collection<Dish> dishes = getDishList();
        List<String> names = dishes.stream()
                .filter(dish -> {
                    System.out.println("filtering = " + dish.getName());
                    return dish.getCalories() > 300;
                })//필터링
                .map(dish -> {
                    System.out.println("mapping = " + dish.getName());
                    return dish.getName();
                })//요리명 추출
                .limit(3L)//쇼트 서킷
                .collect(Collectors.toList());

        System.out.println("names = " + names);
    }

    @DisplayName("최종 연산")
    @Test
    public void finalOperation(){
        //최종 연산은 스트림 파이프라인에서 결과 도출
        getDishList().stream().forEach(System.out::println);
    }

    @DisplayName("퀴즈 4-2")
    @Test
    public void quiz_4_2(){
        long count = getDishList().stream()
                .filter(d -> d.getCalories() > 300)     //중간 연산
                .distinct()                             //중간 연산
                .limit(3)                               //중간 연산
                .count();                               //최종 연산

        System.out.println("count = " + count);
    }

}
