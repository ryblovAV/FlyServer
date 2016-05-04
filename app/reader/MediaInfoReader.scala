package reader

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import config.JsonFieldName._
import config.MongoConfig._
import play.Logger._
import utl.MediaInfoKey

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MediaInfoReader {

  val client = MongoClient(host, 27017)(db)

  def find(keys: List[MediaInfoKey]) = {

    def updateCountQuery(coll: MongoCollection, keys: List[DBObject]) = {
      val builder = coll.initializeOrderedBulkOperation
      keys.foreach(k => builder.find(k).upsert().updateOne($inc(COUNT_QUERY -> 1)))
      builder.execute()
    }

    val coll = client(collection)

    keys.foreach(k => debug(s"-> tth = ${k.tth}, size = ${k.size}"))

    val keysMongoObjects = keys.map(k => MongoDBObject(TTH -> k.tth, SIZE -> k.size))

    val query = $or (keysMongoObjects)
    val fields = MongoDBObject(MEDIA -> 1, TTH -> 1, SIZE -> 1, _ID -> 0)

    val mediaInfoArray = coll.find(query, fields).toArray

    Future {
      updateCountQuery(coll,keysMongoObjects)
    } onFailure { case e => info(s"Error!!! update countQuery: $e")}

    if (isDebugEnabled)
      mediaInfoArray.foreach(i => debug(s"<- ${i}"))

    MongoDBObject(ARRAY -> mediaInfoArray)

  }

}
