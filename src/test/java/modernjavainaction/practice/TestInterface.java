package modernjavainaction.practice;

public interface TestInterface {

    int a=1;          //일반 멤버 변수를 가질 수 없음

    default void funcA() {   //일반 메서드 구현을 할수 할 수 없음
        System.out.println("a = " + a);
    }

}
