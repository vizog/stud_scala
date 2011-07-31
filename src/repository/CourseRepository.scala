package repository
import domain.Course
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet

object CourseRepository {

  def findById(id: String): Course = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from course where id='" + id + "'");

    var crs: Course = null;
    if (rs.next())
      crs = new Course(rs.getString("id"), rs.getString("name"), rs.getInt("units"), Nil);

    JDBCUtil.closeConnection(con);
    crs.start
    return crs
  }
  
  	def save(course: Course) {
			var con: Connection = JDBCUtil.getConnection();
			var st: Statement = con.createStatement();
			
			if ((st.executeQuery("select * from course where id = '" + course.id + "'")).next())
				st.executeUpdate("update course set name = '" + course.name + "', units = 3 " + 
						"where id = '" + course.id + "'");
			else
				st.executeUpdate("insert into course values('" + course.id + "', '" + course.name + "', 3)");
			JDBCUtil.closeConnection(con);
					
	}
	


  
  
  
  
  
  
  
  
  
  
  
  
}