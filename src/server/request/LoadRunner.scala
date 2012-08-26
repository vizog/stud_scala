package server.request

object LoadRunner extends App {

  for(i <- 1 to 40) {
    val start = System.currentTimeMillis()
    val request = new GPARequest("req-" + (i+1),"student-" + (i+1));
    request.start
  }
}