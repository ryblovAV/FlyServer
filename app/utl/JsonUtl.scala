package utl

import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, _}


case class MediaInfoKey(size: String, tth: String)

case class RequestData(array: List[MediaInfoKey])

object JsonUtl {

  implicit val MediaInfoKeyReads: Reads[MediaInfoKey] = (
    (__ \ 'size).read[String] and
      (__ \ 'tth).read[String]
    ) (MediaInfoKey.apply _)

  implicit val RequestDataReads: Reads[RequestData] =
    ((__ \ 'array).read[List[MediaInfoKey]] ) map (RequestData.apply _)

  def parse(str: String): RequestData = {
    Json.parse(str).as[RequestData]
  }

}
