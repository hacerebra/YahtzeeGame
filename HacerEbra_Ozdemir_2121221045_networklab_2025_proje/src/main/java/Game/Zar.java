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
    int value;
    private static int WIDTH = 110, HEIGHT = 110;

    public Zar(JLabel label, int value) {
        this.label = label;
        this.value = value;
        this.label.setIcon(getImageIcon(this.value));
    }

    public void shuffle() {
        this.value = new Random().nextInt(6) + 1;
        this.label.setIcon(getImageIcon(this.value));
    }

    public JLabel getLabel() {
        return this.label;
    }

    public int getValue() {
        return this.value;
    }

    public static ImageIcon getImageIcon(int value) {
        String sourcePath = "src/main/java/images/dice0" + value + ".png";
        ImageIcon imgIcon = new ImageIcon(sourcePath);
        Image scale = imgIcon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT);
        return new ImageIcon(scale);

    }
}
