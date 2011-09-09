package util

import org.apache.log4j.Logger

trait LoggingSupport {
	def debug(message:String) {
	  Logger.getLogger(getClass()).debug(message)
	}
}