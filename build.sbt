name := """play_learning"""
organization := "com.qiulp.learn"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test



val tapirVersion = "0.18.0-M17"
// Tapir
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-play-server" % tapirVersion

// JSON handling (you can use Circe instead of Play-JSON)
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-play" % tapirVersion

// Tapir OpenAPI
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion

libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion

libraryDependencies += "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.10.5"


// Swagger UI for Play
// You can also host Swagger UI by yourself and get rid of this dependency
libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-play" % tapirVersion

libraryDependencies += "dev.zio" %% "zio" % "1.0.9"
libraryDependencies += "dev.zio" %% "zio-streams" % "1.0.9"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.qiulp.learn.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.qiulp.learn.binders._"
