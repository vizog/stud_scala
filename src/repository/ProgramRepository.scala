package repository
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.SQLException
import domain.Term
import java.sql.PreparedStatement
import domain.TermRegulation
import domain.Requirement
import domain.Program
import domain.Course

object ProgramRepository {

  def findById(id: String): Program = {
    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from program where id = '" + id + "'");
    var prog: Program = null
    if (rs.next()) {
      prog = new Program(rs.getString("id"), Nil)
    }
    JDBCUtil.closeConnection(con);
//    prog.start()
    return prog;
  }

  def findProgramRequirements(progId: String): List[Requirement] = {
    try {
      var con: Connection = JDBCUtil.getConnection();
      var st: Statement = con.createStatement();
      var rs: ResultSet = st.executeQuery("select * from requirement where program_id='" + progId + "'");

      var requirements: List[Requirement] = List.empty;
      while (rs.next()) {
        val req = new Requirement(rs.getInt("id"), CourseRepository.findById(rs.getString("course_id")), findPrerequisitesForRequirement(rs.getInt("id")))
        req.start
        requirements = req :: requirements;
      }
      JDBCUtil.closeConnection(con);
      return requirements;
    } catch {
      case ex: SQLException =>
        return null;
    }

  }

  def findPrerequisitesForRequirement(reqId: Int): List[Requirement] = {
    try {
      var con: Connection = JDBCUtil.getConnection();
      var st: Statement = con.createStatement();
      var rs: ResultSet = st.executeQuery("select pre_id from prerequisites where req_id=" + reqId);
      var prerequisites: List[Requirement] = List.empty;
      while (rs.next()) {
        val req = findRequirementById(rs.getInt("pre_id"));
        req.start
        prerequisites = req :: prerequisites;
      }
      JDBCUtil.closeConnection(con);
      return prerequisites;
    } catch {
      case ex: SQLException =>
        ex.printStackTrace()
        return null;
    }
  }

  def findCourseRequirement(programId: String, course: Course): Requirement = {
    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from requirement where program_id = '" + programId + "' and course_id = '" + course.id + "'");
    var requirement: Requirement = null;
    if (rs.next()) {
      //      prog = new Term(rs.getString("name"), rs.getDate("start_date"), Nil, null);
      requirement = new Requirement(rs.getInt("id"), course, Nil)
      requirement.prerequisites = findPrerequisitesForRequirement(requirement.id)
    }
    JDBCUtil.closeConnection(con);
    requirement.start()
    return requirement;
  }

  def findRequirementById(reqId: Int): Requirement = {
    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from requirement where id = " + reqId);
    var requirement: Requirement = null;
    if (rs.next()) {
      requirement = new Requirement(rs.getInt("id"), CourseRepository.findById(rs.getString("course_id")), Nil)
    }
    JDBCUtil.closeConnection(con);
    requirement.start()
    return requirement;
  }

}
  
  
