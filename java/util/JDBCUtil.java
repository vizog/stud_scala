package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {

	// static final String CONN_STR = "jdbc:hsqldb:hsql://localhost/";
	static final String CONN_STR = "jdbc:mysql://localhost/test?user=root&password=root";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			System.err.println("Unable to load MySQL JDBC driver");
		}
	}

	public synchronized static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(CONN_STR);
	}

	public synchronized static void closeConnection(Connection con)
			throws SQLException {
		con.close();
	}
}
