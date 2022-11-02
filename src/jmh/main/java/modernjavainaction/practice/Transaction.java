package modernjavainaction.practice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Transaction {
    private Trader trader;
    private int year;
    private int value;
    private String referenceCode;





}
