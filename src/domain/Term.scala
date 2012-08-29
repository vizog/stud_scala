package domain
import actors.Actor
import java.sql.Date;
import repository.OfferingRepository

class Term(
  var name: String,
  var startDate: Date,
  var offerings: List[Offering], var termRegulation: TermRegulation) extends BaseDomain {

  override def act() {
	  loop {
	    react {
	      
        case TermOfferingsRequest =>
          /* */debug(this + " received " + TermOfferingsRequest)
          sender ! TermOfferingsResponse(OfferingRepository.listTermOfferings(this))

        case CourseGradeRequest(term: Term, target: Actor, CourseGradeResponse(false, grade, name, units)) =>
          /* */debug(this + " received " + CourseGradeRequest(term: Term, target: Actor, CourseGradeResponse(false, grade, name, units))  )
            
          if(term.name.equals(this.name)) {
            //notify the sender that the request is for the this term, send it back to offering so it forwards it to course.
            sender ! CourseGradeRequest(term, target,  CourseGradeResponse(true, grade, name, units))
            /* */debug(this + " sent " + CourseGradeRequest(term, target,  CourseGradeResponse(true, grade, name, units)) + " to " + sender )
           
          } else {
            //the request is not for this term so it should be ignored.
            //notify the coordinator so it does not keep waiting for response
            target ! CourseGradeResponse(false, 0, "", 0)
            /* */debug(this + " sent " + CourseGradeResponse(false, 0, null, 0) + " to " + target )
            
          }
          
        case LockAllOfferingsOfTerm(startTime) =>
          if(offerings == Nil)
            offerings = OfferingRepository.listTermOfferings(this);
          //this is only done so that we can measure response time:
          val replies = for(offering <- offerings) yield {
            offering.start
            offering !! LockOffering 
          }
          for(i <- 1 to offerings.size)
            replies(i-1)
//            println("finish: " + System.currentTimeMillis())
            println(System.currentTimeMillis() - startTime)
            
	    }
	    
	  }
  
  }
   override def toString = "[Term:" + name + "]"
}
case object LockOffering