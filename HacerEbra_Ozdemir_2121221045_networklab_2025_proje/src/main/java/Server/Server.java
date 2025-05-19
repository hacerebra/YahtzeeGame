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

    public static int port; // Server'ın dinleyeceği port numarası
    public static int cID = 0;
    public static ServerSocket socket; // ServerSocket: Client bağlantılarını kabul etmek için kullanılan socket
    public static NewListenThread listenThread; // Client bağlantılarını dinleyen ana thread
    public static ArrayList<SClient> sclients = new ArrayList(); // Bağlanan tüm client'ların listesi
    public static Semaphore esler = new Semaphore(1, true);

    // Server'ı belirtilen portta başlatan method
    public static void Start(int port) {
        try {
            Server.port = port;
            Server.socket = new ServerSocket(Server.port); // ServerSocket oluşturulur ve portta dinleme başlar
            Server.listenThread = new NewListenThread(); // Yeni bağlantıları kabul etmek için dinleme thread'i oluşturulur
            Server.listenThread.start(); // Thread başlatılır
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Belirtilen client'a mesaj göndermek için kullanılır
    public static void Send(SClient cl, Message msg) {
        try {
            cl.sOutput.writeObject(msg);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

// Yeni client bağlantılarını kabul eden ve dinleyen thread
class NewListenThread extends Thread {

    @Override
    public void run() {
        // ServerSocket kapatılmadığı sürece sonsuz döngüde dinle
        while (!Server.socket.isClosed()) {
            try {
                System.out.println("Client Bekleniyor...");
                Socket newSocket = Server.socket.accept();  // Yeni bir client bağlantısı kabul edilir 
                System.out.println("Client Bağlandı");

                // Yeni bağlanan client için SClient nesnesi oluşturulur
                SClient newSClient = new SClient(Server.cID, newSocket);
                Server.cID++; // Client ID sayacı artırılır
                Server.sclients.add(newSClient); // Client listesine eklenir

                // Yeni client için dinleme thread'i başlatılır (mesajlar dinlenir)
                newSClient.listenThread.start();
            } catch (IOException ex) {
                // Eğer socket kapalıysa, program kapatma durumundadır ve döngü kırılır
                if (Server.socket.isClosed()) {
                    System.out.println("Server kapatıldı.");
                    break;
                }
                // Bağlantı hatası varsa, hata mesajı yazdırılır ve 1 saniye beklenip tekrar denenir
                System.out.println("Baglanti Hatası.");
                try {
                    Thread.sleep(1000); // Bağlantı hatasında 1 saniye bekleyip tekrar dener
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
