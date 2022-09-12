package modernjavainaction.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static modernjavainaction.practice.DataManager.*;
import static org.assertj.core.api.Assertions.*;

public class Chapter5_2 {

    @DisplayName("숫자형 스트림")
    @Test
    public void numericStream() {
        //given
        Collection<Dish> dishes = getDishList();


        /**
         * Stream<T>를 반환하면 sum 메셔드 사용 불가
         */
        dishes.stream()
                .map(Dish::getCalories);

        /**
         * IntStream, DoubleStream, LongStream은 기본형 특화 스트림 API를 제공한다
         */
        Integer totalCalories = dishes.stream()
                .mapToInt(Dish::getCalories)//Stream<Integer>가 아닌 IntStream 반환
                .sum();


        //then
        System.out.println("totalCalories = " + totalCalories);
    }

    @DisplayName("객체 스트림으로 복원")
    @Test
    public void restoreToObjStream() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        IntStream intStream = dishes.stream().mapToInt(Dish::getCalories);  //stream -> IntStream
        Stream<Integer> stream = intStream.boxed(); //IntStream -> Stream<Integer>

        //then
        System.out.println("intStream = " + intStream);
        System.out.println("stream = " + stream);
    }

    @DisplayName("OptionalInt")
    @Test
    public void optionalInt() {
        //given
        Collection<Dish> dishes = getDishList();

        //when
        int max = dishes.stream()
                .mapToInt(Dish::getCalories)
                .max()  //값이 존재하지 않을 경우를 대비해 Optional을 반환한다
                .orElse(1);


        //then
        assertThat(max).isNotEqualTo(1);
        System.out.println("max = " + max);
    }

    @DisplayName("숫자 범위")
    @Test
    public void numberRange() {
        //given
        IntStream range = IntStream.range(1, 100);              //숫자 범위  1<  x <100
        IntStream rangeClosed = IntStream.rangeClosed(1, 100);  //숫자 범위  1<= x <=100

        //when
        List<Integer> evenNumbers = rangeClosed.filter(value -> value % 2 == 0)
                .boxed()
                .collect(toList());

        List<Integer> evenNumbers2 = range.filter(value -> value % 2 == 0)
                .boxed()
                .collect(toList());

        //then
        assertThat(evenNumbers.size()).isEqualTo(50);
        assertThat(evenNumbers2.size()).isEqualTo(49);
    }

    //    @DisplayName("피타고라스 수 ")
//    @Test
    public void t() {
        //given
        int a = 2;

//        Stream.of(IntStream.rangeClosed(1,100))
//                .filter(b-> Math.sqrt((a*a)+(b*b)) % 1 == 0)


        //when


        //then
    }

    @DisplayName("값으로 스트림 만들기")
    @Test
    public void createStreamWithValue() {
        //given
        Stream<String> stream = Stream.of("Modern", "Java", "In", "Action");
        Stream<Object> empty = Stream.empty();

        //when
        stream.map(String::toUpperCase).forEach(System.out::println);
    }

    @DisplayName("Null이 될 수 있는 객체로 스트림 만들기")
    @Test
    public void createNullableStream() {
        //given
        String homeValue = System.getProperty("home");
        Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(homeValue);
        Stream<String> homeValueStreamAdv = Stream.ofNullable(System.getProperty("home"));  //ofNullable을 활용한 nullable 객체 생성

        List<String> list = Stream.of("config", "home", "user")
                .flatMap(key -> Stream.ofNullable(System.getProperty(key))) //Stream 평면화
                .collect(toList());

        list.forEach(System.out::println);
    }

    @DisplayName("배열로 스트림 만들기")
    @Test
    public void createStreamWithArray() {
        //given
        int[] numbers = {2, 3, 5, 7, 11, 13};

        //when
        int sum = Arrays.stream(numbers).sum();


        //then
        assertThat(sum).isEqualTo(41);
    }

    @DisplayName("파일에서 고유한 단어 수 찾기")
    @Test
    public void findUniqueWordInFile() {
        //given
        long uniqueWords = 0;

        //when
        /**
         * Try-With-Resource 사용 -> try문 안에 있는 resource를 자동으로 해제해준다
         * try-with-resources에서 자동으로 close가 호출되는 것은 AutoCloseable을 구현한 객체에만 해당 됨(Stream의 상위인터페이스가 AutoCloseable를 상속받음)
         */
        try (Stream<String> lines = Files.lines(Paths.get(getPath()), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))   //고유 단어 수 계산
                    .distinct()
                    .count();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //then
        System.out.println("uniqueWords = " + uniqueWords);
    }

    private String getPath() {
        String path="C:\\Users\\magic\\Desktop\\me\\dev\\102.ModernJavaInAction\\practice\\src\\test\\java\\modernjavainaction\\practice\\data.txt";
        return path;
    }

    @DisplayName("함수로 무한 스트림 만들기")
    @Test
    public void makeInfStreamWithFunc() {
        //given
        Stream.iterate(0, n -> n + 2);


        //when


        //then
    }

}
