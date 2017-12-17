package net.michaelripley.twitchalizer

import java.io.FileInputStream
import java.util.concurrent.{ExecutorService, Executors}

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.util.concurrent.{FutureCallback, Futures, MoreExecutors}
import net.michaelripley.twitchalizer.dto.settings.Settings
import net.michaelripley.twitchalizer.dto.twitch.livegames.LiveGames
import net.michaelripley.twitchalizer.singletons.ObjectMapperInstance
import org.asynchttpclient.extras.guava.ListenableFutureAdapter
import org.asynchttpclient.{AsyncHttpClient, DefaultAsyncHttpClient, Response}

object Driver {

  lazy val mapper: ObjectMapper = ObjectMapperInstance.instance
  lazy val httpClient: AsyncHttpClient = new DefaultAsyncHttpClient()
  lazy val executor: ExecutorService = Executors.newWorkStealingPool()

  def main(args: Array[String]): Unit = {


    // read settings
    val inputStream = new FileInputStream(args(0)) // first argument is the path to the settings
    val settings = mapper.readValue(inputStream, classOf[Settings])

    println(settings)

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
    }, executor)

    println(s"end of thread ${Thread.currentThread().getName}")
  }

  def parseResponse(response: Response): Unit = {
    println(s"parsing json on thread ${Thread.currentThread().getName}")
    val bytes = response.getResponseBodyAsBytes
    println("got bytes")
    println(new String(bytes, "UTF-8"))
    val games = mapper.readValue(bytes, classOf[LiveGames])
    println("done parsing, will now print")
    println(games)
    println("end of parseResponse()")
  }

}
