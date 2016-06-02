
import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.impl.client.{DefaultHttpClient, HttpClientBuilder, HttpClients}
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import twitter4j.auth.{Authorization, AuthorizationFactory}
import twitter4j.conf.ConfigurationBuilder
import twitter4j.Status

import scala.collection.mutable.ArrayBuffer
import scala.io.Source


object TwitterDriver {

  def main(args: Array[String]) : Unit = {
    val conf = new SparkConf().setMaster("local[*]")
//      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

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

    var sentimentValues : Double = 0.0
    var counter : Long = 0

    val filters : Array[String] = Array("#NY")

    val tweets : DStream[Status] = TwitterUtils.createStream(ssc, Option(auth),filters)


    tweets.window(Minutes(5),Seconds(10)).foreachRDD{ tweetRDD =>

      tweetRDD.foreach{tweet =>
          val lang = tweet.getUser.getLang
        if (lang == "en") {
          val username : String = tweet.getUser.getScreenName
          val friends : Long = tweet.getUser.getFriendsCount
          val text : String = tweet.getText.split("https")(0).replaceAll("[^a-zA-Z0-9.!?'%@ ]","").replaceAll("/n"," ")
          val textCount : Long = text.split(" ").length
          val sentiment : Double = SentimentAnalysis.detectSentiment(text)
//          val sentiment : String = if (sentimentValue <= 0.0) {
//            "Not Understood"
//          } else if (sentimentValue <= 1.0) {
//            "Very Negative"
//          } else if (sentimentValue <= 2.0) {
//            "Negative"
//          } else if (sentimentValue <= 3.0) {
//            "Neutral"
//          } else if (sentimentValue <= 4.0) {
//            "Positive"
//          } else if (sentimentValue <= 5.0) {
//            "Very Positive"
//          } else "Not Understood"
//          if (sentiment > 0.0) {
//            println(s"$username is $sentiment has tweeted '$text' ($textCount words) and has $friends friends.")
            sentimentValues = sentimentValues + sentiment
            counter = counter + 1
//          }
        }
      }
      val avgSentiment : Double = sentimentValues/counter.toDouble

      println(s"The average sentiment is $avgSentiment")

      scala.tools.nsc.io.File("sentiment.txt").writeAll(avgSentiment.toString)

//      println(owu.getWeather("New York"))

//      sentimentValues = 0.0
//      counter = 0
    }
    ssc.start()
    ssc.awaitTermination()
  }
}
