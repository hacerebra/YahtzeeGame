/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Client.Message;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hacerebra
 */
public class Server {

    public static int port;
    public static int clientID = 0;
    public static ServerSocket socket;
    public static SListenThread listenThread;
    public static ArrayList<SClient> sclients = new ArrayList();
    public static Semaphore pairTwo = new Semaphore(1, true);

    public static void Start(int port) {
        try {
            Server.port = port;
            Server.socket = new ServerSocket(Server.port);
            Server.listenThread = new SListenThread();
            Server.listenThread.start();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void Send(SClient cl, Message msg) {
        try {
            cl.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class SListenThread extends Thread {

    @Override
    public void run() {
        while (!Server.socket.isClosed()) {
            try {
                System.out.println("Client Bekleniyor...");
                Socket newSocket = Server.socket.accept();  // Client bağlantısını bekle
                System.out.println("Client Bağlandı");

                // Yeni client için yeni bir SClient nesnesi oluştur
                SClient newSClient = new SClient(Server.clientID, newSocket);
                Server.clientID++;  // Client ID'yi artır
                Server.sclients.add(newSClient);  // Client'ı listeye ekle

                // Yeni bir dinleme thread'i başlat
                newSClient.listenThread.start();
            } catch (IOException ex) {
                // Socket kapalıysa, bu bir hatadan çok bağlantı kapanması olabilir
                if (Server.socket.isClosed()) {
                    System.out.println("Server kapatıldı.");
                    break;
                }
                System.out.println("Connection error, retrying...");
                try {
                    Thread.sleep(1000); // Bağlantı hatasında 1 saniye bekleyip tekrar dener
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
