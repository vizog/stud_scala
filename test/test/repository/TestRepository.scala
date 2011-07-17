package test.repository
import repository.CourseRepository
import domain.SayName

object TestRepository {
  
   def main(args: Array[String]): Unit = {
    println("hello I am Testing");
    testCourseRepo
    
//    
  }
   
   def testCourseRepo() {
	var course = CourseRepository.findById("ap")
	course ! SayName
   }
}