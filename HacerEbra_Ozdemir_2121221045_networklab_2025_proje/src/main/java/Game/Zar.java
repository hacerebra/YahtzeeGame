/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author hacerebra
 */
public class Zar {

    public JLabel label;
    int value; // Zarın mevcut sayısal değeri (1-6 arası)
    private static int WIDTH = 110, HEIGHT = 110;

    public Zar(JLabel label, int value) {
        this.label = label;
        this.value = value;
        this.label.setIcon(getImageIcon(this.value));
    }

    private static final Random RANDOM = new Random();

    // Zar değerini rastgele değiştirir (1-6 arasında) ve görseli günceller
    public void shuffle() {
        this.value = RANDOM.nextInt(6) + 1;
        this.label.setIcon(getImageIcon(this.value));
    }

    // Zarın JLabel bileşenini döner
    public JLabel getLabel() {
        return this.label;
    }

    // Zarın mevcut sayısal değerini döner
    public int getValue() {
        return this.value;
    }

    // Verilen değere uygun zar görselini ImageIcon olarak döner
    public static ImageIcon getImageIcon(int value) {
        String zarPath = "src/main/java/images/dice0" + value + ".png";
        ImageIcon imgIcon = new ImageIcon(zarPath); // ImageIcon oluşturulur
        Image olcek = imgIcon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        return new ImageIcon(olcek); // Ölçeklenmiş görsel yeni ImageIcon olarak döner

    }
}
