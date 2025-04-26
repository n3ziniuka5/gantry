package gantry.core.services

import gantry.core.domain.{Chart, Deployment, GantryConfig, Helpers, Service}
import org.virtuslab.yaml.*
import scala.language.experimental.namedTuples
import gantry.core.domain.AppError
import gantry.core.util.EitherBoundary.either
import gantry.core.util.EitherBoundary.either.?
import scala.util.Try
import gantry.cli.DtoConversions.*
import gantry.core.util.ProcUtils.callSafe
object ChartService:
    /*
     * returns `fileName -> content` map
     */
    def generateChartFiles(config: GantryConfig): Map[String, String] =
        val chart        = Chart.generate(config)
        val helpers      = Helpers.generate(config)
        val deployment   = Deployment.generate(config)
        val maybeService = Service.generate(config)
        Map(
          chart.fileName      -> chart.value.asYaml,
          helpers.fileName    -> helpers.value,
          deployment.fileName -> deployment.value.asYaml
        ) ++ maybeService.map(s => s.fileName -> s.value.asYaml)

    def generateToDirectory(config: GantryConfig, directory: os.Path): Either[AppError, Unit] =
        either:
            val allFileNames = List(Chart.fileName, Helpers.fileName, Deployment.fileName, Service.fileName)
            val files        = generateChartFiles(config)

            // Delete existing files
            allFileNames.foreach { fileName =>
                val filePath = fileName.toPath(directory).?
                Try(os.remove(filePath)).toEither.left
                    .map(e => AppError.FailedToRemoveFile(filePath, e))
                    .?
            }

            // Write new files
            files.foreach { (fileName, content) =>
                val filePath = fileName.toPath(directory).?
                Try(os.write(filePath, content, createFolders = true)).toEither.left
                    .map(e => AppError.FailedToWriteFile(filePath, e))
                    .?
            }
            println(s"Generated chart files in $directory")

    def installChart(chartPath: os.Path, releaseName: String): Either[AppError, Unit] =
        either:
            println(s"Installing chart \"$releaseName\" from $chartPath")
            os.proc("helm", "upgrade", "--install", releaseName, chartPath.toString(), "--wait")
                .callSafe()
                .?
