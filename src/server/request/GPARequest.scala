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

class GPARequest(var reqId: String, var studId: String) extends Request(reqId) {

  override def act() {

	  val start = System.currentTimeMillis();
    var bebe = StudentRepository.findById(studId)
//    info("" + (System.currentTimeMillis() - start))
    var term88_89_1 = TermRepository.findByName("term-1")

    bebe.start
    bebe ! GPARequest(null, term88_89_1, self, null)
    react {
      case GPAResponse(gpa: Double) =>
//        Logger.getLogger(getClass()).debug("received final response: " + GPAResponse(gpa))
        val end = System.currentTimeMillis();
//        debug(id + " END      -> " + end);
//        info(";" + id + ";" + (end - start));
        println(end - start + "," + end)
    }
  }

  override def toString(): String = {
    return "Request: [" + id + "]"
  }
}