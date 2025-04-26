package gantry.core.usecases

import gantry.core.domain.AppError
import gantry.core.services.{ChartService, ConfigService}
import gantry.core.util.EitherBoundary.either
import gantry.core.util.EitherBoundary.either.?

object GantryUseCase:
    def buildChart(gantryFilePath: os.Path, outputDir: os.Path): Either[AppError, Unit] =
        either:
            val config = ConfigService.getConfig(gantryFilePath).?
            ChartService
                .generateToDirectory(config, outputDir)
                .?

    def installChart(gantryFilePath: os.Path, maybeReleaseName: Option[String]): Either[AppError, Unit] =
        either:
            val config  = ConfigService.getConfig(gantryFilePath).?
            val tempDir = os.temp.dir()
            ChartService.generateToDirectory(config, tempDir).?

            val releaseName = maybeReleaseName.getOrElse(config.name)
            ChartService.installChart(tempDir, releaseName).?
