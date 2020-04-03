package ru.network;

import java.io.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TCPConnection {

    private  final Socket socket;//сокет который связан  с соединение TCP
    private final  Thread rxThread;//поток который "слушает" входящее содинение
    private final TCPConnectionListener eventListener;//слушатель событий
    private final BufferedReader in;//поток ввода
    private final BufferedWriter out;//поток вывода

    public TCPConnection(TCPConnectionListener eventListener, String ipAddr, int port) throws  IOException { //принимает готовый сокет
        this(eventListener, new Socket(ipAddr, port));//создает новый соке по параметрам ip, №port
    }

    public TCPConnection(TCPConnectionListener eventListener,Socket socket) throws IOException { //конструкторпринимает готовый объект сокета и с ним создает соединение
        this.eventListener = eventListener;
        this.socket = socket;                                        //
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        rxThread = new Thread(new Runnable(){
            @Override
            public void run() {//"слушаем" входящие соединения
                try {
                    eventListener.onConnectionReady(TCPConnection.this);
                    while(!rxThread.isInterrupted()) {
                        eventListener.onReceiveString(TCPConnection.this, in.readLine());
                    }

                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                }finally{
                    eventListener.onDisconnect(TCPConnection.this);//указываем что случился дисконект
                }
            }
        }); //создали новый поток
        rxThread.start();//запускаем его
    }
    public synchronized void sendString(String value) { //Синхронизируем для безопасного обращеия из разных потоков
        try {
            out.write(value + "\r\n");//возврат кретки и перевод строки
            out.flush();//сбрасывает все буферы и отпровляет
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);//если случилось исключение говорим евентлистнеру
            disconnect();
        }
    }
    private synchronized void disconnect() {
        rxThread.interrupt ();
        try {
            socket.close();
        } catch (IOException e) {
           eventListener.onException(TCPConnection.this, e);

        }
    }
    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress() + ": " + socket.getPort();
    }
}
