package model

import java.security.MessageDigest
import java.sql.Timestamp
import javax.inject.Inject

import play.api.data.Form
import play.api.data.Forms._

import model.util.DAOUtils
import play.api.data.Form
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}

import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import slick.lifted.ProvenShape
import slick.profile.SqlProfile.ColumnOption.SqlType

import scala.concurrent.ExecutionContext.Implicits.global

case class PokeCool(id: Int,
                    nombre: String,
                    tipo: Int,
                    peso: BigDecimal,
                    altura: BigDecimal,
                    vida: Int,
                    puntosCombate: Int,
                    apodo: String,
                    urlImagen: String)

case class PokeData (nombre: String,
                     tipo: Int,
                     peso: BigDecimal,
                     altura: BigDecimal,
                     vida: Int,
                     puntosCombate: Int,
                     apodo: String,
                     urlImagen: String)

object PokeForm {

  val form = Form(
    mapping(
      "nombre" -> text,
      "tipo" -> number,
      "peso" -> bigDecimal,
      "altura" -> bigDecimal,
      "vida" -> number,
      "puntosCombate" -> number,
      "apodo" -> text,
      "urlImagen" -> text
    )(PokeData.apply)(PokeData.unapply)
  )

}

class PokeCoolTable (tag: Tag) extends Table[PokeCool](tag, "pokedex") {

  val tipos = TableQuery[PokeTipoTable]

  def id            = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def nombre        = column[String]("nombre")
  def tipo          = column[Int]("tipo")
  def peso          = column[BigDecimal]("peso")
  def altura        = column[BigDecimal]("altura")
  def vida          = column[Int]("vida")
  def puntosCombate = column[Int]("puntos_combate")
  def apodo         = column[String]("apodo")
  def url_imagen    = column[String]("url_imagen")

  def tipoFK = foreignKey("fk_pokedex_1", tipo, tipos)(_.id)

  override def * : ProvenShape[PokeCool] =
    (
      id,
      nombre,
      tipo,
      peso,
      altura,
      vida,
      puntosCombate,
      apodo,
      url_imagen
    )<>(PokeCool.tupled, PokeCool.unapply)

}

class PokeCoolDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends DAOUtils[PokeCoolTable, PokeCool] {

  override val t: TableQuery[PokeCoolTable] = TableQuery[PokeCoolTable]

  def delete(id: Int): Future[Int] = db.run(t.filter(_.id === id).delete)

  def update(pokeCool: PokeCool): Future[String] =
    db.run(
      t.filter(_.id === pokeCool.id).update(pokeCool)).map(_ => "ok").recover {
    case ex: Exception => ex.getCause.getMessage
  }

}

