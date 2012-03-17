package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

	// static final String CONN_STR = "jdbc:hsqldb:hsql://localhost/";
	static final String CONN_STR = "jdbc:mysql://localhost/edu?user=edu&password=edu";
//	static final String CONN_STR = "jdbc:h2:tcp://localhost:9092/~/edu";
//	static final String CONN_STR = "jdbc:h2:~/edu";

	static {
		try {
//			Class.forName("com.mysql.jdbc.Driver");
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException ex) {
			System.err.println("Unable to load MySQL JDBC driver");
		}
	}

	public synchronized static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CONN_STR,"edu","edu");
	}

	public synchronized static void closeConnection(Connection con)
			throws SQLException {
		con.close();
	}
}
