package test.performance
import repository.CourseRepository;
import repository.StudentRepository;
import scala.actors.Actor;
import scala.actors.Actor._;
import java.util.Date;
import domain.Course;
import domain.SayName;
import domain.ChangeName;
import domain.Save;
import domain.Student;
import domain.HasPassed1;
import domain.HasPassedPreReqs;

object PerfTest extends {

  def main(args: Array[String]): Unit = {

	var size: Int = new Integer(args(0));
    var actors: List[Student] = List(new Student ("0", "name 0", Nil))
    for (i <- 1 until size){
     actors = actors ::: List(new Student(i.toString(), "name " + i, Nil));
    }
    println(new Date())
    for(a <- actors) {
      a.start()
    }
    
    println(new Date())
    for(a <- actors) {
    	a ! "sayName"
    }

  }

}