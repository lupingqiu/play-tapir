package models

import play.api.libs.json.{Format, Json}

/**
  * Created by luping.qiu in 4:04 PM 2021/6/21
  */
case class Book(title: String, year: Int, author: Author)

object Book {
  implicit val format: Format[Book] = Json.format[Book]
}

case class Author(name: String)
object Author {
  implicit val format: Format[Author] = Json.format[Author]
}

case class SuccessR(code: Int)
object SuccessR {
  implicit val format: Format[SuccessR] = Json.format[SuccessR]
}


case class LogicError(error: String) extends ErrorInfo

object LogicError {
  implicit val format: Format[LogicError] = Json.format[LogicError]
}

case class UserContext(userId:String,tenantId:String)

object UserContext{
  implicit val format:Format[UserContext] = Json.format[UserContext]
}

sealed trait ErrorInfo
case class NotFound(what: String) extends ErrorInfo
case class Unauthorized(realm: String) extends ErrorInfo
case class Unknown(code: Int, msg: String) extends ErrorInfo
case object NoContent extends ErrorInfo

object NotFound{
  implicit val format:Format[NotFound] = Json.format[NotFound]
}
object Unauthorized{
  implicit val format:Format[Unauthorized] = Json.format[Unauthorized]
}
object Unknown{
  implicit val format:Format[Unknown] = Json.format[Unknown]
}