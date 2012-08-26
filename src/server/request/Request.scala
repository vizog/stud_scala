package server.request

import scala.actors.Actor

import org.apache.log4j.Logger

//messages:

abstract class Request(
  var id: String) extends Actor {
  
  private val perfLogger: Logger = Logger.getLogger("PerformanceLogger");

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
  
  def info(msg:String) = {
    perfLogger.info(msg)
  }
  
  def debug(msg:String) = {
	  perfLogger.debug(msg)
  }
}