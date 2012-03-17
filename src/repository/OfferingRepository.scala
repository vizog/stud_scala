package repository
import domain.Offering
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.SQLException
import domain.Course
import domain.Course
import domain.Term

object OfferingRepository {

  def findById(id: String): Offering = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from offering where id='" + id + "'");

    var offering: Offering = null;
    if (rs.next()) {
      var crs:Course = CourseRepository.findById(rs.getString("course_id"));
      crs.preRequisites = CourseRepository.findPreRequisitesForCourse(crs);
      var term: Term = TermRepository.findByName(rs.getString("term_name"));
      offering = new Offering(id, crs, rs.getInt("section") , rs.getDate("exam_date"), term );
      offering.start
      
    }
    con.close();
    return offering;
  }

}