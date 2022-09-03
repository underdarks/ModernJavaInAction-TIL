package modernjavainaction.practice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {
    private final Trader trader;
    private final int year;
    private final int value;

    @Override
    public String toString() {
        return "{" +
                "trader=" + trader +
                ", year=" + year +
                ", value=" + value +
                '}';
    }


}
