package util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCUtil {

	// static final String CONN_STR = "jdbc:hsqldb:hsql://localhost/";
	static final String CONN_STR = "jdbc:mysql://localhost/edu_perf?user=edu&password=edu";
	static ComboPooledDataSource cpds;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			
			
			cpds = new ComboPooledDataSource();
			cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
			cpds.setJdbcUrl( CONN_STR );
			cpds.setUser("edu");
			cpds.setPassword("edu");

			// the settings below are optional -- c3p0 can work with defaults
			cpds.setMinPoolSize(20);
			cpds.setAcquireIncrement(10);
			cpds.setMaxPoolSize(145);

			
		} catch (ClassNotFoundException ex) {
			System.err.println("Unable to load MySQL JDBC driver");
			System.exit(1);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public  static Connection getConnection() throws SQLException {
		return cpds.getConnection();
				
//		return DriverManager.getConnection(CONN_STR);
	}

	public static void closeConnection(Connection con)
			throws SQLException {
		con.close();
	}
}
