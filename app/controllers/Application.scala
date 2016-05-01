package controllers

import play.api.mvc._
import reader.MediaInfoReader
import utl.{JsonUtl, ZLibUtl}

class Application extends Controller {

  def getInfo = Action(parse.raw) {
    request =>
      request.body.asBytes() match {
        case Some(bs) =>
          val requestInfo = JsonUtl.parse(ZLibUtl.decompress(bs.toArray))

          val mediaInfoCompress =
            ZLibUtl.compress(
              MediaInfoReader.find(
                requestInfo.array
              ).toString.getBytes
            )
          Ok(mediaInfoCompress)
        case None => BadRequest
      }
  }
}