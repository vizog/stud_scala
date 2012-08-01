package server.request

import scala.actors.TIMEOUT
import org.apache.log4j.Logger
import domain.GPAResponse
import domain.Student
import domain.Term
import junit.framework.Assert
import repository.StudentRepository
import repository.TermRepository
import domain.GPARequest
import scala.actors.Actor._

//messages:

 class GPARequest(var reqId: String) extends Request(reqId) {


  //this should be overridden by concrete requests
  
  override def act() {
    val start = System.currentTimeMillis();
    debug(id + " START    -> " + start);

     var bebe: Student = null
    var term88_89_1: Term = null
    bebe = StudentRepository.findById("bebe")
    bebe.studyRecords = StudentRepository.findStudyRecords(bebe)
    bebe.start

    term88_89_1 = TermRepository.findByName("88-89-1")

    bebe ! GPARequest(null, term88_89_1, self, null)
    react {
      case GPAResponse(gpa: Double) =>
        Logger.getLogger(getClass()).debug("received final response: " + GPAResponse(gpa))
         val end = System.currentTimeMillis();
        debug(id + " END      -> " + end);
    	info(id + " RESPONSE -> " + (end - start));
      case TIMEOUT => 
        info("TIMEOUT __________________________________________")
      case a: Any =>
      info("OTHER __________________________________________" + a)
      Assert.fail("received other:" + a)
    }
  }
  

  override def toString(): String = {
    return "Request: [" + id + "]"
  }
}