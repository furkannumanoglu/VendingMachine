import java.io.*;
import java.util.Scanner;

public class VendingMachineDriver {
    public static void main(String[] args) throws IOException, InterruptedException {
        User user = new User(0);
        VendingMachine.outputMessage("Merhaba", user, null);
        driver(user);
    }
    public static void driver(User user) throws InterruptedException {
        VendingMachine.outputMessage("Açılış mesajı", user, null);
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(input.equals("X")){ //exit
            VendingMachine.outputMessage("Kapanış mesajı", user, null);
            System.exit(0);
        }
        else if(input.equals("A") || input.equals("B")){ //A:drinks B:snacks
            VendingMachine.printMenu(input);
        }
        else{
            VendingMachine.outputMessage("Hatalı tuşlama", user, null);
            driver(user);
        }
        VendingMachine.outputMessage("Numara tuşla", user, null);

        int intInput = scanner.nextInt();

        VendingMachine.vend(intInput, user, input, null);
        VendingMachine.outputMessage("Devam?", user, null);
        scanner.nextLine();
        String isContinue = scanner.nextLine();

        while(true){
            if(!isContinue.equals("Y") && !isContinue.equals("N")){
                VendingMachine.outputMessage("Hatalı tuşlama", user, null);
                System.out.println("Lütfen 'Y' ya da 'N' tuşlayınız.");
                isContinue = scanner.nextLine();
            }
            else{
                break;
            }
        }

        if(isContinue.equals("Y")){ //Y means "Yes"
            driver(user);
        }
        else{
            VendingMachine.outputMessage("Kapanış mesajı", user, null);
        }

        System.exit(0);
    }
}

