package gantry.util

import gantry.domain.AppError

object ProcUtils:
    extension (p: os.proc)
        def callSafe(): Either[AppError, Unit] =
            val result = p.call(stdout = os.Inherit, stderr = os.Inherit, check = false)
            result.exitCode match
                case 0 => Right(())
                case _ => Left(AppError.FailedToRunCommand(p, result.exitCode))
