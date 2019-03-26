
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

public class DataBase {
	Connection conn;
	final String DB_URL = "jdbc:mysql://localhost:3306/flea_market?useSSL=true";
	
    final String USER = "root";
    final String PASS = "123456";
  DataBase(){
       try {
           conn = DriverManager.getConnection(DB_URL,USER,PASS);
       }
       catch(SQLException e) {
       	//System.out.println(e);
       }
}
 Statement createsql() {
	 Statement sql=null;
	 try {
		 sql=conn.createStatement();
	 }
	 catch(SQLException e) {}
   return sql;
}

 ResultSet getresult(Statement sql,String query) {
	 ResultSet rs=null;
	 try {
		 rs = sql.executeQuery(query) ;
		
		}
	catch(SQLException e) {}
  return rs;
	 
}
void close() {
	
	 try {
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
}

}
