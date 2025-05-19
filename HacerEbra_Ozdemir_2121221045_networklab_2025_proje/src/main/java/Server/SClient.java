/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Client.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hacerebra
 */
public class SClient {

    int clientID; // Client'in benzersiz ID'si
    Socket socket; // Client'in bağlantı soketi
    public String name = "NoName";
    ObjectOutputStream sOutput; // Mesaj gönderme için ObjectOutputStream
    ObjectInputStream sInput; // Mesaj alma için ObjectInputStream
    Listen listenThread; // Mesajları dinlemek için oluşturulan thread
    PairingThread pairThread; // Rakip eşlemesi yapmak için oluşturulan thread
    SClient rival; // Rakip client (eşleşmiş client)
    public boolean paired = false; // Eşleşme durumu

    public SClient(int clientID, Socket socket) {
        try {
            this.socket = socket;
            this.clientID = clientID;
            this.listenThread = new Listen(this);
            this.pairThread = new PairingThread(this);
            this.sOutput = new ObjectOutputStream(this.socket.getOutputStream());
            this.sInput = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Client'a mesaj göndermek için kullanılan metot
    public void Send(Message message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Client mesajlarını dinleyen thread sınıfı. Gelen mesajları okuyup uygun işlemi yapar
    public class Listen extends Thread {

        SClient sclient;

        public Listen(SClient sclient) {
            this.sclient = sclient;
        }

        @Override
        public void run() {
            // Client bağlantısı devam ettiği sürece döngü devam eder
            while (sclient.socket.isConnected()) {
                try {
                    Message msg = (Message) sclient.sInput.readObject(); // Gelen mesajı al
                    switch (msg.type) { // Mesaj tipine göre işlem yap
                        case Ad: // Client ismini alır ve eşleştirme threadini başlatır
                            sclient.name = msg.content.toString();
                            if (!sclient.pairThread.isAlive()) {
                                sclient.pairThread.start();
                            }
                            break;
                        case TurDegis: // Tur değişimini rakip client'a gönder
                            sclient.rival.Send(msg);
                            System.out.println("Tur degistirdi.");
                            break;
                        // Bu mesaj tiplerini doğrudan rakibe ilet
                        case Kontrol:
                            Server.Send(sclient.rival, msg);
                            break;
                        case Zarlar:
                            Server.Send(sclient.rival, msg);
                            break;
                        case AraToplam:
                            Server.Send(sclient.rival, msg);
                            break;
                        case Bitis:
                            Server.Send(sclient.rival, msg);
                            break;
                        case Kazanma:
                            Server.Send(sclient.rival, msg);
                        case YeniOyun:
                            Server.Send(sclient.rival, msg);
                            break;
                        case BaglantiKoptu:
                            Server.Send(sclient.rival, msg);
                            break;

                    }
                } catch (IOException ex) {
                    System.out.println("Listen Thread Exception");

                    // Bağlantı kopunca soketi kapat
                    try {
                        sclient.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Rakip varsa ona bağlantı koptu mesajı gönder
                    if (sclient.rival != null) {
                        Message disconnectMsg = new Message(Message.Message_Type.BaglantiKoptu);
                        disconnectMsg.content = "Rakibiniz oyundan ayrıldı.";
                        Server.Send(sclient.rival, disconnectMsg);

                        // Rakibin eşleşme durumunu sıfırla
                        sclient.rival.rival = null;
                        sclient.rival.paired = false;

                        // Rakip eşleşme threadini yeniden başlat (eğer kapalıysa)
                        if (sclient.rival.pairThread == null || !sclient.rival.pairThread.isAlive()) {
                            sclient.rival.pairThread = sclient.rival.new PairingThread(sclient.rival); // Yeni thread örneği
                            sclient.rival.pairThread.start(); // Tekrar başlat
                        }
                    }
                    // Client listeden çıkarılır
                    Server.sclients.remove(sclient);
                    // Thread'den çık
                    break;
                } catch (ClassNotFoundException ex) {
                    System.out.println("Class Not Found");
                } catch (IllegalThreadStateException te) {
                    System.out.println("Illegal Thread");
                }
            }
        }

    }

    // Client eşleştirmesini sağlayan thread sınıfı. Henüz eşleşmemiş client'lar için uygun rakip arar ve eşleştirir.
    public class PairingThread extends Thread {

        SClient sclient;

        public PairingThread(SClient sclient) {
            this.sclient = sclient;
        }

        @Override
        public void run() {
            // Client eşleşene kadar döngü devam eder
            while (!this.sclient.paired) {
                try {
                    // Bağlantı kopmuşsa listeden çıkar ve thread'i bitir
                    if (sclient.socket.isClosed() || !sclient.socket.isConnected()) {
                        Server.sclients.remove(sclient);
                        break;
                    }

                    // Eşleştirme için semafor alınır (eşzamanlı erişim kontrolü)
                    Server.esler.acquire();

                    if (!sclient.paired) {
                        SClient selectedPair = null;

                        // Server'daki client listesinde uygun rakip ara
                        for (SClient client : Server.sclients) {
                            if (client == sclient) {
                                continue;
                            }

                            // Bağlantısı kopanları listeden çıkar
                            if (client.socket.isClosed() || !client.socket.isConnected()) {
                                Server.sclients.remove(client);
                                continue;
                            }

                            // Rakipsiz ve eşleşmemiş client bulunursa seç
                            if (client.rival == null && !client.paired) {
                                selectedPair = client;

                                // Karşılıklı eşle
                                selectedPair.rival = sclient;
                                sclient.rival = selectedPair;

                                selectedPair.paired = true;
                                sclient.paired = true;

                                // Eşleşme mesajlarını karşılıklı gönder
                                Message msg1 = new Message(Message.Message_Type.RakipBaglanti);
                                msg1.content = sclient.name;
                                Server.Send(selectedPair, msg1);

                                Message msg2 = new Message(Message.Message_Type.RakipBaglanti);
                                msg2.content = selectedPair.name;
                                Server.Send(sclient, msg2);

                                // Oyunun kontrol mesajları
                                Message msg3 = new Message(Message.Message_Type.Kontrol);
                                msg3.content = 0;
                                Server.Send(sclient, msg3);

                                Message msg4 = new Message(Message.Message_Type.Kontrol);
                                msg4.content = 1;
                                Server.Send(selectedPair, msg4);
                                break;
                            }
                        }
                    }

                    // Semafor serbest bırakılır
                    Server.esler.release();

                    sleep(1000);

                } catch (InterruptedException e) {
                    System.out.println("PairingThread interrupted.");
                    break;
                }
            }

            // Döngüden çıkınca, bağlantı kopuksa listeden çıkar
            if (!sclient.paired && (sclient.socket.isClosed() || !sclient.socket.isConnected())) {
                Server.sclients.remove(sclient);
            }
        }
    }
}
