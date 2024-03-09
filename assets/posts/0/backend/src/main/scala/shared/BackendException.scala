package com.lovindata
package shared

sealed trait BackendException extends Exception

object BackendException {
  case class BadRequestException(message: String)          extends BackendException
  case class ServerInternalErrorException(message: String) extends BackendException
}
