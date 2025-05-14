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

    public Scores score_type;
    public int content;

    // score tipleri enum 
    public static enum Scores {
        Birler, Ikiler, Ucler, Dortler, Besler, Altilar, UcSet, DortSet, FullHouse, KucukKent, BuyukKent, Sans, Yahtzee
    }

    public ScoreMessage(Scores score) {
        this.score_type = score;
    }

}
