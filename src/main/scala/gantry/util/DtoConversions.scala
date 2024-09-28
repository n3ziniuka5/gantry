package gantry.util

import gantry.domain.AppError
import scala.util.Try
import caseapp.core.argparser.ArgParser

object DtoConversions:
    given ArgParser[os.Path] =
        ArgParser.string.xmapError(_.toString, _.toPath.left.map(e => caseapp.core.Error.Other(e.message)))

    extension (self: String)
        def toPath: Either[AppError, os.Path] =
            toPath(os.pwd)
        def toPath(base: os.Path): Either[AppError, os.Path] =
            Try(os.Path(self, base)).toEither.left.map(AppError.InvalidPath(self, _))
