package server.request

import domain.Course
import domain.HasPassed
import domain.Passed
import domain.Student
import repository.CourseRepository
import repository.StudentRepository

class CheckPassCourseRequest(var reqId: String, var studId: String) extends Request(reqId) {

  private var bebe: Student = null
  private var course: Course = null
  private var startTime: Long = 0
  def init() = {
    bebe = StudentRepository.findById(studId)
    //    info("" + (System.currentTimeMillis() - start))

    course = CourseRepository.findById("course-5-2")
    course.start

    bebe.start

  }

  override def act() {

    loop {

      react {

        case "start" =>
          startTime = System.currentTimeMillis();
          bebe ! HasPassed(course, this)

        case Passed(course, true) =>
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