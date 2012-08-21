package domain
import actors.Actor
import java.sql.Date
import repository.OfferingRepository
import repository.ProgramRepository

class Program(
  var id: String,
  var requirements: List[Requirement]) extends BaseDomain {

  override def act() {
	  loop {
	    react {
	      case HasPassedPreReqs(student, course, target) =>
	        val req = ProgramRepository.findCourseRequirement(id, course);
	        req ! HasPassedPreReqs(student, course, target) 
	        
	    }
	    
	  }
  
  }
   override def toString = "[Program:" + id + "]"
}