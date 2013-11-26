package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnUtil {
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}	// static ±¸¹®
	
	public static Connection getConnection() throws SQLException {
		String url ="jdbc:oracle:thin:@localhost:1521:xe";
		String user ="game";		// 
		String password="game";		//
		
		return DriverManager.getConnection(url, user, password);
	}
	
	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		if(rs !=null) {
			try {
				rs.close();
			}catch(Exception e){
			}
		}
		
		if(ps !=null) {
			try {
				ps.close();
			}catch(Exception e) {
			}
		}
		
		if(conn != null) {
			try {
				conn.close();
			}catch(Exception e) {
			}
		}
	} // end close();
	
	public static void close(Connection conn, PreparedStatement ps ) {
		close(conn, ps, null);
	}
}







