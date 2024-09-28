package gantry.usecases

import gantry.domain.AppError
import gantry.services.{ChartService, ConfigService}
import gantry.util.EitherBoundary.either
import gantry.util.EitherBoundary.either.get
import gantry.util.ProcUtils.callSafe

object GantryUseCase:
    def buildChart(gantryFilePath: os.Path, outputDir: os.Path): Either[AppError, Unit] =
        either:
            val config = ConfigService.getConfig(gantryFilePath).get
            ChartService
                .generateToDirectory(config, outputDir)
                .get

    def installChart(gantryFilePath: os.Path, maybeReleaseName: Option[String]): Either[AppError, Unit] =
        either:
            val config  = ConfigService.getConfig(gantryFilePath).get
            val tempDir = os.temp.dir()
            ChartService.generateToDirectory(config, tempDir).get

            val chartName   = config.name
            val releaseName = maybeReleaseName.getOrElse(chartName)
            println(s"Installing chart $chartName from $tempDir")
            os.proc("helm", "upgrade", "--install", releaseName, tempDir.toString(), "--wait")
                .callSafe()
                .get
