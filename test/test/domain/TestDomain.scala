package test.domain
import repository.CourseRepository
import repository.StudentRepository
import scala.actors.Actor
import scala.actors.Actor._
import java.util.Date
import domain.Course
import domain.SayName
import domain.ChangeName
import domain.Save
import domain.Student
import domain.HasPassedPreReqs
import domain.HasPassedPreReqs_FINE_GRAINED
import domain.Passed
import util.LoggingSupport
import org.apache.log4j.Logger
import domain.HasPassed3
import org.junit.Test
import junit.framework.Assert
import scala.actors.TIMEOUT
import org.junit.Before

class TestDomain {

  var ap, math1, ds: Course = null
  var pres: List[Course] = null
  var bebe: Student = null

  @Before def initialize() {
    ap = CourseRepository.findById("ap")
    ap.preRequisites = CourseRepository.findPreRequisitesForCourse(ap)
    ap.start

    math1 = CourseRepository.findById("math1")
    math1.preRequisites = CourseRepository.findPreRequisitesForCourse(math1)
    math1.start

    ds = CourseRepository.findById("ds")
    ds.preRequisites = CourseRepository.findPreRequisitesForCourse(ds)
    ds.start

    bebe = StudentRepository.findById("bebe")
    bebe.studyRecords = StudentRepository.findStudyRecords(bebe)
    bebe.start

  }

  @Test def testStudentHasPassed1() {

    bebe ! HasPassed3(ap, self)

    receiveWithin(500) {

      case Passed(ap, result) =>
        Logger.getLogger(getClass()).debug("received final response: " + Passed(ap, result))
        Assert.assertEquals(result, true)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }
  }
  
  @Test def testStudentHasPassed2() {

    bebe ! HasPassed3(math1, self)

    receiveWithin(500) {
      case Passed(math11, result) =>
        Logger.getLogger(getClass()).debug("received final response: " + Passed(math11, result))
        Assert.assertEquals(result, false)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }
  }
  
  @Test def testStudentHasPassed3() {
    bebe ! HasPassed3(ds, self)

    receiveWithin(500) {
      case Passed(ds, result) =>
        Logger.getLogger(getClass()).debug("received final response: " + Passed(ds, result))
        Assert.assertEquals(result, false)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }

  }

}