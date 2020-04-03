package ru.chat.server;

import ru.network.TCPConnection;
import ru.network.TCPConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ChatServer implements TCPConnectionListener {// описываем класс
    public static void main(String[] args){
        new ChatServer();//создаем экземпляр
    }

    private final ArrayList<TCPConnection> connections = new ArrayList<>();

    private ChatServer() {//выполнение конструктора
        System.out.println("Server running...");
        try(ServerSocket serverSocket = new ServerSocket(8189)) {//создаем сокет который слушает тсп порт 8189
            while(true) {//в цикле
                try {
                    new TCPConnection(this, serverSocket.accept());//висим в методе аксепт
                } catch (IOException e) {                               //как только соединеие установиось он возвращает
                    System.out.println("TCPConnected exception: " + e);//объект сокета готовый который сним связан и передает в ТСП
                }
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connections.add(tcpConnection);
        sendToAllConnections("Client connected: "+ tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnections(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
    connections.remove(tcpConnection);
        sendToAllConnections("Client disconnected: "+ tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
System.out.println("TCPConnection exception: " + e);
    }
    private void sendToAllConnections(String value) {
        System.out.println(value);
        final int cnt = connections.size();
        for(int i = 0; i < cnt; i++) connections.get(i).sendString(value);
        }
    }
