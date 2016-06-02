
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{DefaultHttpClient, HttpClientBuilder, HttpClients}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.auth.{Authorization, AuthorizationFactory}
import twitter4j.conf.ConfigurationBuilder
import twitter4j.Status

import scala.io.Source


object TwitterDriver {

  def main(args: Array[String]) : Unit = {
    val conf = new SparkConf()
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val sc  = new SparkContext(conf)
    val ssc = new StreamingContext(sc,Seconds(5))
    val owu = new OpenWeatherUtil

    val consumerKey       : String = config.consumerKey
    val consumerSecret    : String = config.consumerSecret
    val accessToken       : String = config.accessToken
    val accessTokenSecret : String = config.accessTokenSecret

    val cb = new ConfigurationBuilder()
    cb.setDebugEnabled(true)
      .setOAuthConsumerKey(consumerKey)
      .setOAuthConsumerSecret(consumerSecret)
      .setOAuthAccessToken(accessToken)
      .setOAuthAccessTokenSecret(accessTokenSecret)

    val auth : Authorization = AuthorizationFactory.getInstance(cb.build())


    val filters : Array[String] = Array("#fiveyearsout")

    val tweets : DStream[Status] = TwitterUtils.createStream(ssc, Option(auth),filters)


    tweets.foreachRDD{tweetRDD =>
      tweetRDD.foreach{tweet =>
          val lang = tweet.getUser.getLang
        if (lang == "en") {
          val username : String = tweet.getUser.getScreenName
          val friends : Long = tweet.getUser.getFriendsCount
          val text : String = tweet.getText.split("https")(0).replaceAll("[^a-zA-Z0-9.!?'%@ /n]","").replaceAll("/n"," ")
          val textCount : Long = text.split(" ").length
          val sentimentValue : Double = SentimentAnalysis.detectSentiment(text)
          val sentiment : String = if (sentimentValue <= 0.0) {
            "Not Understood"
          } else if (sentimentValue <= 1.0) {
            "Very Negative"
          } else if (sentimentValue <= 2.0) {
            "Negative"
          } else if (sentimentValue <= 3.0) {
            "Neutral"
          } else if (sentimentValue <= 4.0) {
            "Positive"
          } else if (sentimentValue <= 5.0) {
            "Very Positive"
          } else "Not Understood"
          if (sentiment != "Not Understood") {
            println(s"$username is $sentiment has tweeted '$text' ($textCount words) and has $friends friends.")
          }
        }
      }
//      println(owu.getWeather("Denver"))
    }
    ssc.start()
    ssc.awaitTermination()
  }
}
