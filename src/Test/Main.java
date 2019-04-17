package Test;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main
{
    private Socket socket;

    public Main(Socket socket)
    {
        this.socket = socket;
    }
}