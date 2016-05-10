import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.sql.PreparedStatement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
 
public class Main {
	static DB p = new DB();
 
	public static void main(String[] args) throws SQLException, IOException {
		p.runSql2("TRUNCATE Record;");
		processPage("http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=1195517&newsearch=true&queryText=Deceptive%20Answer%20Prediction");
	}
 
	public static void processPage(String URL) throws SQLException, IOException{
		//check if the given URL is already in database
		String sql = "select * from Record where URL = '"+URL+"'";
		ResultSet rs = p.runSql(sql);
		if(rs.next()){
 
		}else{
			//store the URL to database to avoid parsing again
			sql = "INSERT INTO  `Web_crawler`.`Record` " + "(`URL`) VALUES " + "(?);";
			PreparedStatement stmt = p.conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, URL);
			stmt.execute();
 
			//get useful information
			Document doc = Jsoup.connect("http://ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=1195517&newsearch=true&queryText=Deceptive%20Answer%20Prediction/").get();
 
			if(doc.text().contains("Graph")){
				System.out.println("hello");
				System.out.println(URL);
			}
 
			//get all links and recursively call the processPage method
			Elements questions = doc.select("a[href]");
			for(Element link: questions){
				if(link.attr("href").contains("ieeexplore.ieee.org/xpl/articleDetails.jsp?arnumber=1195517&newsearch=true&queryText=Deceptive%20Answer%20Prediction"))
					processPage(link.attr("abs:href"));
			}
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
 
	public boolean runSql2(String sql) throws SQLException {
		Statement sta = conn.createStatement();
		return sta.execute(sql);
	}
 
	@Override
	protected void finalize() throws Throwable {
		if (conn != null || !conn.isClosed()) {
			conn.close();
		}
	}
}