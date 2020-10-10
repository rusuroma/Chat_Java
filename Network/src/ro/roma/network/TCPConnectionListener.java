package ro.roma.network;

public interface TCPConnectionListener {


    void onConnectionReady(TCPConnection tcpConection);//pornirea conectiunii
    void onRecieveString(TCPConnection tcpConection,String value);//conectiunea primeste stringul
    void onDisconnect(TCPConnection tcpConection);//conexiunea s-a intrerupt
    void onException(TCPConnection tcpConnection,Exception e);//apar erori

}
