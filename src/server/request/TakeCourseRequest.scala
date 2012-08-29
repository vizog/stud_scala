package server.request

import domain.Offering
import domain.Student
import domain.TakeCourse
import domain.TakeCourseResponse
import repository.OfferingRepository
import repository.StudentRepository

class TakeCourseRequest(var reqId: String, var studId: String) extends Request(reqId) {

  private var bebe: Student = null
  private var offering: Offering = null
  private var startTime: Long = 0
  def init() = {
    bebe = StudentRepository.findById(studId)
    //    info("" + (System.currentTimeMillis() - start))

    offering = OfferingRepository.findById("course-5-3")
    offering.start

    bebe.start

  }

  override def act() {

    loop {

      react {

        case "start" =>
          startTime = System.currentTimeMillis();
          bebe ! TakeCourse(offering, this)

        case TakeCourseResponse(result, comment) =>
          val end = System.currentTimeMillis();
          println(end - startTime)
//          println(end - startTime + "," + end)
          exit

      }
    }
  }

  override def toString(): String = {
    return "Request: [" + id + "]"
  }
}