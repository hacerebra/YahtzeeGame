/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import Game.Zar;
import game_start.Login;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author hacerebra
 */
public class Client {

    // Sunucu ile iletişim kurmak için gerekli değişkenler
    public static Socket socket;
    public static ObjectInputStream sInput;
    public static ObjectOutputStream sOutput;
    public static boolean isPaired = false;
    public static ListenThread listen;

    // Sunucuya bağlanmak için başlatma metodu
    public static void Start(String ip, int port) {
        try {
            // IP ve port üzerinden sunucuya bağlanılır
            Client.socket = new Socket(ip, port);
            Client.listen = new ListenThread();

            // Giriş ve çıkış akışları ayarlanır
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());

            // Gelen mesajları dinlemek için thread başlatılır
            Client.listen.start();

            // Kullanıcı adını sunucuya gönder
            Message msg = new Message(Message.Message_Type.Ad);
            msg.content = Login.txt_ad.getText();
            Client.Send(msg);

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Bağlantıyı düzgün şekilde sonlandıran metod
    public static void Stop() {
        try {
            if (Client.socket != null) {
                if (Client.listen != null) {
                    Client.listen.interrupt(); // Dinleyici thread'i durdur
                }
                // Socket ve stream'leri kapat
                Client.socket.close();
                Client.sOutput.flush();  // Çıkış stream'ini temizle
                Client.sOutput.close();
                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Konsola mesaj yazdırmak için kullanılan yardımcı metod
    public static void Display(String message) {
        System.out.println(message);
    }

    // Mesajı sunucuya gönderen metod
    public static void Send(Message msg) {
        try {
            Client.sOutput.writeObject(msg);
            Client.sOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

// Gelen mesajları dinleyen thread sınıfı
class ListenThread extends Thread {

    @Override
    public void run() {
        // Socket bağlantısı devam ettiği sürece dinleme yapılır
        while (Client.socket.isConnected()) {
            try {
                Message msg = (Message) Client.sInput.readObject();
                switch (msg.type) {
                    case Ad:
                        break;
                    case RakipBaglanti:
                        // Rakip bağlantısı gerçekleşti
                        System.out.println("Rakip Bağlantı");
                        String rivalName = (String) msg.content;
                        Client.isPaired = true;
                        Login.game.control.setText("Eşleşme oldu.");
                        Login.game.oyuncu2_lbl.setText(rivalName);
                        Login.login.setVisible(false);
                        break;
                    case TurDegis:
                        // Tur değiştiğinde rakibin skoru güncellenir, sıra oyuncuya geçer
                        System.out.println("Tur değişimi");
                        ScoreMessage score = (ScoreMessage) msg.content;
                        Login.game.getRivalButtonByGivenType(score.score_type).setText(String.valueOf(score.content));
                        Login.game.roundControl = 1;
                        Login.game.changeTurn(true);
                        break;
                    case Kontrol:
                        // Tur kontrolü: Oyuncunun sırası mı, rakibin mi?
                        Login.game.roundControl = (int) msg.content;
                        System.out.println((int) msg.content);
                        if ((int) msg.content == 1) {
                            Login.game.changeTurn(true);
                        } else if ((int) msg.content == 0) {
                            Login.game.changeTurn(false);
                        }
                        break;
                    case Zarlar:
                        // Rakip tarafından atılan zarlar oyuncuya gönderilir ve gösterilir
                        ZarMessage gelenZarlar = (ZarMessage) msg.content;
                        for (int i = 0; i < 5; i++) {
                            Login.game.zarlar[i].label.setIcon(Zar.getImageIcon(gelenZarlar.zarlar[i]));
                        }
                        break;
                    case AraToplam:
                        // Rakibin ara toplam skoru güncellenir
                        Login.game.o2_ara.setText((String) msg.content);
                        break;
                    case Bitis:
                        // Oyun bitti, toplam skor görüntülenir
                        Login.game.finishState = true;
                        Login.game.o2_toplam.setText((String) msg.content);
                        break;
                    case Kazanma:
                        // Kazanma durumu mesajı gösterilir, yeni oyun başlatılabilir hale gelir
                        Login.game.lbl_bitis.setText((String) msg.content);
                        Login.game.btn_yeni.setEnabled(true);
                        Login.game.zarat_btn.setEnabled(false);
                        break;
                    /*case YeniOyun:
                        // Yeni oyun başlatılıyor
                        System.out.println("Yeni oyun başlatılıyor...");
                        Login.game.btn_yeni.setEnabled(false);
                        Login.game.resetGame(); // Oyunun iç durumunu sıfırlar
                        Login.game.zarat_btn.setEnabled(false);
                        JOptionPane.showMessageDialog(null, "Yeni oyun başladı!", "Oyun Başladı", JOptionPane.INFORMATION_MESSAGE);
                        break;*/
                    case YeniOyun:
                        int secim = JOptionPane.showConfirmDialog(
                                null,
                                "Rakibiniz yeni oyun başlatmak istiyor.\nYeni oyunu onaylıyor musunuz?",
                                "Yeni Oyun İsteği",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (secim == JOptionPane.YES_OPTION) {
                            System.out.println("Yeni oyun başlatılıyor...");
                            Login.game.btn_yeni.setEnabled(false);
                            Login.game.resetGame(); // Oyunun iç durumunu sıfırlar
                            Login.game.zarat_btn.setEnabled(false);
                            JOptionPane.showMessageDialog(null, "Yeni oyun başladı!", "Oyun Başladı", JOptionPane.INFORMATION_MESSAGE);

                        } else {
                            // Oyun pasif hale getirilir (oyuncu yeni rakip istemedi)
                            Login.game.control.setText("Yeni oyun başlatılmadı. Oyundan çıkabilirsiniz.");
                            Login.game.zarat_btn.setEnabled(false);
                            Login.game.btn_yeni.setEnabled(false);
                            Login.game.btn_cikis.setVisible(true);
                        }

                        break;
                    case BaglantiKoptu:
                        // Rakip bağlantısı koptuğunda oyuncuya sorulur: yeni rakip ister misin?
                        int choice = JOptionPane.showConfirmDialog(
                                null,
                                "Rakibiniz oyundan ayrıldı.\nYeni rakip bağlantısı ister misiniz?",
                                "Rakip Ayrıldı",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );

                        if (choice == JOptionPane.YES_OPTION) {
                            // Yeni rakip için hazırlık
                            Login.game.resetGame();
                            Login.game.control.setText("Rakibiniz ayrıldı.");
                            Login.game.oyuncu2_lbl.setText("Rakip");
                            Login.game.zarat_btn.setEnabled(false);
                            Login.game.btn_yeni.setEnabled(false);

                        } else {
                            // Oyun pasif hale getirilir (oyuncu yeni rakip istemedi)
                            Login.game.control.setText("Yeni oyun başlatılmadı. Oyundan çıkabilirsiniz.");
                            Login.game.zarat_btn.setEnabled(false);
                            Login.game.btn_yeni.setEnabled(false);
                            Login.game.btn_cikis.setVisible(true);
                        }

                        break;
                }
            } catch (IOException ex) {
                System.out.println("Bağlantı hatası: " + ex.getMessage());
                break;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
