package dal

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.collection.mutable.HashMap

/**
  * Created by coolluck on 11/21/15.
  */
class Loader {

}

object Loader {
  val DEFAULT_PROTOCOL = "https"
  val ITS_DOMAIN = "as.its-kenpo.or.jp"
  val ITS_CATEGORY_URI = "/service_category/index"
  val ITS_SERVICE_URI = "/service_group/index"
  val TEXT_FOR_VACANT = "直営・通年・夏季保養施設(空き照会)"

  def getEntranceToken = {
    val url = s"$DEFAULT_PROTOCOL://$ITS_DOMAIN$ITS_CATEGORY_URI"

    val browser = new Browser
    val doc = browser.get(url)
    val uris = for (
      item <- doc >> elementList(".service_category");
      if item >> text("a") == "直営・通年・夏季保養施設(空き照会)"
    ) yield item.getElementsByTag("a").attr("href")
    val secondUrl = s"$DEFAULT_PROTOCOL://$ITS_DOMAIN${uris.head}"

    val result = new HashMap[String, String]

    for (secondItems <- browser.get(secondUrl) >?> element(".items")) {
      for (secondLinks <- secondItems.getElementsByTag("a")) {
        for (
          link <- secondLinks.attr("href");
          hotelName <- secondLinks.text
        ) yield result put (link, hotelName)
      }
    }
    println(result)
  }

  def main(args: Array[String]): Unit = {
    getEntranceToken
  }
}