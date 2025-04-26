package gantry.core.services

import gantry.core.domain.{AppError, GantryConfig}
import gantry.core.util.EitherBoundary.either
import gantry.core.util.EitherBoundary.either.?
import com.typesafe.config.ConfigFactory
import scala.util.Try
import com.typesafe.config.ConfigParseOptions
import pureconfig.*

object ConfigService:
    def getConfig(from: os.Path): Either[AppError, GantryConfig] =
        either:
            val lightbendConfig = Try:
                ConfigFactory.parseFile(from.toIO, ConfigParseOptions.defaults().setAllowMissing(false))
            .toEither.left
                .map(AppError.FailedToParseConfig(from, _))
                .?

            ConfigSource
                .fromConfig(lightbendConfig)
                .load[GantryConfig]
                .left
                .map(errors => AppError.FailedToParseConfig(from, RuntimeException(errors.prettyPrint())))
                .?
