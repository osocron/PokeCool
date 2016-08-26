package controllers

import javax.inject.Inject

import model._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.FileInputStream

class Application @Inject()(pokeCoolDAO: PokeCoolDAO,
                            pokeTipoDAO: PokeTipoDAO) extends Controller {

  def index = Action.async { request =>
    for {
      p <- pokeCoolDAO.getAll
      t <- pokeTipoDAO.getAll
    } yield {
      Ok(views.html.index(p, t, PokeForm.form))
    }
  }

  def addPokemon() = Action.async(parse.multipartFormData) { implicit request =>
    PokeForm.form.bindFromRequest.fold(
      errors => Future.successful(Ok("errores en datos")),
      data => {
        val pokemon = PokeCool(0, data.nombre,
          data.tipo, data.peso, data.altura,
          data.vida, data.puntosCombate, data.apodo, data.urlImagen)
        pokeCoolDAO.add(pokemon).map { res =>
          Redirect(routes.Application.index())
        }
      }
    )
  }

  def updatePokemon(id: Int) = Action.async {implicit request =>
    PokeForm.form.bindFromRequest.fold(
      errorForm => Future.successful(Ok("errores en datos")),
      data => {
        val pokemon = PokeCool(id, data.nombre,
          data.tipo, data.peso, data.altura,
          data.vida, data.puntosCombate, data.apodo, data.urlImagen)
        pokeCoolDAO.update(pokemon).map(res => Redirect(routes.Application.index()))
      }
    )
  }

  def deletePokemon(id: Int) = Action.async { implicit request =>
    pokeCoolDAO.delete(id).map { res =>
      Ok("deleted")
    }
  }

}