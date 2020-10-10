package ro.roma.network;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.io.*;


public class TCPConnection {
    private final  Socket socket;
    private final Thread rxThread;
    private final TCPConnectionListener evenListener;
    private final BufferedReader in;
    private final BufferedWriter out;

    public TCPConnection(TCPConnectionListener evenListener, String ipAdr, int port) throws IOException{
       this(evenListener,new Socket(ipAdr,port));//acest constructor creaza un  socket dupa parametrii ipadresa si port
    }//second constructor


    public TCPConnection(TCPConnectionListener evenListener,Socket socket) throws IOException
    {
this.socket=socket;
this.evenListener=evenListener;
socket.getInputStream();
in =new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));//flux de intrare
out =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));//flux de iesire
rxThread=new Thread(new Runnable() {//cream clasa care  realizeaza interfata runnable
    @Override
    public void run() {
        try {
            evenListener.onConnectionReady(TCPConnection.this);
            while (!rxThread.isInterrupted()) {
                evenListener.onRecieveString(TCPConnection.this, in.readLine());
            }

    }catch(IOException  e){
            evenListener.onException(TCPConnection.this, e);
        }finally {
evenListener.onDisconnect(TCPConnection.this);
        }
    }
});//fluxul care asculta intrarea conexiunii
rxThread.start();//il pornim
    }
    public synchronized void sendString(String value){//synchronized vizibil oriunde
try{
        out.write(value+"\r\n");
        out.flush();//impunem sa se trimita mesajul
}catch (IOException e){
    evenListener.onException(TCPConnection.this,e);
    disconect();
}
    }
    public synchronized void disconect(){
        rxThread.interrupt();
        try{
        socket.close();
        }catch (IOException e){
            evenListener.onException(TCPConnection.this,e);
        }
    }
@Override
    public String toString(){
        return "TCPCPnnection"+ socket.getInetAddress()+": "+socket.getPort();//returneaza adresa si portul
}


}
