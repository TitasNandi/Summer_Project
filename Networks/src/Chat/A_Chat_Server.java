package Chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class A_Chat_Server 
{
    public static ArrayList<Socket> ConnectionArray = new ArrayList<Socket>();
    public static ArrayList<String> CurrentUsers = new ArrayList<String>();
    //public static ArrayList<String> Passwords = new ArrayList<String>();
    
    public static void main(String[] args) throws IOException
    {
        
        try
        {
            final int PORT = 1808;
            ServerSocket SERVER = new ServerSocket(PORT);
            System.out.println("Waiting for clients...");
            
            while(true)
            {
                Socket SOCK = SERVER.accept();
                ConnectionArray.add(SOCK);
                
                System.out.println("Client connected from: " + SOCK.getLocalAddress().getHostName());
                
                AddUserName(SOCK);
                
                A_Chat_Server_Return CHAT = new A_Chat_Server_Return(SOCK);
                Thread X = new Thread(CHAT);
                X.start();
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    
    public static void AddUserName(Socket X) throws IOException
    {
        Scanner INPUT = new Scanner(X.getInputStream());
        String UserName = INPUT.nextLine();
        CurrentUsers.add(UserName);
      //  String Pass = INPUT.nextLine();
        //Passwords.add(Pass);
        for(int i = 1; i <= A_Chat_Server.ConnectionArray.size(); i++)
        {
            Socket TEMP_SOCK = (Socket)A_Chat_Server.ConnectionArray.get(i-1);
            PrintWriter OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
            OUT.println("#?!" + CurrentUsers);
            OUT.flush();
        }
    }
    
}
