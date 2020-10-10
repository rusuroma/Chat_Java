package ro.roma.chat.server;
import ro.roma.network.TCPConnection;
import ro.roma.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {

    public static  void main(String []args){
new ChatServer();
    }//end main
    private final ArrayList<TCPConnection> connections=new ArrayList<>();
    private ChatServer() {
        System.out.println("Server running...");

        try {
            ServerSocket serverSocket = new ServerSocket(8189);//numarul portului
            while (true) {
                try {
                    new TCPConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCConnection exception" + e);
                }
            }//end while
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConection) {
     connections.add(tcpConection);
     sendToAllConnections("Client connected "+tcpConection);
    }

    @Override
    public synchronized void onRecieveString(TCPConnection topConection, String value) {
sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConection) {
connections.remove(tcpConection);
        sendToAllConnections("Client connected "+tcpConection);
    }

    @Override
    public synchronized void onException(TCPConnection topConnection, Exception e) {
System.out.println("TCP exception"+e);
    }
private void sendToAllConnections(String value) {
    System.out.println(value);
    for(int i=0;i<connections.size();i++){
       connections.get(i).sendString(value);
    }
}
}
