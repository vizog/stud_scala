package server.request

import scala.actors.Actor
import util.LoggingSupport

//messages:

abstract class Request(
  var id: String) extends Actor with LoggingSupport {

//  override def act() {
//    val start = System.currentTimeMillis();
//    debug(id + " START    -> " + start);
//
//    process;
//
//    val end = System.currentTimeMillis();
//    debug(id + " END      -> " + end);
//    info(id + " RESPONSE -> " + (end - start));
//
//  }

  //this should be overridden by concrete requests
//  def process

  override def toString(): String = {
    return "Request: [" + id + "]"
  }
}