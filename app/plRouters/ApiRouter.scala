package plRouters

import akka.stream.Materializer
import controllers.BookController
import endpoints.PLEndPoints
import javax.inject._
import play.api.Logger
import play.api.mvc.{ActionBuilder, AnyContent, Request}
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.server.play.PlayServerInterpreter
import sttp.tapir.swagger.play.SwaggerPlay
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApiRouter @Inject()(apiController: BookController,
                          bookEndpoints: PLEndPoints)
                         (implicit val materializer: Materializer, ec: ExecutionContext) extends SimpleRouter {

  val logger = Logger(this.getClass)

  /**
    * 路由
    * @return
    */
  override def routes: Routes = {
    openApiRoute()
      .orElse(booksListingRoute)
      .orElse(addBookRoute)
      .orElse(getBookRoute)
      .orElse(addBookRoutePart)
  }

  /**
    * book list
    */
  private val booksListingRoute: Routes =
    PlayServerInterpreter.toRoutes(bookEndpoints.booksListingEndpoint.serverLogic[Future](_=>apiController.listBooks()))

  /**
    * get book
    */
  private val getBookRoute: Routes =
    PlayServerInterpreter.toRoutes(bookEndpoints.getBookEndPoint)(d=>apiController.getBook(d))

  /**
    * add book
    * endpoint适配业务逻辑serverLogic
    */
  private val addBookRoute: Routes =
    PlayServerInterpreter.toRoutes(bookEndpoints.addBookEndpointNoLogic.serverLogic(d=>apiController.addBook(d._1,d._2)))

  /**
    * add book
    * endPointParts适配业务逻辑serverLogic
    */
  private val addBookRoutePart: Routes =
    PlayServerInterpreter.toRoutes(bookEndpoints.addBookEndpointLogicPart.andThen(d=>apiController.addBook(d._1,d._2)))

  /**
    * api docs
    */
  private val openApiDocs: OpenAPI = OpenAPIDocsInterpreter.toOpenAPI(List(
    bookEndpoints.booksListingEndpoint,
    bookEndpoints.addBookEndpointNoLogic.endpoint,
    bookEndpoints.addBookEndpointLogicPart.endpoint,
    bookEndpoints.getBookEndPoint,
  ),"Tapir Play Sample", "1.0.0")

  val openApiYml: String = openApiDocs.toYaml

  /**
    * SwaggerPlay需要actionBuilder，后续版本可能会去除
    */
  import sttp.tapir.server.play.PlayServerOptions.default
  implicit val actionBuilder: ActionBuilder[Request, AnyContent] = default.defaultActionBuilder

  // Doc will be on /docs
  def openApiRoute() ={

    // 打印swagger json string
    val yamlReader = new ObjectMapper(new YAMLFactory)
    val obj = yamlReader.readValue(openApiYml, classOf[Any])
    val jsonWriter = new ObjectMapper
    logger.info(jsonWriter.writeValueAsString(obj))

    // yml
    new SwaggerPlay(openApiYml).routes

  }

}
