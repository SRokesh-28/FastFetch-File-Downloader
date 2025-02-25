package User;

import java.sql.*;

public class DBconnection {
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/dynamic_download", "root", "root");
	}
}
