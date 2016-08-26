package model.util

import play.api.db.slick.HasDatabaseConfigProvider


import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This trait abstracts many of the common operations on data tables
  *
  * @tparam A The table type in the relational database. This type must
  *           be a subtype of `Table[B]`.
  * @tparam B Given a tye `AccountTable` which is a subclass of `Table[Account]`
  *           B would be `Account`
  */
trait DAOUtils[A <: Table[B], B] extends HasDatabaseConfigProvider[JdbcProfile] {

  /**
    * The table to be queried.
    */
  val t: TableQuery[A]

  /**
    * Queries a table to get a result based on a predicate
    *
    * @param p The predicate to be applied
    * @return  The possibility of a future computation that contains
    *          the possibility of an element of type `B`
    */
  def queryByPredicate (p: (A => Rep[Boolean])): Future[Option[B]] = {
    val query = t.filter(p)
    db.run(query.result.headOption)
  }

  /**
    * Queries a table and gets all the results
    *
    * @return  The possibility of a future computation containing a
    *          sequence of elements of type `B`
    */
  def getAll: Future[Seq[B]] = db.run(t.result)

  /**
    * Queries a table to see if an element exists based on a predicate
    *
    * @param p The predicate to be applied
    * @return  The possibility of a future computation containing a Boolean
    */
  def queryIfExists(p: (A => Rep[Boolean])): Future[Boolean] = {
    val query = t.filter(p)
    db.run(query.exists.result)
  }

  /**
    * Abstract add function for single elements of type B
    *
    * @param e The element to be added
    * @return  `ok` if the element was successfully added or the exception message otherwise.
    */
  def add(e: B): Future[String] = db.run(t += e).map(res => "ok").recover {
    case ex: Exception => ex.getCause.getMessage
  }


}
