package test.domain
import repository.CourseRepository
import repository.StudentRepository
import repository.TermRepository
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
import domain.HasPassed
import org.junit.Test
import junit.framework.Assert
import scala.actors.TIMEOUT
import org.junit.Before
import domain.TakeCourse
import domain.TakeCourseResponse
import domain.Offering
import repository.OfferingRepository
import domain.HasPassedPreReqs
import domain.StudentPassPreReqsActor
import domain.PassedPres
import domain.Term
import domain.TermOfferingsRequest
import domain.TermOfferingsResponse
import domain.GPARequest
import domain.GPAResponse

class TestDomain {

  var ap, math1, ds: Course = null
  var dsOffering, dmOffering, math11: Offering = null
  var pres: List[Course] = null
  var bebe: Student = null
  var term88_89_1, term88_89_2: Term = null

  @Before def initialize() {
    ap = CourseRepository.findById("ap")
    ap.preRequisites = CourseRepository.findPreRequisitesForCourse(ap)
    ap.start

    dsOffering = OfferingRepository.findById("ds1")
    dsOffering.start

    dmOffering = OfferingRepository.findById("dm1")
    dmOffering.start
    math11 = OfferingRepository.findById("math11")
    math11.start

    math1 = CourseRepository.findById("math1")
    math1.preRequisites = CourseRepository.findPreRequisitesForCourse(math1)
    math1.start

    ds = CourseRepository.findById("ds")
    ds.preRequisites = CourseRepository.findPreRequisitesForCourse(ds)
    ds.start

    bebe = StudentRepository.findById("bebe")
    bebe.studyRecords = StudentRepository.findStudyRecords(bebe)
    bebe.start

    term88_89_1 = TermRepository.findByName("88-89-1")
    term88_89_2 = TermRepository.findByName("88-89-2")

  }

  @Test def testStudentHasPassed1() {

    bebe ! HasPassed(ap, self)

    receiveWithin(2000) {

      case Passed(ap, result) =>
        Logger.getLogger(getClass()).debug("received final response: " + Passed(ap, result))
        Assert.assertEquals(result, true)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }
  }

  @Test def testStudentTakeCourse1() {

    bebe ! TakeCourse(dsOffering, self)

    receiveWithin(5000) {

      case TakeCourseResponse(result, comment) =>
        Logger.getLogger(getClass()).debug("received final response: " + TakeCourseResponse(result, comment))
        //bebe has not passed all prerequisites of ds  ( -> dm)
        Assert.assertEquals(result, false)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }

  }
  @Test def testStudentTakeCourse2() {

    bebe ! TakeCourse(dmOffering, self)

    receiveWithin(5000) {

      case TakeCourseResponse(result, comment) =>
        Logger.getLogger(getClass()).debug("received final response: " + TakeCourseResponse(result, comment))
        //should not be able to take dm (hasn't passed math1)
        Assert.assertEquals(result, false)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }
  }
  @Test def testStudentTakeCourse3() {

    bebe ! TakeCourse(math11, self)
    receiveWithin(5000) {

      case TakeCourseResponse(result, comment) =>
        Logger.getLogger(getClass()).debug("received final response: " + TakeCourseResponse(result, comment))
        //bebe has already taken math1, should reply false
        Assert.assertEquals(result, false)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }
  }

  @Test def testStudentHasPassed2() {

    bebe ! HasPassed(math1, self)

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

  @Test def testStudentHasPassedPres() {

    val coursePassPres = new StudentPassPreReqsActor(bebe)
    coursePassPres.start
    coursePassPres ! HasPassedPreReqs(ds, self);

    receiveWithin(500) {
      case PassedPres(ds, result) =>
        Logger.getLogger(getClass()).debug("received final response: " + PassedPres(ds, result))
        Assert.assertEquals(result, false)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }
  }

  @Test def testStudentHasPassed3() {
    bebe ! HasPassed(ds, self)

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

  @Test def testOfferingList() {

    term88_89_1 ! TermOfferingsRequest
    receiveWithin(5000) {
      case TermOfferingsResponse(offerings: List[Offering]) =>
        Logger.getLogger(getClass()).debug("received final response: " + TermOfferingsResponse(offerings))
        Assert.assertNotNull(offerings)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }

  }

  @Test def testGPA1() {

    bebe ! GPARequest(null, term88_89_1, self, null)
    receiveWithin(10000) {
      case GPAResponse(gpa: Double) =>
        Logger.getLogger(getClass()).debug("received final response: " + GPAResponse(gpa))
        //bebe has already taken math1, should reply false
        Assert.assertEquals(gpa, 12.8)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }

  }

  @Test def testGPA2() {

    bebe ! GPARequest(null, term88_89_2, self, null)
    receiveWithin(10000) {
      case GPAResponse(gpa: Double) =>
        Logger.getLogger(getClass()).debug("received final response: " + GPAResponse(gpa))
        //bebe has already taken math1, should reply false
        Assert.assertEquals(gpa, 0.0)

      case TIMEOUT =>
        Assert.fail("time out")

      case a: Any =>
        Assert.fail("received other:" + a)
    }

  }

}