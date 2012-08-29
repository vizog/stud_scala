package server.request

import java.util.Date

object LoadRunner extends App {

//  takeCourse;
  //  lockOfferings
  //  gpa
  passedCourse

  def passedCourse() = {

    new Thread() {

      override def run() {
        println("start = " + System.currentTimeMillis())
        var reqs: List[CheckPassCourseRequest] = List.empty;
        for (i <- 1 to 1000) {
          val request = new CheckPassCourseRequest("req-" + i, "student-" + i);
          request.init
          request.start
          reqs = request :: reqs 
          Thread.sleep(10);
        }
        for (req <- reqs) {
          req ! "start"
          Thread.sleep(100);
        }

      }
    }.start
  }


  def takeCourse() = {

    new Thread() {

      override def run() {
        println("start = " + System.currentTimeMillis())
        var reqs: List[TakeCourseRequest] = List.empty;
        for (i <- 1 to 1000) {
          val request = new TakeCourseRequest("req-" + i, "student-" + i);
          request.init
          request.start
          reqs = request :: reqs 
          Thread.sleep(10);
        }
        for (req <- reqs) {
          req ! "start"
          Thread.sleep(100);
        }

      }
    }.start
  }

  def lockOfferings() = {
    new Thread() {

      override def run() {

        val request = new LockOfferingsRequest("req-1", "term-1 ");
        request.init();
        request.start
        for (i <- 1 to 10000) {
          request ! "start";
          Thread.sleep(2000);
        }
      }
    }.start

  }

  def gpa() = {

    new Thread() {

      override def run() {
        for (i <- 1 to 1000) {
          val request = new GPARequest("req-" + i, "student-" + i);
          request.start
          Thread.sleep(100);
        }

      }
    }.start
  }
}