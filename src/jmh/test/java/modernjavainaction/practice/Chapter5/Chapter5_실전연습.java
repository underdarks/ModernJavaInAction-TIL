package modernjavainaction.practice.Chapter5;

import modernjavainaction.practice.Trader;
import modernjavainaction.practice.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

public class Chapter5_실전연습 {

    private final String CAMBRIDGE="Cambridge";
    private final String MILIAN="Milan";



    @DisplayName("실전 엽습 데이터 생성")
    @Test
    public List<Transaction> createTransactionData(){
        Trader raoul = new Trader("Raoul", CAMBRIDGE);
        Trader mario = new Trader("Mario", MILIAN);
        Trader alan = new Trader("Alan", CAMBRIDGE);
        Trader brian = new Trader("Brian", CAMBRIDGE);

        return Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );
    }


    @DisplayName("실전연습 1번 - 20111년에 일어난 모든 트랙잭션을 찾아 값을 오름차순으로 정리하시오")
    @Test
    public void quiz1(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        List<Transaction> tr2011 = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(toList());

        tr2011.forEach(System.out::println);

        //then
        assertThat(tr2011.size()).isEqualTo(2);
    }

    @DisplayName("실전연습 2번 - 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.")
    @Test
    public void quiz2(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        //distinct()와 List를 활용한 중복 제거
        List<String> cities = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .distinct()
                .collect(toList());

        cities.forEach(System.out::println);

        //Set을 이용하여 중복 제거 방식
//        //when
//        Set<String> cities2 = transactions.stream()
//                .map(transaction -> transaction.getTrader().getCity())
//                .collect(toSet());
//
//        cities2.forEach(System.out::println);

        //then
        assertThat(cities.size()).isEqualTo(2);
    }

    @DisplayName("실전연습 3번 - 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.")
    @Test
    public void quiz3(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        List<Trader> traders = transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals(CAMBRIDGE))
                .map(Transaction::getTrader)
                .distinct()
                .sorted(comparing(Trader::getName).reversed())   //내림차순 정렬
                .collect(toList());

        //then
        traders.forEach(System.out::println);
        assertThat(traders.get(0).getName()).isEqualTo("Raoul");

    }

    @DisplayName("실전연습 4번 - 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.")
    @Test
    public void quiz4(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        List<String> names = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .sorted()
                .collect(toList());

        names.forEach(System.out::println);

        //then
        assertThat(names.get(0)).isEqualTo("Alan");
    }

    @DisplayName("실전연습 5번 - 밀라노에 거래자가 있는지?")
    @Test
    public void quiz5(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        boolean isMilanoTrader = transactions.stream()
                .anyMatch(transaction -> transaction.getTrader().getCity().equals(MILIAN));

        //then
        assertThat(isMilanoTrader).isEqualTo(true);
    }

    @DisplayName("실전연습 6번 - 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.")
    @Test
    public void quiz6(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        List<Integer> values = transactions.stream()
                .filter(transaction -> transaction.getTrader().getCity().equals(CAMBRIDGE))
                .map(Transaction::getValue)
                .collect(toList());

        //then
        values.forEach(System.out::println);
    }


    @DisplayName("실전연습 7번 - 전체 트랜잭션 중 최대값은 얼마인가?")
    @Test
    public void quiz7(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        //reduce를 활용한 max값 구하기
        Integer max = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Math::max)
                .orElse(0);


        //max()를 활용한 최대값
//        Integer max2 = transactions.stream()
//                .max(comparing(Transaction::getValue))
//                .map(Transaction::getValue)
//                .orElse(0);
//
//        System.out.println("max2 = " + max2);

        //then
        assertThat(max).isEqualTo(1000);
    }

    @DisplayName("실전연습 8번 - 전체 트랜잭션 중 최소값은 얼마인가?")
    @Test
    public void quiz8(){
        //given
        List<Transaction> transactions = createTransactionData();

        //when
        //reduce를 활용한 최소값 구하기
        Integer min = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Math::min)
                .orElse(0);

        //min()을 활용한 최소값 구하기
//        Integer min2 = transactions.stream()
//                .min(comparing(Transaction::getValue))
//                .map(Transaction::getValue)
//                .orElse(0);
//
//        System.out.println("min2 = " + min2);

        //then
        assertThat(min).isEqualTo(300);
    }

}
