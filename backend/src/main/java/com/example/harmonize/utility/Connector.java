package com.example.harmonize.utility;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.Socket;


public class Connector {
    //Socket

    public String SocketCall(String filename, String id, Long order){
        BufferedReader in = null;
        PrintWriter out = null;

        Socket socket = null;

        try{
            socket = new Socket("127.0.0.1", 9999);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            String sendMessage = "1024"+ filename +"/"+id+"/"+Long.toString(order);
            System.out.println("result : " + sendMessage);
            out.println(sendMessage);
            out.flush();

            String inputMessage = in.readLine();
            System.out.println("From Server: " + inputMessage);
            return inputMessage;

        }catch (IOException e){
            System.out.println(e.getMessage());
            return null;

        }finally {
            try{
                if(socket != null){
                    socket.close();
                    System.out.println("Sever disconnected");
                }
            }catch (IOException e){
                System.out.println("Socket communication Error");
            }
        }
    }

}
