package modernjavainaction.practice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor
public class Dish {
    private String name;
    private boolean vegetarian;
    private int calories;
    private FoodType foodType;



    public enum FoodType {
        MEAT,
        FISH,
        OTHER
    }
}
