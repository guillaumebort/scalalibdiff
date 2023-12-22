import mill._
import mill.scalalib._
import mill.scalalib.publish._
import mill.scalajslib._
import scalafmt._

trait ScalalibdiffModule extends CrossScalaModule with ScalafmtModule with PublishModule {
  def millSourcePath = build.millSourcePath
  def artifactName = "scalalibdiff"
  def publishVersion = "0.1.0"
  def pomSettings = PomSettings(
    description = "Basic text diff library for Scala & ScalaJS",
    organization = "com.github.guillaumebort",
    url = "https://github.com/guillaumebort/scalalibdiff",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("guillaumebort", "scalalibdiff"),
    developers = Seq(
      Developer("guillaume.bort@gmail.com", "Guillaume Bort","https://github.com/guillaumebort")
    )
  )
}

trait ScalalibdiffTestModule extends TestModule with ScalaModule {
  def ivyDeps = Agg(ivy"com.lihaoyi::utest::0.8.2")
  def testFrameworks = Seq("utest.runner.Framework")
}

object jvm extends Cross[ScalalibdiffJvmModule]("2.11.12", "2.12.7", "2.13.12")
class ScalalibdiffJvmModule(val crossScalaVersion: String) extends ScalalibdiffModule {
  def platformSegment = "jvm"
  object test extends Tests with ScalalibdiffTestModule {
    def millSourcePath = build.millSourcePath / "test"
  }
}

object js extends Cross[ScalalibdiffJsModule]("2.11.12", "2.12.7", "2.13.12")
class ScalalibdiffJsModule(val crossScalaVersion: String) extends ScalalibdiffModule with ScalaJSModule {
  def platformSegment = "js"
  def scalaJSVersion = "1.14.0"
  object test extends Tests with ScalalibdiffTestModule {
    def millSourcePath = build.millSourcePath / "test"
  }
}
