/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

/**
 *
 * @author hacerebra
 */
public class Message implements java.io.Serializable {

    // Farklı mesaj türlerini temsil eden enum (mesajın ne tür bilgi taşıdığını belirtir)
    public static enum Message_Type {
        Ad, // Oyuncunun kullanıcı adını taşıyan mesaj
        RakipBaglanti, // Rakiple bağlantı kurulduğunu bildiren mesaj
        TurDegis, // Tur değişimini bildiren mesaj
        Kontrol, // Oyuncunun sırası mı değil mi kontrolü
        AraToplam, // Ara toplam puan bilgisini ileten mesaj
        Bitis, // Oyunun bittiğini belirten mesaj
        Zarlar, // Zar değerlerini içeren mesaj
        Kazanma, // Oyunun kazananını belirten mesaj
        YeniOyun, // Yeni oyun başlatıldığını bildiren mesaj
        BaglantiKoptu, // Rakibin bağlantısı koptuğunda gönderilen mesaj
    }

    // Mesajın tipi
    public Message_Type type;

    // Mesajın içeriği
    public Object content;

    public Message(Message_Type t) {
        this.type = t;
    }

}
