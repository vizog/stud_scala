package domain
import actors.Actor
import java.sql.Date;
import repository.OfferingRepository

class Requirement(
  var id: Int,
  var course: Course,
  var prerequisites: List[Requirement]) extends BaseDomain {

  override def act() {
	  loop {
	    react {
	      case  HasPassedPreReqs(student, course, target)  =>
	        val passPresActor = new StudentPassPreReqsActor(student, this, target)
	        passPresActor.start
	    }
	    
	  }
  
  }
   override def toString = "[Requirement[" + id + "] course-> " + course + "]"
}