package gantry.util

import scala.util.boundary
import scala.util.boundary.{Label, break}

object EitherBoundary:
    object either:
        case class EitherBoundary[+E](error: E)

        def apply[E, A](body: Label[EitherBoundary[E]] ?=> A): Either[E, A] =
            boundary(body) match
                case EitherBoundary(error) => Left(error.asInstanceOf[E])
                case a                     => Right(a.asInstanceOf[A])

        extension [E, A](either: Either[E, A])(using label: Label[EitherBoundary[E]])
            def get: A =
                either match
                    case Right(a) => a
                    case Left(e)  => break(EitherBoundary(e))
