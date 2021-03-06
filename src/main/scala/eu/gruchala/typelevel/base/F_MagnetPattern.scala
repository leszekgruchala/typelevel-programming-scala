package eu.gruchala.typelevel.base

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.implicitConversions

object F_MagnetPattern {

  class HttpResponse
  sealed trait StatusCode
  object StatusCode {
    case object Ok extends StatusCode
    case object Bad_Request extends StatusCode
  }

  trait RouteOps {

    //The problem
    def complete(status: StatusCode): Unit
    def complete(response: HttpResponse): Int
//    def complete(future: Future[StatusCode]): Unit //does not compile
    def complete(future: Future[HttpResponse]): Unit
    def complete[T](obj: T): Int
    def complete[T](statusCode: StatusCode, obj: T): Int
  }

  val ? = "What is Magnet pattern?"

  sealed trait CompletionMagnet {
    type Result
    def apply(): Result
  }
  object CompletionMagnet {
    implicit def fromStatusCode(statusCode: StatusCode): CompletionMagnet =
      new CompletionMagnet {
        override type Result = Int
        override def apply(): Int = if (statusCode == StatusCode.Ok) 200 else 500
      }

    implicit def fromFutureStatusCode(future: Future[StatusCode]): CompletionMagnet =
      new CompletionMagnet {
        override type Result = Unit
        override def apply(): Result = future onSuccess { case resp => s"log: Got $resp" }
      }
    //etc.
  }

  object MagnetRoute {
    import CompletionMagnet._

    def complete = ???

  }
}
