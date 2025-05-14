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

    int clientID;
    Socket socket;
    public String name = "NoName";
    ObjectOutputStream sOutput;
    ObjectInputStream sInput;
    // dinleme threadi
    Listen listenThread;
    // eslesme threadi
    PairingThread pairThread;
    //rakip client
    SClient rival;
    //eşleşme durumu
    public boolean paired = false;

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

    //client mesaj gönderme
    public void Send(Message message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public class Listen extends Thread {

        SClient sclient;

        public Listen(SClient sclient) {
            this.sclient = sclient;
        }

        @Override
        public void run() {
            while (sclient.socket.isConnected()) {

                try {
                    Message msg = (Message) sclient.sInput.readObject();
                    switch (msg.type) {
                        case Ad:
                            sclient.name = msg.content.toString();
                            if (!sclient.pairThread.isAlive()) {
                                sclient.pairThread.start();
                            }
                            break;
                        case TurDegis:
                            sclient.rival.Send(msg);
                            System.out.println("Sclient mesajı yollandı.");
                            break;
                        case Kontrol:
                            Server.Send(sclient.rival, msg);
                            break;
                        case Zarlar:
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
                   
                    // Client bağlantısını kapat
                    try {
                        sclient.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Rakibi varsa rakibini bilgilendir
                    if (sclient.rival != null) {
                        Message disconnectMsg = new Message(Message.Message_Type.BaglantiKoptu);
                        disconnectMsg.content = "Rakibiniz oyundan ayrıldı.";
                        Server.Send(sclient.rival, disconnectMsg);

                        // Rakibin eşleşmesini de sıfırla
                        sclient.rival.rival = null;
                        sclient.rival.paired = false;
                    }
                    // Bu client'i listeden sil
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

    public class PairingThread extends Thread {

        SClient sclient;

        public PairingThread(SClient sclient) {
            this.sclient = sclient;
        }

        @Override
        public void run() {
            while (this.sclient.paired == false && this.sclient.socket.isConnected()) {
                try {
                    //lock mekanizması
                    //sadece bir client içeri grebilir
                    //diğerleri release olana kadar bekler
                    Server.pairTwo.acquire(1);

                    //client eğer eşleşmemişse 
                    if (!sclient.paired) {
                        SClient selectedPair = null;
                        while (selectedPair == null && this.sclient.socket.isConnected()) {
                            for (SClient client : Server.sclients) {
                                if (sclient != client && client.rival == null) {
                                    selectedPair = client;
                                    selectedPair.paired = true;
                                    selectedPair.rival = sclient;
                                    sclient.rival = selectedPair;
                                    sclient.paired = true;

                                    // eşleşme oldu
                                    Message msg1 = new Message(Message.Message_Type.RakipBaglanti);
                                    msg1.content = sclient.name;
                                    Server.Send(sclient.rival, msg1);

                                    Message msg2 = new Message(Message.Message_Type.RakipBaglanti);
                                    msg2.content = sclient.rival.name;
                                    Server.Send(sclient, msg2);

                                    Message msg3 = new Message(Message.Message_Type.Kontrol);
                                    int a = 0;
                                    msg3.content = a;
                                    Server.Send(sclient, msg3);

                                    Message msg4 = new Message(Message.Message_Type.Kontrol);
                                    int b = 1;
                                    msg4.content = b;
                                    Server.Send(sclient.rival, msg4);
                                    break;
                                }
                            }
                            //sürekli dönmesin 1 saniyede bir dönsün
                            sleep(1000);
                        }
                    }
                    //lock mekanizmasını servest bırak
                    //bırakılmazsa deadlock olur.
                    Server.pairTwo.release(1);
                } catch (InterruptedException ex) {
                    System.out.println("Pairing Thread Exception");
                } catch (IllegalThreadStateException te) {
                    System.out.println("Pairing Illegal Thread");
                }
            }
        }

    }

}
