package Test;

import Socket.Mysocket;

import java.io.IOException;

public class Main{
    public static void main(String[]args) throws IOException
    {
        Mysocket serverSocket=new Mysocket();
        serverSocket.service();
    }
}