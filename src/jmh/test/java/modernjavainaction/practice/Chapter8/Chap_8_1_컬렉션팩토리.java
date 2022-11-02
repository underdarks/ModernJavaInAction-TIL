package modernjavainaction.practice.Chapter8;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class Chap_8_1_컬렉션팩토리 {


    private final String[] values={"Raphael", "Oliva", "Thibaut"};

    /**
     * 8.1 컬렉션 팩토리 (p.276)
     *
     */
    @DisplayName("고정 크기의 리스트 생성")
    @Test
    public void fixedSizeList(){
        //given
        List<String> friends = Arrays.asList(values);    //고정 크기의 리스트 생성

        //고정 크기의 리스트를 만들었으므로 새 요소 추가하려 하면 UnsupportedOperationException 발생
        //when
        assertThrows(UnsupportedOperationException.class,() ->{
            friends.set(0,"Richard");
            friends.add("Thibaut");
        });

        //then
    }

    @DisplayName("고정 크기의 리스트 삭제 테스트")
    @Test
    public void deleteFixedSizeList(){
        //given
        List<String> friends = Arrays.asList(values);    //고정 크기의 리스트 생성

        //고정 크기의 리스트를 만들었으므로 새 요소 추가하려 하면 UnsupportedOperationException 발생
        //when
        assertThrows(UnsupportedOperationException.class,() ->{
            for (String friend : friends) {
                friends.remove(friend);
            }

        });

        //then
    }

    @DisplayName("불필요한 객체 할당으로 데이터 생성")
    @Test
    public void unnecessaryObjectAllocation(){
        //given
        HashSet<String> friends = new HashSet<>(Arrays.asList(values));

        Set<String> friends2 = Stream.of(values)
                .collect(Collectors.toSet());

    }

    /**
     * 8.1.1 리스트 팩토리 (p.279)
     */
    @DisplayName("List.of 팩토리 메소드를 이용한 리스트생성")
    @Test
    public void makeListWithListOf(){
        //given
        List<String> friends = List.of(this.values);
        System.out.println("friends = " + friends);

        //when
        /**
         *  List.of 또한 UnsupportedOperationException 발생, 변경할 수 없는(Immutable) 리스트 생성하였기 때문에
         */
        assertThrows(UnsupportedOperationException.class, () ->{
            friends.add("Chic-Chun");
        });
   }

    /**
     * 8.1.2 집합 팩토리 (p.279)
     */
    @DisplayName("Set.of 팩토리 메서드를 활용한 집합 생성")
    @Test
    public void makeSetWithSetof(){
        //given
        /**
         * 중복된 요소가 있는 데이터로 Set을 만드려고 하면 IllegalArgumentException 발생
         * Set은 중복을 허용하지 않는 자료구조이기 때문
         */
        assertThrows(IllegalArgumentException.class,()->{
            Set<String> friends = Set.of("Olivia","Olivia","Raphael");
        });
    }

    /**
     * 8.1.3 맵 팩토리 (p.279)
     */
    @DisplayName("Map.of 팩토리 메서드를 활용한 맵 팩토리")
    @Test
    public void mapFactoryWithMapOf(){
        /**
         * Map.of는 열개 이하의 키와 쌍을 가진 맵을 만들 수 있음.(10개 초과는 안됨)
         * 따라서, 10개 초과의 경우 Map.ofEntires 팩토리 메서드를 이용하는것이 좋음
         */
        //given
        Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 25);

        Map<String, Integer> ofEntry = Map.ofEntries(
                entry("Raphael", 30),
                entry("Olivia", 25),
                entry("Thibaut", 35)
        );
    }

    /**
     *  퀴즈 8-1 (p.280)
     */
    @DisplayName("퀴즈 8-1")
    @Test
    public void quiz_8_1(){
        //given
        List<String> actors = List.of("Kenau", "Jessica");

        /**
         * List.of는 ImmutableCollections(불변)을 반환하므로 리스트에 요소 추가시 예외 발생
         */
        //then
        assertThrows(UnsupportedOperationException.class,() ->{
            actors.set(0,"Brad");
        });
    }
}
