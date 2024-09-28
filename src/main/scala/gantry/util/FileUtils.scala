package gantry.util

import gantry.domain.AppError

import scala.util.Try

object FileUtils:
    def readFile(path: os.Path): Either[AppError.FailedToReadFile, String] =
        Try(os.read(path)).toEither.left.map(AppError.FailedToReadFile(path, _))
