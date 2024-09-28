package gantry.services

import gantry.domain.{AppError, GantryConfig}
import gantry.util.EitherBoundary.either
import gantry.util.EitherBoundary.either.get
import gantry.util.FileUtils
import org.virtuslab.yaml.*

object ConfigService:
    def getConfig(from: os.Path): Either[AppError, GantryConfig] =
        either:
            val content = FileUtils.readFile(from).get
            content.as[GantryConfig].left.map(AppError.FailedToParseYaml(from, _)).get
