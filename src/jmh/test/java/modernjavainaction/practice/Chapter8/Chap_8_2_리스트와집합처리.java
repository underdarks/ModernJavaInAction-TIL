package modernjavainaction.practice.Chapter8;

import modernjavainaction.practice.Other.DataManager;
import modernjavainaction.practice.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class Chap_8_2_리스트와집합처리 {

    /**
     * - removeIf   -> 프레디케이트를 만족하는 요소를 제거한다. List나 Set을 구현하거나 그 구현을 상속받은 모든 클래스에서 이용할 수 있다.
     * - replaceAll -> 요소 변경
     * - sort       -> 리스트 정렬
     *
     * 이들 메서드는 호출한 컬렉션 자체를 빠군다. 새로운 결과를 만드는 스트림 동작과 달리 이들 메서드는 기존 컬렉션을 바꾼다.
     * 이러한 기존 컬렉션을 바꾸는 동작은 에러를 유발하며 복잡도를 증가한다. 왜 추가했을까?
     **/


    /**
     * 8.2.1 removeIf 메서드
     */
    @DisplayName("removeIf 메서드 적용하기 전 요소 삭제")
    @Test
    public void notUseremoveIf() {
        //given
        List<Transaction> transactions = DataManager.getTransactionData();

        /**
         * ConcurrentModificationException이 발생해야 하는데 UnsupportedOperationException가 계속 발생하네.
         *  => 이유 찾음! Arrays.asList로 만든 List에는 새로운 원소를 추가 삭제 할 수 없다.. 그래서 발생( 참고 : https://m.blog.naver.com/roropoly1/221140156345)
         *  => return Arrays.asList에서 -> return new ArrayList<>(Arrays.asList(...)) 으로 변경
         *
         * 반복자의 상태와 컬렉션의 상태가 동기화되지 않아서 발생하는 문제
         */
        //when
        assertThrows(ConcurrentModificationException.class, () -> {
                    for (Transaction transaction : transactions) {
                        if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
                            transactions.remove(transaction);}
                    }
        });

        //removeIf 적용
        transactions.removeIf(transaction -> Character.isDigit(transaction.getReferenceCode().charAt(0)));

    }

    /**
     * 8.2.1 removeIf 메서드
     */
    @DisplayName("removeIf 메서드 적용")
    @Test
    public void remzoveIf() {
        //given
        List<Transaction> transactions = DataManager.getTransactionData();

        //when
        //removeIf 적용
        transactions.removeIf(transaction -> Character.isDigit(transaction.getReferenceCode().charAt(0)));

        //then
        transactions.forEach(System.out::println);
   }

   @DisplayName("replaceAll 메서드")
   @Test
   public void useReplaceAllMethod(){
       //given
       List<String> referenceCodes=Arrays.asList("a12","C14","b13");

       //when
       /**
        * map을 이용하여 변환하더라고 기존 컬렉션 데이터는 변경이 발생하지 않게 됨 
        */
       referenceCodes.stream()
               .map(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1))
               .collect(toList())
               .forEach(System.out::println);

       System.out.println(" ================ ");
       referenceCodes.forEach(System.out::println);

       /**
        * 그러나 replaceAll을 사용하면 기존 컬렉션 데이터를 변경할 수 있다.
        */
       referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));


       System.out.println(" ================ ");
       //then
       referenceCodes.forEach(System.out::println);
   }
}
