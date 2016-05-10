package Chat;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Server 
{
	static ArrayList<Client> client = new ArrayList<Client>();
	static DB db = new DB(); 
	public static void main(String args[])
	{
		try
        {
            final int port = 1808;
            ServerSocket SERVER = null;
            try
            {
            	SERVER = new ServerSocket(port);
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }
            System.out.println("Waiting for clients...");
            
            while(true)
            {
            	Socket SOCK = null;
            	try
            	{
            		SOCK = SERVER.accept();
            		Client CHAT = new Client(SOCK);
            		client.add(CHAT);
                    Thread X = new Thread(CHAT);
                    X.start();
            	}
            	catch(Exception ex)
                {
                	ex.printStackTrace();
                }
                //ConnectionArray.add(SOCK);
                
                //System.out.println("Client connected from: " + SOCK.getLocalAddress().getHostName());
                
                //AddUserName(SOCK);
                
                
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
	}
	

}

class Client implements Runnable
{
	Socket sock;
	String username;
	String password;
	PrintWriter p;
	Scanner s;
	public Client(Socket sock) throws SQLException,IOException
	{
		this.sock = sock;
//		GetInfoFromUser(sock);
	}
	public void GetInfoFromUser(Socket sock) throws SQLException,IOException
	{
		s = new Scanner(sock.getInputStream());
		String UserName = s.nextLine();
		GetData(UserName,s);
		//String Password = s.nextLine();
	}
	public void GetData(String UserName, Scanner s) throws SQLException, IOException
	{
		String sql = "select * from Record where UserName = '"+UserName+"'";
		ResultSet rs = Server.db.runSql(sql);
		p = new PrintWriter(sock.getOutputStream());
		if(rs.next())
		{
			p.println("UserName already in use, try again");
		}
		else
		{
			this.username = UserName;
			this.password = s.nextLine();
			String sql_1 = "INSERT INTO Record " +
	                   "VALUES (" +this.username +", " + this.password + ")";
			Server.db.runSql2(sql_1);
			p.println("User successfully created");
			System.out.println("Client connected from: " + sock.getLocalAddress().getHostName());
		}
		}
	public void run()
	{
		try {
			s = new Scanner(sock.getInputStream());
			p = new PrintWriter(sock.getOutputStream());
			GetInfoFromUser(sock);
			while(true)
			{
				String To = s.nextLine();
				String sql = "select * from Record where UserName = '"+To+"'";
				ResultSet rs = Server.db.runSql(sql);
				if(rs.next())
				{
					for(int i=0; i<Server.client.size(); i++)
					{
						if(Server.client.get(i).username==To)
						{
							if(!Server.client.get(i).sock.isConnected())
							{
								p.println("Can't send message, User offline");
							}
							else
							{
								String Msg = s.nextLine();
								Client c = (Client) Server.client.get(i);
								PrintWriter TEMP_OUT = new PrintWriter(c.sock.getOutputStream());
		                        TEMP_OUT.println(Msg);
		                        TEMP_OUT.flush();
							}
						}
					}
				}
				else
				{
					p.println("User to send message does not exist");
				}
			}
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class DB {
	 
	public Connection conn = null;
 
	public DB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/Web_crawler";
			conn = DriverManager.getConnection(url, "root", "");
			System.out.println("conn built");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
 
	public ResultSet runSql(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.executeQuery(sql);
	}
 
	public void runSql2(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		sta.executeUpdate(sql);
	}
 
	@Override
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}
}
