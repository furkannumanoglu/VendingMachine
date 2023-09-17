import java.util.ArrayList;

public class Consumer extends User{
    Double moneyAmount;
    public Consumer(double money){
        super("Consumer","Consumer");
        this.moneyAmount = money;
        choicesList = new ArrayList<>();
    }
}
