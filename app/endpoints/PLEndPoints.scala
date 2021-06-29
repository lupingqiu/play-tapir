package endpoints

import javax.inject.Inject
import models._
import play.api.Logger
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import sttp.tapir.json.play.jsonBody
import sttp.tapir.server.{PartialServerEndpoint, ServerEndpointInParts}
import sttp.tapir.{Endpoint, endpoint, _}

import scala.concurrent.{ExecutionContext, Future}
/**
  * Created by luping.qiu in 4:01 PM 2021/6/21
  */
class PLEndPoints @Inject()(implicit ex:ExecutionContext) {

  val logger = Logger(this.getClass)
  private val plEndpoint = endpoint
    .tag("测试tapir")
    .in("books")



  val booksListingEndpoint: Endpoint[Unit, Unit, Vector[Book], Any] = plEndpoint.get
    .summary("List all books")
    .in("list" / "all")
    .out(jsonBody[Vector[Book]])



  val getBookEndPoint = plEndpoint.get
    .summary("Get a book (by title)")
    .in("find")
    .in(
      query[String]("title")
        .description("The title to look for")
    )
    .errorOut(jsonBody[LogicError])
    .out(
      jsonBody[Book]
        .description("The book (if found)")
    )



  def authorize(authToken: String): Future[UserContext] = {
    Future{UserContext(authToken,"")}
  }



  def authorize1(authToken: String): Future[Either[LogicError, UserContext]] = {
    Future{
      authToken match {
        case "" => Left(LogicError("token is null"))
        case _ => Right(UserContext(authToken,""))
      } }
  }

  def addBookToUser(user: UserContext, book: Book): Future[Either[LogicError, SuccessR]] = Future{
    Right(SuccessR(200))
  }

  /**
    * serverLogicPart
    * 输出SuccessR
    */
  val addBookEndpointLogicPart: ServerEndpointInParts[UserContext, Book, (String, Book), LogicError, SuccessR, Any, Future] = plEndpoint.post
    .in("add_part")
    .summary("serverLogicPart auth part")
    .in(header[String]("X-AUTH-TOKEN"))
    .errorOut(jsonBody[LogicError])
    .in(
    jsonBody[Book]
      .description("The book to add")
      .example(Book("Pride and Prejudice", 1813, Author("Jane Austen")))
    )
    .out(jsonBody[SuccessR])
    .serverLogicPart(authorize1)



  val secureEndpoint: PartialServerEndpoint[String, UserContext, Unit, LogicError, Unit, Any, Future] = plEndpoint
    .in(header[String]("X-AUTH-TOKEN"))
    .errorOut(jsonBody[LogicError])
    .errorOut(statusCode(StatusCode.Unauthorized))
    .serverLogicForCurrent(authorize1)


   val addBookEndpointNoLogic: PartialServerEndpoint[String, UserContext, Book, LogicError, SuccessR, Any, Future] = secureEndpoint.post
    .in("add")
     .summary("serverLogicForCurrent with no logic")
    .in(
      jsonBody[Book]
        .description("The book to add")
        .example(Book("Pride and Prejudice", 1813, Author("Jane Austen")))
    )
    .out(jsonBody[SuccessR])

}
