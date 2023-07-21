
import java.io.*;
import java.util.Scanner;

public class VendingMachine {

    public static void vend(int itemNo, User user, String choice, Urun urun){
        Scanner scanner = new Scanner(System.in);
        String dosyaYolu;
        if(choice.equals("A")){
            dosyaYolu = "drinks.txt";
        }
        else{
            dosyaYolu = "snacks.txt";
        }
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
            String satir;

            while ((satir = br.readLine()) != null) {
                String[] veri = satir.split(",");
                String urunNumarasi = veri[0];
                int urunNo = Integer.parseInt(urunNumarasi);
                double urunFiyati = Double.parseDouble(veri[2]);
                int stokMiktari = Integer.parseInt(veri[3]);

                urun.price = urunFiyati;
                urun.quantity = stokMiktari;
                urun.description = veri[1];

                if(user.moneyAmount >=urunFiyati && stokMiktari>0 && itemNo == urunNo){
                    changeStock(itemNo, choice);
                    user.moneyAmount = user.moneyAmount - urunFiyati;
                    user.choicesList.add(veri[1]);
                    System.out.println("11");
                    outputMessage("Alışveriş başarılı", user, null);
                }

                else if(stokMiktari==0 && itemNo == urunNo){
                    outputMessage("Stok 0", null,null);

                    String yn = scanner.nextLine();
                    if(yn.equals("Y")){
                        VendingMachineDriver.driver(user);
                    }
                    else if(yn.equals("N")){

                        outputMessage("Kapanış mesajı", user, null);
                        System.exit(0);

                    }
                    //BAŞKA BİR İNPUT GELİRSE?
                }
                else if (itemNo == urunNo){
                    System.out.println("Yetersiz Bakiye ("+user.moneyAmount+")");
                    System.out.println("Bu ürünü alabilmeniz için minimum "+(urunFiyati-user.moneyAmount)+" TL daha eklemelisiniz.");
                    System.out.println("Para girişi yapınız. (Çıkış yapmak için:-1 Ana menüye dönüş için:-2 tuşlayınız");

                    Double input = scanner.nextDouble();

                    if(input == -1){
                        outputMessage("Kapanış mesajı", user, null);
                        System.exit(0);
                    }
                    else if(input == -2){
                        VendingMachineDriver.driver(user);
                    }
                    else{
                        user.moneyAmount += input;
                        vend(itemNo,user, choice, urun);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    public static void changeStock(int itemNo, String choice){
        String dosyaYolu;
        String geciciDosyaYolu = "gecici_stock.txt";
        if(choice.equals("A")){
            dosyaYolu = "drinks.txt";
        }
        else{
            dosyaYolu = "snacks.txt";
        }
        Reader.changeLineStock(dosyaYolu, geciciDosyaYolu, itemNo, -1);
    }
    public static String outputMessage(String request, User user, Urun urun) {

        if(request.equals("Kapanış mesajı")){

            int size = user.choicesList.size();

            if(size == 0){
                System.out.println("Herhangi bir ürün alınmadı. Hoşçakalın. Para üstünüz:"+user.moneyAmount + " ₺");
                System.exit(0);
            }
            System.out.print("Yapılan alışveriş (sırasıyla) -> ");
            for(int i=0; i<size;i++){
                if(i == size-1){
                    System.out.println(user.choicesList.get(i));
                }
                else{
                    System.out.print(user.choicesList.get(i)  + " - ");
                }
            }
            System.out.println("Hoşçakalın. Para üstünüz:"+user.moneyAmount + " ₺");
        }

        else if(request.equals("Merhaba")){
            System.out.println("Merhaba, Ankara Otomat'a hoş geldiniz!");
        }
        else if(request.equals("Açılış mesajı")){
            System.out.println("Lütfen seçiniz:");
            System.out.println("A-Drinks B-Snacks X-Exit");
        }

        else if(request.equals("Hatalı tuşlama")){
            System.out.println("Hatalı tuşlama yaptınız tekrar deneyiniz.");
        }

        else if (request.equals("Numara tuşla")){
            System.out.println("");
            System.out.println("Almak istediğiniz ürünün numarasını tuşlayınız.");
        }

        else if(request.equals("Devam?")){
            System.out.println("Alışverişinize devam etmek istiyor musunuz? Y-N");
        }

        else if(request.equals("Stok 0")){
            System.out.println("Bu ürünün stoğu malesef mevcut değil. Ana menüye dönüp devam etmek ister misiniz? (Y-N)");
        }
        else if(request.equals("Alışveriş başarılı")){
            System.out.println("Alışveriş başarılı! Afiyet olsun.");
            System.out.println("Kalan bakiye: "+user.moneyAmount + "₺");
        }

        return null;
    }
    public static void printMenu(String choice){
        String dosyaYolu;
        if(choice.equals("A")){
            dosyaYolu = "drinks.txt";
        }
        else{
            dosyaYolu = "snacks.txt";
        }
        System.out.println("İşte güncel menü:");
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu))) {
            String satir;
            System.out.print("Item#");
            System.out.print(" "+"Name");
            System.out.print("                      "+"Price");


            System.out.println(" "+"Quantity");
            while ((satir = br.readLine()) != null) {
                String[] veri = satir.split(",");
                String urunNo = veri[0];
                String urunAdi = veri[1];
                double urunFiyati = Double.parseDouble(veri[2]);
                int stokMiktari = Integer.parseInt(veri[3]);
                int size = 25 - urunAdi.length();
                System.out.print(urunNo);
                System.out.print("     "+urunAdi);
                for(int i=0; i<size;i++){
                    System.out.print(" ");
                }
                System.out.print(" "+urunFiyati);
                if(urunFiyati<10){
                    System.out.print(" ");
                }
                System.out.print("\t");
                System.out.println("  "+stokMiktari);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
