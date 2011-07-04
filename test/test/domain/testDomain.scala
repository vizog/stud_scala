package test.domain

import domain._
object testDomain {
  def main(args : Array[String]) : Unit = {
    println("hello I am Working");
    
    var st = new Student
    st.start
    st ! SayFirstName
    st ! SayLastName
    st ! "boomBang"
    st ! exit
    st ! "gholaam"
  }
}
