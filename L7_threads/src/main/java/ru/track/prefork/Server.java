package ru.track.prefork;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * - multithreaded +
 * - atomic counter +
 * - setName() +
 * - thread -> Worker +
 * - save threads
 * - broadcast (fail-safe)
 */
public class Server {
    static Logger log = LoggerFactory.getLogger(Server.class);
    private AtomicInteger counter = new AtomicInteger(0);
    private int port;
    Map<Integer, MyThread> threadMap = new HashMap<>();

    public Server(int port) {
        this.port = port;
    }

    public class MyThread extends Thread {
        private Socket socket;
        private InputStream in;
        private DataOutputStream out;
        private ObjectOutputStream oos;
        private int id;

        public MyThread(Socket socket, int id) throws IOException {
            this.socket = socket;
            setName(String.format("Client[%d]@%s:%d", id, socket.getInetAddress(), socket.getPort()));
            this.id = id;
            in = socket.getInputStream();
            out = new DataOutputStream(socket.getOutputStream());
            oos = new ObjectOutputStream(out);
        }

        public void send(byte[] buf, int nRead, int id) {
            try {
                MyThread thread = threadMap.get(id);
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Client[%d]@%s:%d>", id, thread.socket.getInetAddress(), thread.socket.getPort())).append(buf.toString());
                byte[] buff = sb.toString().getBytes();
                out.write(buff);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(Message msg, int id) {
            try {
                MyThread thread = threadMap.get(id);
                msg.username = String.format("Client[%d]@%s:%d", id, thread.socket.getInetAddress(), thread.socket.getPort());
                oos.writeObject(msg);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (!socket.isClosed()) {
                    log.info("on select...");
                    ObjectInputStream ois = new ObjectInputStream(in);
                    Message msg = (Message) ois.readObject();
                    if ("EXIT".equals(msg.data)) {
                        socket.close();
                        break;
                    }
                    for (Map.Entry<Integer, MyThread> entry : threadMap.entrySet()) {
                        if (entry.getValue().id != id) {
                            entry.getValue().send(msg, id);
                        }
                    }
                }
            } catch (Exception e) {
                log.info("something is wrong");
            }
        }
    }

    public void serve() throws Exception {
        ServerSocket serverSocket = new ServerSocket(port, 10, InetAddress.getByName("localhost"));
        while (true) {
            final Socket socket = serverSocket.accept();
            int id = counter.getAndIncrement();
            MyThread thread = new MyThread(socket, id);
            threadMap.put(id, thread);
            thread.start();
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(9000);
        server.serve();
    }
}
