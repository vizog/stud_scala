package repository
import domain.Course
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.SQLException

object CourseRepository {

  def findById(id: String): Course = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from course where id='" + id + "'");

    var crs: Course = null;
    if (rs.next()) {
      crs = new Course(rs.getString("id"), rs.getString("name"), rs.getInt("units"), Nil);
//      crs.start
    }

    //add prerequisites
    //    rs = st.executeQuery("select * from course where pre_of='" + id + "'");
    //    var pres: List[Course] =  List.empty;
    //    while(rs.next())
    //    	pres =  (new Course(rs.getString("id"), rs.getString("name"), rs.getInt("units"), Nil)) :: pres;
    //    
    //    crs.preRequisites = pres
    //    
    JDBCUtil.closeConnection(con);
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

  def findPreRequisitesForCourse(course: Course): List[Course] = {
    try {
      var con: Connection = JDBCUtil.getConnection();
      var st: Statement = con.createStatement();
      var rs: ResultSet = st.executeQuery("select * from prerequisites where course_id='" + course.id + "'");

      var prerequisites: List[Course] = List.empty;
      while (rs.next()) {
        prerequisites = (findById(rs.getString("pre_id"))) :: prerequisites;
      }
      JDBCUtil.closeConnection(con);
      return prerequisites;
    } catch {
      case ex: SQLException =>
        return null;
    }

  }

}