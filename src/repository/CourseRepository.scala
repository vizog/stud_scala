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
}