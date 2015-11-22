package lib

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.jsoup.nodes.Element

import scala.collection.mutable

/**
  * Created by coolluck on 11/21/15.
  */
class crawler {

}

object crawler {
  val DEFAULT_PROTOCOL = "https"
  val ITS_DOMAIN = "as.its-kenpo.or.jp"
  val ITS_CATEGORY_URI = "/service_category/index"
  val ITS_SERVICE_URI = "/service_group/index"
  val TEXT_FOR_VACANT = "直営・通年・夏季保養施設(空き照会)"
  val browser = new Browser

  def entranceUrl(uri: String): String = {
    val url = makeUrl(uri)
    val uris = for (
      item <- browser.get(url) >> elementList(".service_category");
      if item >> text("a") == TEXT_FOR_VACANT
    ) yield item.getElementsByTag("a").attr("href")
    makeUrl(uris.head)
  }

  def availableDate(uri: String) = {
    for (
      date <- (browser.get(makeUrl(uri)) >> element("#apply_join_time")).getElementsByTag("option");
      if !date.attr("value").isEmpty
    ) yield date.attr("value")
  }

  def extractLink(item: Element) = for (link <- item.getElementsByTag("a")) yield (link.attr("href"), link.text)

  def links(f: String => String, uri: String) = browser.get(f(uri)) >?> element(".items").map(extractLink(_))

  def makeUrl(uri: String): String = s"$DEFAULT_PROTOCOL://$ITS_DOMAIN$uri"

  def main(args: Array[String]): Unit = {
    val result = new mutable.HashMap[String, Any]()
    links(entranceUrl, ITS_CATEGORY_URI) match {
      case Some(hotels) => hotels.foreach(
        (x: (String, String)) => result += (
          x._2 -> links(makeUrl, x._1).get.map(
            (y: (String, String)) => availableDate(y._1)
          )
          )
      )
    }
    println(result)

    //    val hotels = links(entranceUrl, ITS_CATEGORY_URI).get
    //    val uri = hotels.head._1
    //    val result1 = links(uri => makeUrl(uri), uri).get
    //    val dates = availableDate(result1(2)._1)
  }
}