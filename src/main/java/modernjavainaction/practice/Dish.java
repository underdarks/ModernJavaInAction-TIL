package modernjavainaction.practice;

import lombok.*;

import static lombok.AccessLevel.*;

@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
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

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                '}';
    }
}
