package domain

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors.OutputChannel
import repository.StudentRepository

class StudentComputeTermGPAActor() extends BaseDomain {

  private var student: Student = null
  private var term: Term = null
  private var offering: Offering = null
  private var target: Actor = null
  private var numOfResponses: Int = 0
  private var numOfExpectedResponses: Int = 0
  private var gpa: Double = 0
  private var totalUnits: Int = 0
  private var transcript: List[String] = List.empty

  override def act() {
    loop {
      (
        react({
          case GPARequest(st: Student, term: Term, target_, null) => sendOutRequestsToStudyRecs(st, term, target_)

          case CourseGradeResponse(isForTerm: Boolean, grade: Double, courseName: String, units: Int) =>
            /* */debug("received: " + CourseGradeResponse(isForTerm, grade, courseName, units))
            if (isForTerm) {
              if (grade != -1) {
                //if grade == -1, it means that the grade for this course is not assigned yet. 
                //therefore the gpa should be updated only if grade != -1 
                gpa = gpa + grade * units;
                totalUnits = totalUnits + units
              }
              transcript = getTranscriptString(courseName, grade, units) :: transcript

            }
            //else ignore
            stepForward()
        }))
    }
  }
  def stepForward() {
    numOfResponses += 1

    if (numOfResponses == numOfExpectedResponses) {
      if (totalUnits == 0)
        gpa = 0.0
      else
        gpa = gpa / totalUnits
//      debug("transcript for student" + student + "in term: " + term + " : \n" + transcript)
      sendResponse()

    }
  }

  def sendResponse() {
    target ! GPAResponse(gpa)
    info("sent " + GPAResponse(gpa) + "to target: " + target + "in term: " + term + " : \n" + transcript)
    exit
  }

  override def toString = "[StudentComputeTermGPAActor of " + student + "]"

  private def sendOutRequestsToStudyRecs(st: Student, term: Term, targetActor: Actor): Unit = {
    this.target = targetActor
    this.student = st
    this.term = term
    this.numOfExpectedResponses = student.studyRecords.size
    //send request to all study records
    for (sr <- student.studyRecords)
      //      sr ! GPARequest(st, term, self, null)
      sr ! CourseGradeRequest(term, self, null)
  }

  private def getTranscriptString(courseName: String, grade: Double, units: Int): String = {
    grade match {
      case -1 =>
        "COURSE: " + courseName + " | " + "GRADE: NOT ASSIGNED | " + "UNITS: " + units
      case _ =>
        "COURSE: " + courseName + " | " + "GRADE: " + grade + " |" + "UNITS: " + units
    }
  }

}