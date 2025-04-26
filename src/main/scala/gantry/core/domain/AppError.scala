package gantry.core.domain

enum AppError:
    case FailedToReadFile(path: os.Path, cause: Throwable)
    case FailedToParseConfig(path: os.Path, cause: Throwable)
    case InvalidPath(path: String, cause: Throwable)
    case FailedToRemoveFile(path: os.Path, cause: Throwable)
    case FailedToWriteFile(path: os.Path, cause: Throwable)
    case FailedToRunCommand(command: os.proc, exitCode: Int)

    def message: String = this match
        case InvalidPath(path: String, cause: Throwable) => s"Invalid path: $path: $cause"
        case FailedToReadFile(path: os.Path, cause: Throwable) =>
            s"Could not read file: $cause"
        case FailedToParseConfig(path: os.Path, cause: Throwable) =>
            s"Could not parse $path as a HOCON config file: $cause"
        case FailedToRemoveFile(path: os.Path, cause: Throwable) => s"Could not remove file $path: $cause"
        case FailedToWriteFile(path: os.Path, cause: Throwable)  => s"Could not write file $path: $cause"
        case FailedToRunCommand(command: os.proc, exitCode: Int) =>
            s"Command \"${command.commandChunks.mkString(" ")}\" failed with exit code $exitCode"
