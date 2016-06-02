import java.io.{BufferedReader, InputStreamReader, StringWriter}

import org.apache.commons.httpclient.HttpClient
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.{HttpClientBuilder, HttpClients}
import play.api.libs.ws.ning.NingWSClient
import play.api.libs.concurrent.Execution.Implicits._

import scala.io.Source

/**
  * Created by a78084 on 5/27/16.
  */
class OpenWeatherUtil {
  val key : String = config.openWeatherKey


  def getWeather (location : String) : String = {
//    val client = HttpClientBuilder.create()
    val baseURL = "https://api.openweathermap.org/data/2.5/weather?q="
    val url = baseURL + location + "&APPID=" + key

    val wsClient = NingWSClient()

    val text: String = wsClient.url(url).get().map(row => row.toString).value.get.getOrElse("Missing")



//    val httpGet : HttpGet = new HttpGet(url)
//
//    val response : CloseableHttpResponse = client.build().execute(httpGet)
//    val entity = response.getEntity
//    val content = entity.getContent
//    val text = Source.fromInputStream(content).getLines().mkString
//    content.close()
    text
  }

}
