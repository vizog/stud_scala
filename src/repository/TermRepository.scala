package repository
import util.JDBCUtil
import java.sql.Connection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.SQLException
import domain.Term

object TermRepository {

  def findByName(name: String): Term = {

    var con: Connection = JDBCUtil.getConnection
    var st: Statement = con.createStatement
    var rs: ResultSet = st.executeQuery("select * from term where name = '" + name + "'");
    var currentTerm: Term = null;
    if (rs.next())
      currentTerm = new Term(rs.getString("name"), rs.getDate("start_date"), Nil);
    JDBCUtil.closeConnection(con);
    currentTerm.start()
    return currentTerm;
  }

}
  
  
