import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations

import scala.collection.JavaConversions._
import scala.collection.mutable.ListBuffer


object SentimentAnalysis {

  val nlpProps = {
    val props = new Properties()
    props.setProperty("annotators", "tokenize, ssplit, regexner, pos, lemma, parse, sentiment")
    props
  }

  def detectSentiment(text: String): Double = {

    val pipeline = new StanfordCoreNLP(nlpProps)
    val annotation = pipeline.process(text)
    var sentiments : ListBuffer[Double] = ListBuffer()

    for (sentence <- annotation.get(classOf[CoreAnnotations.SentencesAnnotation])) {
      val tree = sentence.get(classOf[SentimentCoreAnnotations.AnnotatedTree])
      val sentiment = RNNCoreAnnotations.getPredictedClass(tree)

      sentiments += sentiment.toDouble
    }

    val weightedSentiment = if(sentiments.isEmpty) {
      0.0
    } else {
      sentiments.sum/sentiments.length.toDouble
    }

    weightedSentiment
  }
}