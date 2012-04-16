package domain

import scala.actors.Actor;
import scala.actors.Actor._;

class StudyRecord(
  var grade: Double,
  var offering: Offering) extends BaseDomainClass {

  def act() {
    loop {
      react {
        case SayGrade =>
          println(grade)

        case AreYouAPassCourseRequest(course, target) =>
          debug("[StudyRec:" + this + "] received: AreYouAPassCourseRecord(" + course + ", " + target + ")");
          if (grade < 10) {
            debug("[StudyRec:" + this + "] sent: Passed(" + course + ", false) to " + target);
            target ! Passed(course, false);
          } else {
            offering ! HasPassed(course, target)
            debug("[StudyRec:" + this + "] sent: HasPassed2(" + course + ", " + target + ") + to " + offering);
          }
        //        case AreYouAPassCourseRecord(course, target) =>
        //          debug("[StudyRec:" + this + "] received: AreYouAPassCourseRecord("+course+", "+target+")");
        //          if (grade < 10) {
        //            debug("[StudyRec:" + this + "] sent: Passed(" + course + ", false) to " + target);
        //            target !Passed(course, false);
        ////           	reply(Passed(course, false));
        //          }
        //          else {
        //            val res = offering !? IsYourCourse(course, self)
        //            debug("[StudyRec:" + this + "] sent: Passed(" + course + ", " + res + ") + to " + target);
        //            target ! Passed(course, res.asInstanceOf[Boolean])
        ////            reply(Passed(course, res.asInstanceOf[Boolean]))
        //          }
        case exit =>
          exit
      }
    }
  }

  override def toString(): String = {
    return offering.id + "->" + grade
  }
}