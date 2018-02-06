name := "typesafe-config-test"

organization := "com.github.dnvriend"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.4"

libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.2.16"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.8.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.7"
libraryDependencies += "org.typelevel" %% "scalaz-scalatest" % "1.1.2" % Test
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test

// testing configuration
fork in Test := true
parallelExecution := false
envVars in Test := Map("FIRST_NAME" -> "my_first_name")

// enable scala code formatting //
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

// Scalariform settings
SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
   .setPreference(AlignSingleLineCaseStatements, true)
   .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
   .setPreference(DoubleIndentConstructorArguments, true)
   .setPreference(DanglingCloseParenthesis, Preserve)

// enable updating file headers //
organizationName := "Dennis Vriend"
startYear := Some(2017)
licenses := Seq(("Apache-2.0", new URL("https://www.apache.org/licenses/LICENSE-2.0.txt")))
headerMappings := headerMappings.value + (HeaderFileType.scala -> HeaderCommentStyle.CppStyleLineComment)

enablePlugins(AutomateHeaderPlugin, SbtScalariform)
