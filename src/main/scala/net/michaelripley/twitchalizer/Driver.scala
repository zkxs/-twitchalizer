package net.michaelripley.twitchalizer

import java.io.FileInputStream

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.util.concurrent.{FutureCallback, Futures, MoreExecutors}
import net.michaelripley.twitchalizer.dto.settings.Settings
import net.michaelripley.twitchalizer.dto.twitch.livegames.LiveGames
import net.michaelripley.twitchalizer.singletons.ObjectMapperInstance
import org.asynchttpclient.extras.guava.ListenableFutureAdapter
import org.asynchttpclient.{AsyncHttpClient, DefaultAsyncHttpClient, Response}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.slf4j.{Logger, LoggerFactory}

object Driver {

  val log: Logger = LoggerFactory.getLogger(Driver.getClass)

  lazy val mapper: ObjectMapper = ObjectMapperInstance.instance
  lazy val httpClient: AsyncHttpClient = new DefaultAsyncHttpClient()

  implicit val formats: DefaultFormats = DefaultFormats // Brings in default json date formats, etc.


  def main(args: Array[String]): Unit = {


    // read settings
    val inputStream = new FileInputStream(args(0)) // first argument is the path to the settings
    val settings = parse(inputStream).extract[Settings] // parse settings JSON

    log.debug(settings.toString)

    val twitchUrl = s"https://api.twitch.tv/api/users/${settings.username}/follows/games/live?on_site=1"

    hitTwitch(twitchUrl, settings, parseResponse)
  }

  def hitTwitch(url: String, settings: Settings, responseHandler: Response => Unit): Unit = {

    val builder = httpClient.prepareGet(url)
    settings.headers.foreach(t => {builder.setHeader(t._1, t._2)})
    builder.addQueryParam("limit", 500.toString)
    val future = ListenableFutureAdapter.asGuavaFuture(builder.execute())

    Futures.addCallback(future, new FutureCallback[Response] {
      def onSuccess(response: Response): Unit = {
        responseHandler(response)
      }

      def onFailure(t: Throwable): Unit = {
        t.printStackTrace()
      }
    }, MoreExecutors.directExecutor())

    log.debug("end of main thread")
  }

  def parseResponse(response: Response): Unit = {
    log.debug("parsing json")
    val responseBody = response.getResponseBody
    log.debug(s"extracted response body: $responseBody")
    val games = parse(responseBody).extract[LiveGames]
    log.debug("done parsing, will now print")
    println(games)
    httpClient.close()
    log.debug("end of parseResponse()")
  }

}
