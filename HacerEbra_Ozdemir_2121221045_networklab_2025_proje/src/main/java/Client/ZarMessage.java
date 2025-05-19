/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

/**
 *
 * @author hacerebra
 */
// Oyuncunun attığı zarların değerlerini taşımak için kullanılır
public class ZarMessage implements java.io.Serializable {

    public int[] zarlar; // Zarların değerlerini tutan tamsayı dizisi

    public ZarMessage(int[] zarlar) {
        this.zarlar = zarlar;
    }
}
