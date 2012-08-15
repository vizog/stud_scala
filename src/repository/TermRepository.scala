package repository
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.SQLException
import domain.Term
import java.sql.PreparedStatement
import domain.TermRegulation

object TermRepository {

  def findByName(name: String): Term = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from term where name = '" + name + "'");
    var currentTerm: Term = null;
    if (rs.next()) {
      currentTerm = new Term(rs.getString("name"), rs.getDate("start_date"), Nil, null);
      val regId = rs.getString("regulation_id")
      var st1: PreparedStatement = con.prepareStatement("select * from term_regulation where id = ?");
      st1.setString(1, regId);
      st1.execute();
      rs = st1.getResultSet();
      if (rs.next()) {
        var termRegulation: TermRegulation = new TermRegulation(rs.getInt("max_allowed_units"), rs.getBoolean("retake_allowed"), rs.getBoolean("take_without_pres_allowed"));
        currentTerm.termRegulation = termRegulation;
        termRegulation.start()
      }

    }
    JDBCUtil.closeConnection(con);
    currentTerm.start()
    return currentTerm;
  }

  def getCurrentTerm(): Term = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from term order by start_date desc");
    var currentTerm: Term = null;
    if (rs.next()) {
      currentTerm = new Term(rs.getString("name"), rs.getDate("start_date"), Nil, null);
      val regId = rs.getString("regulation_id")
      var st1: PreparedStatement = con.prepareStatement("select * from term_regulation where id = ?");
      st1.setString(1, regId);
      st1.execute();
      rs = st1.getResultSet();
      if (rs.next()) {
        var termRegulation: TermRegulation = new TermRegulation(rs.getInt("max_allowed_units"), rs.getBoolean("retake_allowed"), rs.getBoolean("take_without_pres_allowed"));
        currentTerm.termRegulation = termRegulation;
        termRegulation.start()
      }

    }
    JDBCUtil.closeConnection(con);
    currentTerm.start()
    return currentTerm;
  }

}
  
  
