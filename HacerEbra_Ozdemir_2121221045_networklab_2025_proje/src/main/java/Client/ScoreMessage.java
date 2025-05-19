/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

/**
 *
 * @author hacerebra
 */
public class ScoreMessage implements java.io.Serializable {

    public Scores score_type; // Puanın ait olduğu kategori 
    public int content; // Kategoriye karşılık gelen puan değeri

    // Puan kategorilerini temsil eden enum
    public static enum Scores {
        Birler, // Zar değeri 1 olanların toplamı
        Ikiler, // Zar değeri 2 olanların toplamı
        Ucler, // Zar değeri 3 olanların toplamı
        Dortler, // Zar değeri 4 olanların toplamı
        Besler, // Zar değeri 5 olanların toplamı
        Altilar, // Zar değeri 6 olanların toplamı
        UcSet, // En az üç aynı zar
        DortSet, // En az dört aynı zar
        FullHouse, // Üçlü ve ikili kombinasyonu (örnek: 3-3-3-5-5)
        KucukKent, // Dört zarın sıralı dizilimi (örnek: 1-2-3-4)
        BuyukKent, // Beş zarın sıralı dizilimi (örnek: 2-3-4-5-6)
        Sans, // Zarların toplamı (joker puan)
        Yahtzee // Beş zarın da aynı olması (örnek: 6-6-6-6-6)
    }

    public ScoreMessage(Scores score) {
        this.score_type = score;
    }

}
