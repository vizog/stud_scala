package util

import org.apache.log4j.Logger

trait LoggingSupport {
	def debug(message:String) {
	  println("loggerSupport getClass: " + getClass)
	  Logger.getLogger(getClass()).debug(message)
	}
}