package com.company;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
     static ServerSocket socket;
     int k;
     public static void main(String[] args) {
         server s2 = new server();
         s2.serve();
     }
        void serve() {
            try {
                socket = new ServerSocket(8881);
                k = 0;
                while (true) {
                    Socket s = socket.accept();
                    thread t = new thread(s);
                    t.start();
                    k++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
         class thread extends Thread{
             Socket sl;
             int i;
             public thread(Socket s){
                 sl = s;
                 i=0;
             }
             public void run(){
                 try{
                    String get ="y";
                    BufferedReader bl = new BufferedReader(new InputStreamReader(sl.getInputStream()));
                    PrintWriter pn=new PrintWriter(sl.getOutputStream(),true);
                    while(true){
                        get= bl.readLine();
                        if(get.equals("Пока!")){
                            break;
                        }
                        if(get!=null) {
                            pn.println(get);
                            i++;
                        }
                        }
                        k--;
                        System.out.println("Номер клинта"+i+"И номер подключенного клиента"+k);
                    } catch(IOException e){
                         e.printStackTrace();
                     }
             }
         }
     
    }
