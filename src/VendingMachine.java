
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VendingMachine {
    public static void vend(int itemNo, User user, String choice, Urun urun) throws InterruptedException {
//urun gereksiz
        Scanner scanner = new Scanner(System.in);
        String dosyaYolu = null;

        if(choice.equals("A")){
            dosyaYolu = "drinks.txt";
        }
        else if(choice.equals("B")){
            dosyaYolu = "snacks.txt";
        }
        else{
            outputMessage("Hatalı tuşlama", user, urun);
            vend(itemNo, user, choice, urun);
            System.exit(0);
        }

        ArrayList<Urun> urunList = txtToArraylist(dosyaYolu); //ürünleri .txt dosyasından çekildi, urunList olarak tutuluyor.
        //kullanım kolaylığı açısından yapıldı.
        if(itemNo > urunList.size() || itemNo<=0){
            outputMessage("Menüde yok", user, urun);
            Thread.sleep(2000);
            VendingMachineDriver.driver(user);
            System.exit(0);
        }

        int i=0;
        while (i<urunList.size()) {
            Urun tmp_urun = urunList.get(i);

            if(user.moneyAmount >=tmp_urun.price && tmp_urun.quantity>0 && itemNo == tmp_urun.no){
                changeStock(itemNo, choice, tmp_urun);
                user.moneyAmount = user.moneyAmount - tmp_urun.price;
                user.choicesList.add(tmp_urun);
                outputMessage("Alışveriş başarılı", user, tmp_urun);
                break;
            }
            else if(tmp_urun.quantity==0 && itemNo == tmp_urun.no){
                boolean isOk = false;
                outputMessage("Stok 0", null,null);
                while(!isOk){
                    String yn = scanner.nextLine();
                    if(yn.equals("Y")){
                        VendingMachineDriver.driver(user);
                        isOk = true;
                    }
                    else if(yn.equals("N")){
                        outputMessage("Kapanış mesajı", user, null);
                        System.exit(0);
                    }
                    else{
                        VendingMachine.outputMessage("Hatalı tuşlama", null, null);
                    }

                }
            }
            else if (itemNo == tmp_urun.no){
                outputMessage("Yetersiz bakiye", user, tmp_urun);
                Double input = scanner.nextDouble();

                if(input == -1){
                    outputMessage("Kapanış mesajı", user, null);

                }
                else if(input == -2){
                    VendingMachineDriver.driver(user);

                }
                else{
                    user.moneyAmount += input;
                    vend(itemNo,user, choice, urun);

                }
                break;
            }
            i++;
        }
    }

    public static void changeStock(int itemNo, String choice, Urun urun){
        String dosyaYolu;
        String geciciDosyaYolu = "gecici_stock.txt";
        if(choice.equals("A")){
            dosyaYolu = "drinks.txt";
        }
        else{
            dosyaYolu = "snacks.txt";
        }
        urun.quantity--;
        Reader.changeLineStock(dosyaYolu, geciciDosyaYolu, itemNo, -1);
    }
    public static void outputMessage(String request, User user, Urun urun) {

        if(request.equals("Kapanış mesajı")){
            int size = user.choicesList.size();
            if(size == 0){
                System.out.println("Herhangi bir ürün alınmadı. Hoşçakalın. Para üstünüz:"+user.moneyAmount + " ₺");
                System.exit(0);
            }
            System.out.print("Yapılan alışveriş (sırasıyla): ");
            for(int i=0; i<size;i++){
                if(i == size-1){
                    System.out.println(user.choicesList.get(i).description);
                }
                else{
                    System.out.print(user.choicesList.get(i).description  + " -> ");
                }
            }
            System.out.println("Hoşçakalın. Para üstünüz:"+user.moneyAmount + " ₺");
        }
        else if(request.equals("Menüde yok")){
            System.out.println("Seçtiğiniz ürün numarası menüde bulunmuyor");
            System.out.println("Ana menüye yönlendiriliyorsunuz...");
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
        else if(request.equals("Yetersiz bakiye")){
            System.out.println("Yetersiz Bakiye ("+user.moneyAmount+")");
            System.out.println("Bu ürünü alabilmeniz için minimum "+(urun.price-user.moneyAmount)+" TL daha eklemelisiniz.");
            System.out.println("Para girişi yapınız. (Çıkış yapmak için:-1 Ana menüye dönüş için:-2 tuşlayınız");

        }

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
    public static ArrayList<Urun> txtToArraylist(String dosya){
        ArrayList<Urun> urunList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dosya))) {
            String satir;

            while ((satir = br.readLine()) != null) {
                String[] veri = satir.split(",");
                String urunNumarasi = veri[0];
                String description = veri[1];
                int urunNo = Integer.parseInt(urunNumarasi);
                double urunFiyati = Double.parseDouble(veri[2]);
                int stokMiktari = Integer.parseInt(veri[3]);

                Urun urun = new Urun(urunNo, description, urunFiyati, stokMiktari);
                urunList.add(urun);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return urunList;
    }
}


