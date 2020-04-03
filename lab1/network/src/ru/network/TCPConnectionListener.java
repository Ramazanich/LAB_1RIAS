package ru.network;

public interface TCPConnectionListener {

    void onConnectionReady(TCPConnection tcpConnection);//если соединение готово
    void onReceiveString(TCPConnection tcpConnection, String value);//если внезапно приняли строчку
    void onDisconnect(TCPConnection tcpConnection);//Дисконект(соединение пропало)
    void onException(TCPConnection tcpConnection, Exception e);//если случилось исключение или чт-то пошло не так

}
