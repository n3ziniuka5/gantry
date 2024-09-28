package gantry.services

import gantry.domain.{Chart, Deployment, GantryConfig, Helpers}
import org.virtuslab.yaml.*
import scala.language.experimental.namedTuples
import gantry.domain.AppError
import gantry.util.EitherBoundary.either
import gantry.util.EitherBoundary.either.get
import scala.util.Try
import gantry.util.DtoConversions.*

object ChartService:
    /*
     * returns `fileName -> content` map
     */
    def generateChartFiles(config: GantryConfig): Map[String, String] =
        val chart      = Chart.generate(config)
        val helpers    = Helpers.generate(config)
        val deployment = Deployment.generate(config)
        Map(
          chart.fileName      -> chart.value.asYaml,
          helpers.fileName    -> helpers.value,
          deployment.fileName -> deployment.value.asYaml
        )

    def generateToDirectory(config: GantryConfig, directory: os.Path): Either[AppError, Unit] =
        either:
            val allFileNames = List(Chart.fileName, Helpers.fileName, Deployment.fileName)
            val files        = generateChartFiles(config)

            // Delete existing files
            allFileNames.foreach { fileName =>
                val filePath = fileName.toPath(directory).get
                Try(os.remove(filePath)).toEither.left
                    .map(e => AppError.FailedToRemoveFile(filePath, e))
                    .get
            }

            // Write new files
            files.foreach { (fileName, content) =>
                val filePath = fileName.toPath(directory).get
                Try(os.write(filePath, content, createFolders = true)).toEither.left
                    .map(e => AppError.FailedToWriteFile(filePath, e))
                    .get
            }
            println(s"Generated chart files in $directory")
