/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package game_start;

import Client.Client;
import Client.Message;
import Client.ScoreMessage;
import Client.ScoreMessage.Scores;
import Client.ZarMessage;
import Game.Score;
import Game.Zar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author hacerebra
 */
public class Game extends javax.swing.JFrame {

    public static Game thisGame;

    public boolean finishState = false; // Oyun bitiş durumu
    public int sıra; // Oyuncu sırasını belirten kontrol (1 olan başlar)
    // Oyuncu ve rakibin skorlarını tutan listeler
    public static ArrayList<Score> skorlar;
    public static ArrayList<Score> rakipSkorlar;
    public Zar zarlar[] = new Zar[5]; // 5 zar nesnesi
    int rollCount = 0; // Aynı tur içinde kaç kez zar atıldığını tutar

    public Game() {
        initComponents();
        skorlar = new ArrayList<Score>();
        rakipSkorlar = new ArrayList<Score>();
        thisGame = this;
        btn_cikis.setVisible(false);
        btn_yeni.setEnabled(false);
        initScores();
        disableRivalButtons(false); // Rakip butonları devre dışı
        zarlar[0] = new Zar(dice01lbl, 1);
        zarlar[1] = new Zar(dice02lbl, 2);
        zarlar[2] = new Zar(dice03lbl, 3);
        zarlar[3] = new Zar(dice04lbl, 4);
        zarlar[4] = new Zar(dice05lbl, 5);
        revalidate();
    }

    public void setOyuncuAdi(String oyuncuAdi) {
        oyuncu1_lbl.setText("Sen (" + oyuncuAdi + ")");
        oyuncu2_lbl.setText("Rakip");

        // Zar ve butonları devre dışı bırak
        zarat_btn.setEnabled(false);
        z1.setEnabled(false);
        z2.setEnabled(false);
        z3.setEnabled(false);
        z4.setEnabled(false);
        z5.setEnabled(false);
    }

    // Belirli bir skor türüne karşılık gelen kendi butonunu döndür
    public Score getMyButton(Scores score_type) {
        for (Score myPoint : skorlar) {
            if (myPoint.getScore_type() == score_type) {
                return myPoint;
            }
        }
        return null;
    }

    // Rakibin belirli skor türüne karşılık gelen butonunu döndür
    public JButton getRivalButton(Scores score_type) {
        // Verilen score türüne göre rakibin butonuna erişmek için
        for (Score rivalPoint : rakipSkorlar) {
            if (rivalPoint.getScore_type() == score_type) {
                return rivalPoint.getButton();
            }
        }
        return null;
    }

    // Skor butonlarını başlatır
    public void initScores() {

        // Oyuncu 1 skorları
        skorlar.add(new Score(Scores.Birler, o1_1));
        skorlar.add(new Score(Scores.Ikiler, o1_2));
        skorlar.add(new Score(Scores.Ucler, o1_3));
        skorlar.add(new Score(Scores.Dortler, o1_4));
        skorlar.add(new Score(Scores.Besler, o1_5));
        skorlar.add(new Score(Scores.Altilar, o1_6));
        skorlar.add(new Score(Scores.UcSet, o1_7));
        skorlar.add(new Score(Scores.DortSet, o1_8));
        skorlar.add(new Score(Scores.FullHouse, o1_9));
        skorlar.add(new Score(Scores.KucukKent, o1_10));
        skorlar.add(new Score(Scores.BuyukKent, o1_11));
        skorlar.add(new Score(Scores.Sans, o1_12));
        skorlar.add(new Score(Scores.Yahtzee, o1_13));

        // Oyuncu 2 skorları
        rakipSkorlar.add(new Score(Scores.Birler, o2_1));
        rakipSkorlar.add(new Score(Scores.Ikiler, o2_2));
        rakipSkorlar.add(new Score(Scores.Ucler, o2_3));
        rakipSkorlar.add(new Score(Scores.Dortler, o2_4));
        rakipSkorlar.add(new Score(Scores.Besler, o2_5));
        rakipSkorlar.add(new Score(Scores.Altilar, o2_6));
        rakipSkorlar.add(new Score(Scores.UcSet, o2_7));
        rakipSkorlar.add(new Score(Scores.DortSet, o2_8));
        rakipSkorlar.add(new Score(Scores.FullHouse, o2_9));
        rakipSkorlar.add(new Score(Scores.KucukKent, o2_10));
        rakipSkorlar.add(new Score(Scores.BuyukKent, o2_11));
        rakipSkorlar.add(new Score(Scores.Sans, o2_12));
        rakipSkorlar.add(new Score(Scores.Yahtzee, o2_13));

        addEventListenerToButtons();
    }

    // Tur değiştirildiğinde arayüz durumunu güncelle
    public void turDegisimi(boolean control) {
        zarat_btn.setEnabled(control);
        rollCount = 0;
        // Zar seçme kutularını sıfırla
        z1.setSelected(false);
        z2.setSelected(false);
        z3.setSelected(false);
        z4.setSelected(false);
        z5.setSelected(false);
        z1.setEnabled(false);
        z2.setEnabled(false);
        z3.setEnabled(false);
        z4.setEnabled(false);
        z5.setEnabled(false);

        // Zar label'larının aktifliğini kontrol et
        for (Zar zar : zarlar) {
            zar.getLabel().setEnabled(control);
        }

        // Skor butonlarını aktif et
        for (Score myPoint : skorlar) {
            if (!myPoint.isButtonChoosen) {
                myPoint.getButton().setEnabled(control);
            }
        }
    }

    // Rakip skor butonlarını etkin/pasif yap
    public void disableRivalButtons(boolean control) {
        o2_1.setEnabled(control);
        o2_2.setEnabled(control);
        o2_3.setEnabled(control);
        o2_4.setEnabled(control);
        o2_5.setEnabled(control);
        o2_6.setEnabled(control);
        o2_7.setEnabled(control);
        o2_8.setEnabled(control);
        o2_9.setEnabled(control);
        o2_10.setEnabled(control);
        o2_11.setEnabled(control);
        o2_12.setEnabled(control);
        o2_13.setEnabled(control);
    }

    // Tüm skor butonlarını devre dışı bırak
    public void disableButtons() {
        for (Score myPoint : skorlar) {
            if (!myPoint.isButtonChoosen) {
                myPoint.getButton().setText("-");
            }
            myPoint.getButton().setEnabled(false);
        }
        zarat_btn.setEnabled(false);
    }

    // Skor butonlarına tıklama olaylarını dinleyen ActionListener'ları ekler
    public void addEventListenerToButtons() {
        for (Score myPoint : skorlar) {
            myPoint.getButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    if (sıra == 1 && rollCount > 0) {
                        ScoreMessage score = new ScoreMessage(myPoint.getScore_type());
                        try {
                            score.content = Integer.parseInt(myPoint.getButton().getText());
                        } catch (Exception e) {
                            myPoint.getButton().setText("0");
                            score.content = 0;
                        }
                        sıra = 0;
                        myPoint.isButtonChoosen = true;

                        // Rakibe sıra geçtiğini bildir
                        Message msg = new Message(Message.Message_Type.TurDegis);
                        msg.content = score;
                        Client.Send(msg);
                        System.out.println("Client mesajı yollandı");
                        oyunSonu(); // Ara toplam ve oyun sonu kontrolü
                        disableButtons(); // Butonları devre dışı bırak
                    }
                }
            });
        }
    }

    // Ara toplam ve oyun bitiş kontrolünü yapar
    public void oyunSonu() {
        int araToplam = 0;
        int araToplamSon = 0;
        int sonToplam = 0;

        // Eğer tüm üst kısım skorları seçildiyse
        if (getMyButton(Scores.Birler).isButtonChoosen
                && getMyButton(Scores.Ikiler).isButtonChoosen
                && getMyButton(Scores.Ucler).isButtonChoosen
                && getMyButton(Scores.Dortler).isButtonChoosen
                && getMyButton(Scores.Besler).isButtonChoosen
                && getMyButton(Scores.Altilar).isButtonChoosen) {

            // Üst skorların toplamını al
            araToplam += Integer.parseInt(getMyButton(Scores.Birler).getButton().getText());
            araToplam += Integer.parseInt(getMyButton(Scores.Ikiler).getButton().getText());
            araToplam += Integer.parseInt(getMyButton(Scores.Ucler).getButton().getText());
            araToplam += Integer.parseInt(getMyButton(Scores.Dortler).getButton().getText());
            araToplam += Integer.parseInt(getMyButton(Scores.Besler).getButton().getText());
            araToplam += Integer.parseInt(getMyButton(Scores.Altilar).getButton().getText());

            // 63'ten büyükse 35 bonus ekle
            if (araToplam >= 63) {
                araToplamSon = araToplam + 35;
                o1_bonus.setText("35");
            } else {
                araToplamSon = araToplam;
                o1_bonus.setText("0");
            }
            o1_ara.setText(String.valueOf(araToplamSon));

            // Ara toplamı rakibe gönder
            Message aratoplamMsg = new Message(Message.Message_Type.AraToplam);
            aratoplamMsg.content = o1_ara.getText();
            Client.Send(aratoplamMsg);

            // Alt bölüm skorları da tamamsa oyun bitti
            if (getMyButton(Scores.UcSet).isButtonChoosen
                    && getMyButton(Scores.DortSet).isButtonChoosen
                    && getMyButton(Scores.FullHouse).isButtonChoosen
                    && getMyButton(Scores.KucukKent).isButtonChoosen
                    && getMyButton(Scores.BuyukKent).isButtonChoosen
                    && getMyButton(Scores.Sans).isButtonChoosen
                    && getMyButton(Scores.Yahtzee).isButtonChoosen) {

                // Alt skorların toplamı
                sonToplam += Integer.parseInt(getMyButton(Scores.UcSet).getButton().getText());
                sonToplam += Integer.parseInt(getMyButton(Scores.DortSet).getButton().getText());
                sonToplam += Integer.parseInt(getMyButton(Scores.FullHouse).getButton().getText());
                sonToplam += Integer.parseInt(getMyButton(Scores.KucukKent).getButton().getText());
                sonToplam += Integer.parseInt(getMyButton(Scores.BuyukKent).getButton().getText());
                sonToplam += Integer.parseInt(getMyButton(Scores.Sans).getButton().getText());
                sonToplam += Integer.parseInt(getMyButton(Scores.Yahtzee).getButton().getText());

                sonToplam += araToplamSon; // Toplam skor
                o1_toplam.setText(String.valueOf(sonToplam));

                // Bitiş mesajı gönder
                Message finishMsg = new Message(Message.Message_Type.Bitis);
                finishMsg.content = o1_toplam.getText();
                Client.Send(finishMsg);

                // Her iki taraf da oyunu bitirdiyse kazananı belirle
                if (finishState) {
                    int sonToplam2 = Integer.parseInt(o2_toplam.getText());
                    String finishStr = "";
                    String msg = "";

                    if (sonToplam > sonToplam2) {
                        finishStr = "Sen Kazandın !! Senin skorun: " + sonToplam + " Rakip skor: " + sonToplam2;
                        msg = "Rakibin kazandı :(  Senin skor: " + sonToplam2 + " Rakip Skor: " + sonToplam;
                    } else if (sonToplam2 > sonToplam) {
                        finishStr = "Rakibin kazandı :( Rakip Skor: " + sonToplam2 + " Senin skorun: " + sonToplam;
                        msg = "Sen Kazandın !! Rakip Skor: " + sonToplam + " Senin skorun: " + sonToplam2;
                    }
                    lbl_bitis.setText(finishStr);

                    // Kazanma mesajını Client'a gönder
                    Message winMessage = new Message(Message.Message_Type.Kazanma);
                    winMessage.content = msg;  // Kazanma mesajı içerik olarak
                    Client.Send(winMessage);

                    btn_yeni.setEnabled(true);
                }

            }
        }
    }

    // Yeni oyun için oyun ekranını sıfırla
    public void resetGame() {
        // Skorları sıfırlama
        for (Score myPoint : skorlar) {
            myPoint.getButton().setEnabled(true);
            myPoint.getButton().setText("-");
            myPoint.isButtonChoosen = false;
        }

        for (Score rivalPoint : rakipSkorlar) {
            rivalPoint.getButton().setEnabled(true);
            rivalPoint.getButton().setText("-");
        }

        // Zarları sıfırlama (yani rastgele yeni değerlere ayarla)
        for (Zar zar : zarlar) {
            zar.shuffle();
        }

        // Zarlama sayacını sıfırla
        rollCount = 0;

        // Oyunun bitiş durumu sıfırla
        finishState = false;

        // Skor ekranlarındaki tüm sayısal değerleri sıfırla
        o1_bonus.setText("-");
        o2_bonus.setText("-");
        o1_ara.setText("-");
        o2_ara.setText("-");
        o1_toplam.setText("-");
        o2_toplam.setText("-");

        // Oyuncu sırası sıfırlanıyor, oyun ilk oyuncuyla başlasın
        sıra = 1;

        // Zarı atma butonunu yeniden etkinleştirme
        zarat_btn.setEnabled(true);
        disableRivalButtons(false); // Rakip oyuncunun butonlarını devre dışı bırakma

        // Bitmiş oyun mesajını temizle
        lbl_bitis.setText("___________________________________________________");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        zarat_btn = new javax.swing.JButton();
        z1 = new javax.swing.JRadioButton();
        z2 = new javax.swing.JRadioButton();
        z3 = new javax.swing.JRadioButton();
        z4 = new javax.swing.JRadioButton();
        z5 = new javax.swing.JRadioButton();
        dice05lbl = new javax.swing.JLabel();
        dice04lbl = new javax.swing.JLabel();
        dice03lbl = new javax.swing.JLabel();
        dice01lbl = new javax.swing.JLabel();
        dice02lbl = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        oyuncu1_lbl = new javax.swing.JLabel();
        oyuncu2_lbl = new javax.swing.JLabel();
        o1_1 = new javax.swing.JButton();
        o1_2 = new javax.swing.JButton();
        o1_3 = new javax.swing.JButton();
        o1_4 = new javax.swing.JButton();
        o1_5 = new javax.swing.JButton();
        o1_6 = new javax.swing.JButton();
        o1_7 = new javax.swing.JButton();
        o1_8 = new javax.swing.JButton();
        o1_9 = new javax.swing.JButton();
        o1_10 = new javax.swing.JButton();
        o1_11 = new javax.swing.JButton();
        o1_12 = new javax.swing.JButton();
        o1_13 = new javax.swing.JButton();
        o2_1 = new javax.swing.JButton();
        o2_2 = new javax.swing.JButton();
        o2_3 = new javax.swing.JButton();
        o2_4 = new javax.swing.JButton();
        o2_5 = new javax.swing.JButton();
        o2_6 = new javax.swing.JButton();
        o2_7 = new javax.swing.JButton();
        o2_8 = new javax.swing.JButton();
        o2_9 = new javax.swing.JButton();
        o2_10 = new javax.swing.JButton();
        o2_11 = new javax.swing.JButton();
        o2_12 = new javax.swing.JButton();
        o2_13 = new javax.swing.JButton();
        o1_bonus = new javax.swing.JLabel();
        o2_bonus = new javax.swing.JLabel();
        o1_ara = new javax.swing.JLabel();
        o2_ara = new javax.swing.JLabel();
        o1_toplam = new javax.swing.JLabel();
        o2_toplam = new javax.swing.JLabel();
        lbl_bitis = new javax.swing.JLabel();
        btn_yeni = new javax.swing.JButton();
        btn_oyun = new javax.swing.JButton();
        control = new javax.swing.JLabel();
        btn_cikis = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(781, 708));
        setSize(new java.awt.Dimension(781, 708));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setMaximumSize(new java.awt.Dimension(781, 708));
        jPanel2.setMinimumSize(new java.awt.Dimension(781, 708));
        jPanel2.setPreferredSize(new java.awt.Dimension(781, 708));
        jPanel2.setRequestFocusEnabled(false);

        zarat_btn.setBackground(new java.awt.Color(255, 255, 102));
        zarat_btn.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        zarat_btn.setForeground(new java.awt.Color(76, 32, 165));
        zarat_btn.setText("ZAR AT");
        zarat_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zarat_btnActionPerformed(evt);
            }
        });

        z1.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        z1.setForeground(new java.awt.Color(76, 32, 165));
        z1.setText(" ZAR 1 TUT");
        z1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                z1StateChanged(evt);
            }
        });

        z2.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        z2.setForeground(new java.awt.Color(76, 32, 165));
        z2.setText("ZAR 2 TUT");
        z2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                z2StateChanged(evt);
            }
        });

        z3.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        z3.setForeground(new java.awt.Color(76, 32, 165));
        z3.setText("ZAR 3 TUT");
        z3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                z3StateChanged(evt);
            }
        });

        z4.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        z4.setForeground(new java.awt.Color(76, 32, 165));
        z4.setText("ZAR 4 TUT");
        z4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                z4StateChanged(evt);
            }
        });

        z5.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        z5.setForeground(new java.awt.Color(76, 32, 165));
        z5.setText("ZAR 5 TUT");
        z5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                z5StateChanged(evt);
            }
        });

        dice05lbl.setText("zar5");

        dice04lbl.setText("zar4");

        dice03lbl.setText("zar3");

        dice01lbl.setText("zar1");

        dice02lbl.setText("zar2");

        jLabel17.setFont(new java.awt.Font("PT Serif", 3, 36)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(76, 32, 165));
        jLabel17.setText("YAHTZEE");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Birler");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 50, -1, -1));

        jLabel2.setText("İkiler");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 82, -1, -1));

        jLabel3.setText("Üçler");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 114, -1, -1));

        jLabel4.setText("Dörtler");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 146, -1, -1));

        jLabel5.setText("Beşler");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 178, -1, -1));

        jLabel6.setText("Altılar");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 210, -1, -1));

        jLabel7.setText("Bonus");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 239, -1, -1));

        jLabel8.setText("Ara Toplam");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 268, -1, -1));

        jLabel9.setText("3'lü Set");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 303, -1, -1));

        jLabel10.setText("4'lü Set");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 335, -1, -1));

        jLabel11.setText("Full House");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 367, -1, -1));

        jLabel12.setText("Küçük Kent");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 402, -1, -1));

        jLabel13.setText("Büyük Kent");
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 434, -1, -1));

        jLabel14.setText("Şans");
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 466, -1, -1));

        jLabel15.setText("Yahtzee");
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 498, -1, -1));

        jLabel16.setText("Toplam");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 530, -1, -1));

        oyuncu1_lbl.setText("OYUNCU 1");
        jPanel1.add(oyuncu1_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 12, -1, -1));

        oyuncu2_lbl.setText("OYUNCU 2");
        jPanel1.add(oyuncu2_lbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 12, -1, -1));

        o1_1.setText("-");
        o1_1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 47, 60, -1));

        o1_2.setText("-");
        o1_2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 79, 60, -1));

        o1_3.setText("-");
        o1_3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 111, 60, -1));

        o1_4.setText("-");
        o1_4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 143, 60, -1));

        o1_5.setText("-");
        o1_5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 175, 60, -1));

        o1_6.setText("-");
        o1_6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 207, 60, -1));

        o1_7.setText("-");
        o1_7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 300, 60, -1));

        o1_8.setText("-");
        o1_8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 332, 60, -1));

        o1_9.setText("-");
        o1_9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 364, 60, -1));

        o1_10.setText("-");
        o1_10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 399, 60, -1));

        o1_11.setText("-");
        o1_11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_11, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 431, 60, -1));

        o1_12.setText("-");
        o1_12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_12, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 463, 60, -1));

        o1_13.setText("-");
        o1_13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o1_13, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 495, 60, -1));

        o2_1.setText("-");
        o2_1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_1, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 47, 60, -1));

        o2_2.setText("-");
        o2_2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 79, 60, -1));

        o2_3.setText("-");
        o2_3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_3, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 111, 60, -1));

        o2_4.setText("-");
        o2_4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_4, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 143, 60, -1));

        o2_5.setText("-");
        o2_5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_5, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 175, 60, -1));

        o2_6.setText("-");
        o2_6.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_6, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 207, 60, -1));

        o2_7.setText("-");
        o2_7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_7, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 300, 60, -1));

        o2_8.setText("-");
        o2_8.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_8, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 332, 60, -1));

        o2_9.setText("-");
        o2_9.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_9, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 364, 60, -1));

        o2_10.setText("-");
        o2_10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_10, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 399, 60, -1));

        o2_11.setText("-");
        o2_11.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_11, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 431, 60, -1));

        o2_12.setText("-");
        o2_12.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_12, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 463, 60, -1));

        o2_13.setText("-");
        o2_13.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(51, 0, 153), 3, true));
        jPanel1.add(o2_13, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 495, 60, -1));

        o1_bonus.setText("-");
        jPanel1.add(o1_bonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 239, -1, -1));

        o2_bonus.setText("-");
        jPanel1.add(o2_bonus, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 239, -1, -1));

        o1_ara.setText("-");
        jPanel1.add(o1_ara, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 268, -1, -1));

        o2_ara.setText("-");
        jPanel1.add(o2_ara, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 268, -1, -1));

        o1_toplam.setText("-");
        jPanel1.add(o1_toplam, new org.netbeans.lib.awtextra.AbsoluteConstraints(131, 530, -1, -1));

        o2_toplam.setText("-");
        jPanel1.add(o2_toplam, new org.netbeans.lib.awtextra.AbsoluteConstraints(261, 530, -1, -1));

        lbl_bitis.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lbl_bitis.setForeground(new java.awt.Color(76, 32, 165));
        lbl_bitis.setText("___________________________________________________");

        btn_yeni.setBackground(new java.awt.Color(255, 255, 102));
        btn_yeni.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btn_yeni.setForeground(new java.awt.Color(76, 32, 165));
        btn_yeni.setText("YENİ OYUN");
        btn_yeni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_yeniActionPerformed(evt);
            }
        });

        btn_oyun.setBackground(new java.awt.Color(255, 255, 102));
        btn_oyun.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btn_oyun.setForeground(new java.awt.Color(76, 32, 165));
        btn_oyun.setText("RAKİP BUL");
        btn_oyun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_oyunActionPerformed(evt);
            }
        });

        control.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        control.setForeground(new java.awt.Color(76, 32, 165));
        control.setText("Rakip bekleniyor...");

        btn_cikis.setBackground(new java.awt.Color(255, 255, 102));
        btn_cikis.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        btn_cikis.setForeground(new java.awt.Color(76, 32, 165));
        btn_cikis.setText("ÇIKIŞ");
        btn_cikis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cikisActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btn_cikis)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(control, javax.swing.GroupLayout.PREFERRED_SIZE, 377, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(dice01lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(z1))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(dice02lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(z2))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(dice03lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(z3))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(dice04lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(z4))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(dice05lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(z5)))
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btn_yeni, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(zarat_btn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btn_oyun, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_bitis, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice01lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(z1))
                            .addComponent(btn_yeni))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice02lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(z2)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice03lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(z3)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice04lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(z4)))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dice05lbl, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(z5))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(btn_oyun)
                                .addGap(268, 268, 268)
                                .addComponent(zarat_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_bitis)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(control, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_cikis))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 702, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void zarat_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zarat_btnActionPerformed
        rollCount++; // Zar atma sayısını bir artır
        // Tüm zar seçimlerini aktif et
        z1.setEnabled(true);
        z2.setEnabled(true);
        z3.setEnabled(true);
        z4.setEnabled(true);
        z5.setEnabled(true);

        // Her oyuncunun bir elde en fazla 3 zar atma hakkı vardır
        if (rollCount == 3) {
            zarat_btn.setEnabled(false); // 3. atıştan sonra zar atma butonunu devre dışı bırak
            z1.setEnabled(false);
            z2.setEnabled(false);
            z3.setEnabled(false);
            z4.setEnabled(false);
            z5.setEnabled(false);
        }

        // Etkin olan (tutulmayan) zarları karıştır
        for (Zar d : zarlar) {
            if (d.getLabel().isEnabled()) {
                d.shuffle();
            }
        }

        // Oyuncunun skor tablosundaki seçilmemiş hücreleri yeniden hesapla ve güncelle
        for (Score myPoint : skorlar) {
            if (!myPoint.isButtonChoosen) {
                myPoint.getButton().setText(String.valueOf(Score.SkorHesaplama(zarlar, myPoint.getScore_type())));
            }
        }

        // Zar bilgilerini diğer client'a gönder (eş zamanlı görüntü için)
        int[] values = new int[5];
        for (int i = 0; i < 5; i++) {
            values[i] = zarlar[i].getValue(); // Zar nesnesinden değer al
        }

        // Zar bilgilerini içeren mesaj oluştur
        ZarMessage diceMessage = new ZarMessage(values); // veya senin mevcut yapına göre diziyi gönder
        Message msg = new Message(Message.Message_Type.Zarlar);
        msg.content = diceMessage;
        Client.Send(msg); // Mesajı client üzerinden gönder

        revalidate();
    }//GEN-LAST:event_zarat_btnActionPerformed

    // Her bir zar için tutulma durumu değiştiğinde, zarın kilitlenip kilitlenmeyeceğini kontrol eder
    private void z1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_z1StateChanged
        if (z1.isSelected()) {
            dice01lbl.setEnabled(false); // Zar tutuldu, devre dışı bırak
        } else {
            dice01lbl.setEnabled(true); // Zar bırakıldı, tekrar etkinleştir
        }
    }//GEN-LAST:event_z1StateChanged

    private void z2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_z2StateChanged
        if (z2.isSelected()) {
            dice02lbl.setEnabled(false); // Zar tutuldu, devre dışı bırak
        } else {
            dice02lbl.setEnabled(true); // Zar bırakıldı, tekrar etkinleştir
        }
    }//GEN-LAST:event_z2StateChanged

    private void z3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_z3StateChanged
        if (z3.isSelected()) {
            dice03lbl.setEnabled(false); // Zar tutuldu, devre dışı bırak
        } else {
            dice03lbl.setEnabled(true); // Zar bırakıldı, tekrar etkinleştir
        }
    }//GEN-LAST:event_z3StateChanged

    private void z4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_z4StateChanged
        if (z4.isSelected()) {
            dice04lbl.setEnabled(false); // Zar tutuldu, devre dışı bırak
        } else {
            dice04lbl.setEnabled(true); // Zar bırakıldı, tekrar etkinleştir
        }
    }//GEN-LAST:event_z4StateChanged

    private void z5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_z5StateChanged
        if (z5.isSelected()) {
            dice05lbl.setEnabled(false); // Zar tutuldu, devre dışı bırak
        } else {
            dice05lbl.setEnabled(true); // Zar bırakıldı, tekrar etkinleştir
        }
    }//GEN-LAST:event_z5StateChanged

    private void btn_yeniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_yeniActionPerformed
        btn_yeni.setEnabled(false); // Yeni oyun butonunu devre dışı bırak (bir daha basılmasın diye)
        resetGame(); // Oyunu sıfırla

        // Yeni oyunun başladığını karşı tarafa bildir
        Message msgYeniOyun = new Message(Message.Message_Type.YeniOyun);
        Client.Send(msgYeniOyun);

    }//GEN-LAST:event_btn_yeniActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // Pencere kapanınca bağlantı koptu mesajı gönder
        Message msgKapandi = new Message(Message.Message_Type.BaglantiKoptu);
        Client.Send(msgKapandi);
    }//GEN-LAST:event_formWindowClosed

    private void btn_oyunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_oyunActionPerformed
        // Client servera baglanıyor
        Client.Start("56.228.28.117", 5000);
        //Client.Start("127.0.0.1", 4000); //Localhostta deneme
        // Eğer bağlantı kurulamazsa hata mesajı göster ve uygulamayı kapat
        if (Client.socket == null) {
            JOptionPane.showMessageDialog(this, "Connection Failed !!");
            System.exit(0);
        }
        // Bağlantı başarılıysa "Rakip Bul" butonunu devre dışı bırak
        btn_oyun.setEnabled(false);
    }//GEN-LAST:event_btn_oyunActionPerformed

    private void btn_cikisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cikisActionPerformed
        System.exit(0); // Uygulamadan çık
    }//GEN-LAST:event_btn_cikisActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_cikis;
    public javax.swing.JButton btn_oyun;
    public javax.swing.JButton btn_yeni;
    public static javax.swing.JLabel control;
    private javax.swing.JLabel dice01lbl;
    private javax.swing.JLabel dice02lbl;
    private javax.swing.JLabel dice03lbl;
    private javax.swing.JLabel dice04lbl;
    private javax.swing.JLabel dice05lbl;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JLabel lbl_bitis;
    private javax.swing.JButton o1_1;
    private javax.swing.JButton o1_10;
    private javax.swing.JButton o1_11;
    private javax.swing.JButton o1_12;
    private javax.swing.JButton o1_13;
    private javax.swing.JButton o1_2;
    private javax.swing.JButton o1_3;
    private javax.swing.JButton o1_4;
    private javax.swing.JButton o1_5;
    private javax.swing.JButton o1_6;
    private javax.swing.JButton o1_7;
    private javax.swing.JButton o1_8;
    private javax.swing.JButton o1_9;
    private javax.swing.JLabel o1_ara;
    private javax.swing.JLabel o1_bonus;
    private javax.swing.JLabel o1_toplam;
    private javax.swing.JButton o2_1;
    private javax.swing.JButton o2_10;
    private javax.swing.JButton o2_11;
    private javax.swing.JButton o2_12;
    private javax.swing.JButton o2_13;
    private javax.swing.JButton o2_2;
    private javax.swing.JButton o2_3;
    private javax.swing.JButton o2_4;
    private javax.swing.JButton o2_5;
    private javax.swing.JButton o2_6;
    private javax.swing.JButton o2_7;
    private javax.swing.JButton o2_8;
    private javax.swing.JButton o2_9;
    public javax.swing.JLabel o2_ara;
    private javax.swing.JLabel o2_bonus;
    public javax.swing.JLabel o2_toplam;
    public javax.swing.JLabel oyuncu1_lbl;
    public javax.swing.JLabel oyuncu2_lbl;
    public javax.swing.JRadioButton z1;
    public javax.swing.JRadioButton z2;
    public javax.swing.JRadioButton z3;
    public javax.swing.JRadioButton z4;
    public javax.swing.JRadioButton z5;
    public javax.swing.JButton zarat_btn;
    // End of variables declaration//GEN-END:variables
}
