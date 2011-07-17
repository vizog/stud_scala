package test.domain

import domain._
import java.sql.Date

object testDomain {
  def main(args: Array[String]): Unit = {
    println("hello I am Testing");
//    testStudent
//    testTerm
//    
  }

  def testStudent() {
    var st = new Student
    st.name = "ali gholi"
    st.start
    st ! SayId
    st ! SayName
    st ! "boomBang"
    st ! exit
    st ! "gholaam"
  }

  def testTerm() {
	  val date = new Date(System.currentTimeMillis)
	  var term = new Term("1",date ,Nil )
	  term.start
  }
}
