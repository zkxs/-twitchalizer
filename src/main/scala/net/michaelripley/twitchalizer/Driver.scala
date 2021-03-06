package net.michaelripley.twitchalizer

import java.io.FileInputStream

import com.google.common.util.concurrent.{FutureCallback, Futures, MoreExecutors}
import net.michaelripley.twitchalizer.dto.settings.Settings
import net.michaelripley.twitchalizer.dto.twitch.livegames._
import org.asynchttpclient.extras.guava.ListenableFutureAdapter
import org.asynchttpclient.{AsyncHttpClient, DefaultAsyncHttpClient, Response}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.slf4j.{Logger, LoggerFactory}

object Driver {

  val log: Logger = LoggerFactory.getLogger(Driver.getClass)

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

    println(s"${games._total} games are currently live. Sorted by: ${Game.rankFormula}\n")

//    games.follows.sortWith((a, b) => a.viewers > b.viewers).foreach(println) // most viewers
//    games.follows.filter(_.viewers >= 5).sortBy(_.viewers).foreach(println) // fewest viewers, but at least 5 viewers
//    games.follows.sortWith((a, b) => a.channels > b.channels).foreach(println) // most channels
    games.follows.sortWith((a, b) => a.rank > b.rank).foreach(println) // most metric


    httpClient.close()
    log.debug("end of parseResponse()")
  }

}
