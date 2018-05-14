package ru.track.prefork;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.*;

public class Client {
    static Logger log = LoggerFactory.getLogger(Client.class);

    private int port;
    private String host;

    public Client(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public void loop() throws Exception {
        Socket socket = new Socket(host, port);
        final OutputStream out = socket.getOutputStream();
        final InputStream in = socket.getInputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(out);
        final ObjectInputStream ois = new ObjectInputStream(in);
        Scanner scanner = new Scanner(System.in);
        Thread scannerThread;
        scannerThread = new Thread(() -> {
           try {
               while (true) {
                   String line = scanner.nextLine();
                   Message msg = new Message(line);
                   oos.writeObject(msg);
                   oos.flush();
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
        });
        scannerThread.setDaemon(true);
        scannerThread.start();
        while (true) {
            Message msg = (Message) ois.readObject();
            System.out.println(msg.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client(9000, "localhost");
        client.loop();
    }
}
