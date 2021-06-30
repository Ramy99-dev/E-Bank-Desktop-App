package application.database;
import java.sql.*;


public class Database {
	private Connection conn;
	public Connection connect()  throws ClassNotFoundException, SQLException
	{
		if(conn == null)
		{
			String url ="jdbc:mysql://localhost:3306/javafx_project";
			String username ="root";
			String password =""; 
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url,username,password);
			return conn ;
		}
		return conn ;
		
	}
    

}
