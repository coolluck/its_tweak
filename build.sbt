name := "its_tweak"

version := "1.0"

lazy val `its_tweak` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(jdbc, cache, ws, specs2 % Test)

libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.0.0"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "0.1.2"

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"