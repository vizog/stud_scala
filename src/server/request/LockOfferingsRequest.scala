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
import repository.OfferingRepository
import domain.TakeCourse
import domain.TakeCourseResponse
import domain.LockAllOfferingsOfTerm

class LockOfferingsRequest(var reqId: String, var termId: String) extends Request(reqId) {

  private var term: Term = null

  def init() = {
    term = TermRepository.findByName(termId)
    term.start
  }
  override def act() {

    loop {
      react {
        case "start" =>
          val start = System.currentTimeMillis();
//          println("start:  " + start)
          term ! LockAllOfferingsOfTerm(start)
      }
    }
  }
}