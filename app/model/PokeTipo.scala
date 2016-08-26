package model

import java.security.MessageDigest
import java.sql.Timestamp
import javax.inject.Inject

import model.util.DAOUtils
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape
import slick.profile.SqlProfile.ColumnOption.SqlType

import scala.concurrent.ExecutionContext.Implicits.global

case class PokeTipo (id: Int, nombre: String)

class PokeTipoTable (tag: Tag) extends Table[PokeTipo](tag, "tipo") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def nombre = column[String]("nombre")

  override def * : ProvenShape[PokeTipo] = (id, nombre)<>(PokeTipo.tupled, PokeTipo.unapply)
}

class PokeTipoDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends DAOUtils[PokeTipoTable, PokeTipo] {

  override val t: TableQuery[PokeTipoTable] = TableQuery[PokeTipoTable]

}
