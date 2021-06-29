package controllers

import javax.inject._
import models._
import repositories.BookRepository

import scala.concurrent.Future


@Singleton
class BookController @Inject()(bookRepository: BookRepository) {

  def listBooks(): Future[Either[Unit, Vector[Book]]] = {
    Future.successful(Right(bookRepository.getBooks().toVector))
  }

  def addBook(user:UserContext,book: Book): Future[Either[LogicError, SuccessR]] = {
    bookRepository.addBook(book)
    Future.successful(Right(SuccessR(201)))
  }

  def getBook(title: String): Future[Either[LogicError, Book]] = {
    Future.successful {
      bookRepository.getBooks()
        .find(_.title == title)
        .toRight(LogicError(s"No book with exact title $title"))
    }
  }
}