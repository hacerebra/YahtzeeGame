/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Game;

import Client.ScoreMessage;
import Client.ScoreMessage.Scores;
import javax.swing.JButton;

/**
 *
 * @author hacerebra
 */
public class Score {

    Scores score_type;
    JButton button;
    public boolean isButtonChoosen = false;

    public Score(Scores score_type, JButton button) {
        this.score_type = score_type;
        this.button = button;
    }

    public Scores getScore_type() {
        return score_type;
    }

    public void setScore_type(Scores score_type) {
        this.score_type = score_type;
    }

    public JButton getButton() {
        return button;
    }

    public static int SkorHesaplama(Zar zarlar[], ScoreMessage.Scores score_type) {
        int score = 0;
        switch (score_type) {
            case Birler:
                score = birler(zarlar);
                break;
            case Ikiler:
                score = ikiler(zarlar);
                break;
            case Ucler:
                score = ucler(zarlar);
                break;
            case Dortler:
                score = dortler(zarlar);
                break;
            case Besler:
                score = besler(zarlar);
                break;
            case Altilar:
                score = altilar(zarlar);
                break;
            case UcSet:
                score = ucSet(zarlar);
                break;
            case DortSet:
                score = dortSet(zarlar);
                break;
            case FullHouse:
                score = fullHouse(zarlar);
                break;
            case KucukKent:
                score = kucukKent(zarlar);
                break;
            case BuyukKent:
                score = buyukKent(zarlar);
                break;
            case Sans:
                score = sans(zarlar);
                break;
            case Yahtzee:
                score = yahtzee(zarlar);
                break;
        }
        return score;
    }

    public static int birler(Zar[] zarlar) {
        int result = 0;
        for (Zar d : zarlar) {
            if (d.value == 1) {
                result += 1;
            }
        }
        return result;
    }

    public static int ikiler(Zar[] zarlar) {
        int result = 0;
        for (Zar d : zarlar) {
            if (d.value == 2) {
                result += 2;
            }
        }
        return result;
    }

    public static int ucler(Zar[] zarlar) {
        int result = 0;
        for (Zar d : zarlar) {
            if (d.value == 3) {
                result += 3;
            }
        }
        return result;
    }

    public static int dortler(Zar[] zarlar) {
        int result = 0;
        for (Zar d : zarlar) {
            if (d.value == 4) {
                result += 4;
            }
        }
        return result;
    }

    public static int besler(Zar[] zarlar) {
        int result = 0;
        for (Zar d : zarlar) {
            if (d.value == 5) {
                result += 5;
            }
        }
        return result;
    }

    public static int altilar(Zar[] zarlar) {
        int result = 0;
        for (Zar d : zarlar) {
            if (d.value == 6) {
                result += 6;
            }
        }
        return result;
    }

    public static int ucSet(Zar[] zarlar) {
        int[] t = new int[6];
        for (Zar z : zarlar) {
            t[z.value - 1]++;
        }
        for (int i = 0; i < 6; i++) {
            if (t[i] >= 3) {
                int toplam = 0;
                for (Zar z : zarlar) {
                    toplam += z.value;
                }
                return toplam;
            }
        }
        return 0;
    }

    public static int dortSet(Zar[] zarlar) {
        int[] t = new int[6];
        for (Zar z : zarlar) {
            t[z.value - 1]++;
        }
        for (int i = 0; i < 6; i++) {
            if (t[i] >= 4) {
                int toplam = 0;
                for (Zar z : zarlar) {
                    toplam += z.value;
                }
                return toplam;
            }
        }
        return 0;
    }

    public static int fullHouse(Zar[] zarlar) {
        int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
        for (int i = 0; i < 5; i++) {
            if (zarlar[i].value == 1) {
                ones++;
            }
            if (zarlar[i].value == 2) {
                twos++;
            }
            if (zarlar[i].value == 3) {
                threes++;
            }
            if (zarlar[i].value == 4) {
                fours++;
            }
            if (zarlar[i].value == 5) {
                fives++;
            }
            if (zarlar[i].value == 6) {
                sixes++;
            }
        }
        if ((ones == 3 && (twos == 2 || threes == 2 || fours == 2 || fives == 2 || sixes == 2))
                || (twos == 3 && (ones == 2 || threes == 2 || fours == 2 || fives == 2 || sixes == 2))
                || (threes == 3 && (ones == 2 || twos == 2 || fours == 2 || fives == 2 || sixes == 2))
                || (fours == 3 && (ones == 2 || threes == 2 || twos == 2 || fives == 2 || sixes == 2))
                || (fives == 3 && (ones == 2 || threes == 2 || fours == 2 || twos == 2 || sixes == 2))
                || (sixes == 3 && (ones == 2 || threes == 2 || fours == 2 || fives == 2 || twos == 2))) {
            return 25;
        }
        return 0;
    }

    public static int kucukKent(Zar[] zarlar) {
        boolean[] values = new boolean[7]; // 1-6 arası değerler için

        for (Zar z : zarlar) {
            values[z.value] = true;
        }

        // Kontrol et: 1-2-3-4, 2-3-4-5, 3-4-5-6
        if ((values[1] && values[2] && values[3] && values[4])
                || (values[2] && values[3] && values[4] && values[5])
                || (values[3] && values[4] && values[5] && values[6])) {
            return 30;
        }
        return 0;
    }

    public static int buyukKent(Zar[] zarlar) {
        boolean[] values = new boolean[7];

        for (Zar z : zarlar) {
            values[z.value] = true;
        }

        // Kontrol et: 1-2-3-4-5 veya 2-3-4-5-6
        if ((values[1] && values[2] && values[3] && values[4] && values[5])
                || (values[2] && values[3] && values[4] && values[5] && values[6])) {
            return 40;
        }
        return 0;
    }

    public static int sans(Zar[] zarlar) {
        int result = 0;
        for (Zar d : zarlar) {
            result += d.value;
        }
        return result;
    }

    public static int yahtzee(Zar[] zarlar) {
        int ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
        for (int i = 0; i < zarlar.length; i++) {
            if (zarlar[i].value == 1) {
                ones++;
            }
            if (zarlar[i].value == 2) {
                twos++;
            }
            if (zarlar[i].value == 3) {
                threes++;
            }
            if (zarlar[i].value == 4) {
                fours++;
            }
            if (zarlar[i].value == 5) {
                fives++;
            }
            if (zarlar[i].value == 6) {
                sixes++;
            }
        }
        if (ones == 5 || twos == 5 || threes == 5 || fours == 5 || fives == 5 || sixes == 5) {
            return 50;
        }
        return 0;
    }
}
