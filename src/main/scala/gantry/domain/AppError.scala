package gantry.domain

import org.virtuslab.yaml.YamlError

enum AppError:
    case FailedToReadFile(path: os.Path, cause: Throwable)
    case FailedToParseYaml(path: os.Path, cause: YamlError)
    case InvalidPath(path: String, cause: Throwable)
    case FailedToRemoveFile(path: os.Path, cause: Throwable)
    case FailedToWriteFile(path: os.Path, cause: Throwable)
    case FailedToRunCommand(command: os.proc, exitCode: Int)

    def message: String = this match
        case InvalidPath(path: String, cause: Throwable) => s"Invalid path: $path: $cause"
        case FailedToReadFile(path: os.Path, cause: Throwable) =>
            s"Could not read file: $cause"
        case FailedToParseYaml(path: os.Path, cause: YamlError)  => s"Could not parse $path as yaml file: $cause"
        case FailedToRemoveFile(path: os.Path, cause: Throwable) => s"Could not remove file $path: $cause"
        case FailedToWriteFile(path: os.Path, cause: Throwable)  => s"Could not write file $path: $cause"
        case FailedToRunCommand(command: os.proc, exitCode: Int) =>
            s"Command \"${command.commandChunks.mkString(" ")}\" failed with exit code $exitCode"
