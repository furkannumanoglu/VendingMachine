import java.io.*;

public class Reader {
    public static void changeLineStock(String dosyaYolu, String geciciDosyaYolu, int itemNo, int changeAmount){//changes stock
        try (BufferedReader br = new BufferedReader(new FileReader(dosyaYolu));
             BufferedWriter bw = new BufferedWriter(new FileWriter(geciciDosyaYolu))) {
            String satir;
            while ((satir = br.readLine()) != null) {
                String[] veri = satir.split(",");
                String urunNo = veri[0];
                int no = Integer.parseInt(urunNo);
                String urunAdi = veri[1];
                Double urunFiyati = Double.parseDouble(veri[2]);
                int stokMiktari = Integer.parseInt(veri[3]);

                if(itemNo == no && stokMiktari + changeAmount >= 0){
                    stokMiktari+=changeAmount;
                }
                String guncellenmisSatir = urunNo + "," + urunAdi + "," + urunFiyati + "," + stokMiktari;
                bw.write(guncellenmisSatir);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        changeFile(dosyaYolu, geciciDosyaYolu);
    }

    public static void changeFile(String dosyaYolu, String geciciDosyaYolu){//changes files
        File eskiDosya = new File(dosyaYolu);
        File geciciDosya = new File(geciciDosyaYolu);
        if (eskiDosya.delete()) {
            geciciDosya.renameTo(eskiDosya);
        }
    }



}
