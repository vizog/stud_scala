package repository
import domain.Student
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.SQLException
import domain.StudyRecord
import domain.Offering

object StudentRepository {

  def findById(id: String): Student = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from student where id='" + id + "'");

    var stud: Student = null;
    if (rs.next())
      stud = new Student(rs.getString("id"), rs.getString("name"), Nil);
    JDBCUtil.closeConnection(con);
    stud.start
    return stud
  }

  def save(stud: Student) {
    var con: Connection = JDBCUtil.getConnection();
    var st: Statement = con.createStatement();

    if ((st.executeQuery("select * from student where id = '" + stud.id + "'")).next())
      st.executeUpdate("update student set name = '" + stud.name + "' " +
        "where id = '" + stud.id + "'");
    else
      st.executeUpdate("insert into student values('" + stud.id + "', '" + stud.name + "')");
    JDBCUtil.closeConnection(con);

  }

  def findStudyRecords(stud: Student): List[StudyRecord] = {
    try {
      var con: Connection = JDBCUtil.getConnection();
      var st: Statement = con.createStatement();
      var rs: ResultSet = st.executeQuery("select * from study_record where student_id='" + stud.id + "'");

      var studyRecords: List[StudyRecord] = List.empty;
      while (rs.next()) {
        var offering: Offering = OfferingRepository.findById(rs.getString("offering_id"));
        studyRecords = new StudyRecord(rs.getDouble("grade"), offering) :: studyRecords;
      }
      JDBCUtil.closeConnection(con);
      return studyRecords;
    } catch {
      case ex: SQLException =>
        return null;
    }

  }

}