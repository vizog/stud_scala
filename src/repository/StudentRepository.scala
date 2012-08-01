package repository
import domain.Student
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.SQLException
import domain.StudyRecord
import domain.Offering
import java.sql.PreparedStatement
import util.LoggingSupport

object StudentRepository {

  def findById(id: String): Student = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from student where id='" + id + "'");

    var stud: Student = null;
    if (rs.next()) {
      stud = new Student(rs.getString("id"), rs.getString("name"), Nil);
      stud.start
    }
    JDBCUtil.closeConnection(con);
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
        val str: StudyRecord = new StudyRecord(rs.getDouble("grade"), offering)
        str.start
        
        studyRecords = str :: studyRecords;
      }
      JDBCUtil.closeConnection(con);
      return studyRecords;
    } catch {
      case ex: SQLException =>
        return null;
    }

  }
  
  def saveStudyRecord(stud: Student, sr:StudyRecord) = {
		  try {
			  var con: Connection = JDBCUtil.getConnection();
		  var st: PreparedStatement = con.prepareStatement("insert into study_record(student_id,offering_id,grade) values(?,?,?)");
		  st.setString(1,stud.id);
		  st.setString(2,sr.offering.id);
		  st.setInt(3,-1);
		  st.execute()
		  //debug("saved studyRecord: " + sr)
		  JDBCUtil.closeConnection(con);
		  } catch {
		  case ex: SQLException =>
		    ex.printStackTrace()
		  }
		  
  }

}