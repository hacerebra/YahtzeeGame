/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client;

import Game.Zar;
import game_start.Game;
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

    public static Socket socket;
    public static ObjectInputStream sInput;
    public static ObjectOutputStream sOutput;
    public static boolean isPaired = false;
    public static ListenThread listen;

    public static void Start(String ip, int port) {
        try {
            // server ve socket baglantilari
            Client.socket = new Socket(ip, port);
            Client.listen = new ListenThread();
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listen.start();

            Message msg = new Message(Message.Message_Type.Ad);
            msg.content = Login.txt_ad.getText();
            Client.Send(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Stop() {
        try {
            if (Client.socket != null) {
                if (Client.listen != null) {
                    Client.listen.interrupt();  // Thread'i düzgün şekilde sonlandır
                }
                Client.socket.close();
                Client.sOutput.flush();  // Çıkış stream'ini temizle
                Client.sOutput.close();
                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Display(String message) {
        System.out.println(message);
    }

    public static void Send(Message msg) {
        try {
            Client.sOutput.writeObject(msg);
            Client.sOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

class ListenThread extends Thread {

    @Override
    public void run() {
        while (Client.socket.isConnected()) {
            try {
                Message msg = (Message) Client.sInput.readObject();
                switch (msg.type) {
                    case Ad:
                        break;
                    case RakipBaglanti:
                        System.out.println("Rakip Bağlantı");
                        String rivalName = (String) msg.content;
                        Client.isPaired = true;
                        Login.control.setText("Eşleşme oldu");
                        Login.game.oyuncu1_lbl.setText("Sen");
                        Login.game.oyuncu2_lbl.setText(rivalName);
                        Login.game.setVisible(true);
                        break;
                    case TurDegis:
                        System.out.println("Tur değişimi");
                        ScoreMessage score = (ScoreMessage) msg.content;
                        Login.game.getRivalButtonByGivenType(score.score_type).setText(String.valueOf(score.content));
                        Login.game.roundControl = 1;
                        Login.game.changeTurn(true);
                        break;
                    case Kontrol:
                        Login.game.roundControl = (int) msg.content;
                        System.out.println((int) msg.content);
                        if ((int) msg.content == 1) {
                            Login.game.changeTurn(true);
                        } else if ((int) msg.content == 0) {
                            Login.game.changeTurn(false);
                        }
                        break;
                    case Zarlar:
                        ZarMessage gelenZarlar = (ZarMessage) msg.content;
                        for (int i = 0; i < 5; i++) {
                            Login.game.zarlar[i].label.setIcon(Zar.getImageIcon(gelenZarlar.zarlar[i]));
                        }
                        break;
                    case Bitis:
                        Login.game.finishState = true;
                        Game.o2_toplam.setText((String) msg.content);
                        break;
                    case Kazanma:
                        Login.game.lbl_bitis.setText((String) msg.content);
                        Login.game.btn_yeni.setEnabled(true);
                        break;
                    case YeniOyun:
                        System.out.println("Yeni oyun başlatılıyor...");
                        // Yeni oyun için gerekli sıfırlamaları yapın
                        Login.game.btn_yeni.setEnabled(false);
                        Login.game.zarat_btn.setEnabled(false);
                        Login.game.resetGame();  // Bu metodu Login.game içerisinde tanımlamanız gerekebilir
                        // Rakip ekranında "Yeni oyun başladı!" mesajı gösterebilirsiniz
                        JOptionPane.showMessageDialog(null, "Yeni oyun başladı!", "Oyun Başladı", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case BaglantiKoptu:
                        JOptionPane.showMessageDialog(null, msg.content.toString(), "Bağlantı Kesildi", JOptionPane.WARNING_MESSAGE);
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
