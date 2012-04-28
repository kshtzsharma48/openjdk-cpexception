package cpexception

import scalax.file.Path
import scalax.file.ImplicitConversions._
import scalax.file.PathSet

import com.typesafe.config.ConfigFactory

object Main extends App {
  val openJdkRoot = if (args.isDefinedAt(0)) {
    args(0)
  } else {
    readLine("Please type in the root of exploded OpenJDK root you wish to examine: ")
  }
    
  val classpathExceptionText = """Oracle designates this particular file as subject to the "Classpath" exception as provided by Oracle in the LICENSE file that accompanied this code."""

  val javaExt = "*.java"
    
  val (includePatterns, excludePatterns) = includeExcludePatterns
  
  val javaFiles = includePatterns.map(openJdkRoot ** _ ** javaExt).reduceLeft(_ ++ _) ---
                  excludePatterns.map(openJdkRoot ** _ ** javaExt).reduceLeft(_ ++ _) 

  val notok = javaFiles.filterNot(hasClasspathException)

  println("Examined " + javaFiles.size + " files\n")
  println("""Classes without the "Classpath" exception follow:""")
  notok.foreach(p => println("\t" + p.path))

  def hasClasspathException(path: Path): Boolean = {
    val licenseStatement = path.lines(includeTerminator = false).dropWhile(_.trim == "/*").takeWhile(_.trim != "*/").
                                                                 map(_.stripPrefix(" * ").stripPrefix(" *")).
                                                                 reduceLeft(_ + " " + _)
    licenseStatement.contains(classpathExceptionText)
  }
  
  def includeExcludePatterns = {
	import scala.collection.JavaConversions._

    val conf = ConfigFactory.load()
	val includePatterns = conf.getStringList("includePatterns").toSet
	val excludePatterns = conf.getStringList("excludePatterns").toSet
    
	(includePatterns, excludePatterns)
  }
}