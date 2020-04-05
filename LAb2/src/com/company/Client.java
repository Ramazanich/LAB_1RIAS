package com.company;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static void main(String[] args){
        Socket s;
        try{
            Scanner cin=new Scanner(System.in);
            s =new Socket("127.0.0.1", 8881);
            BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter pn=new PrintWriter(s.getOutputStream(),true);
            System.out.println("Добро пожаловать!");
            System.out.print("->");
            String get;
            do{
                get=cin.next();
                pn.println(get);
                System.out.println("Бот:" +br.readLine());
                System.out.println("->");

            }while(get.compareTo("Пока!")!=0);
            System.out.println("Пока");
        } catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
