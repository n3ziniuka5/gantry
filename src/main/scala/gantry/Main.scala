package gantry

import caseapp.core.app.CommandsEntryPoint
import caseapp.core.argparser.ArgParser
import caseapp.core.help.{RuntimeCommandHelp, RuntimeCommandsHelp}
import caseapp.{Command, HelpMessage, Name, RemainingArgs}
import gantry.domain.AppError
import gantry.usecases.GantryUseCase
import gantry.util.DtoConversions.given_ArgParser_Path
import caseapp.Recurse

case class CommonHelmOptions(
    @Name("f")
    gantryFile: os.Path = os.pwd / "gantry.yaml",
)

case class BuildOptions(
    @Recurse
    common: CommonHelmOptions,
    @Name("o")
    outputDirectory: os.Path
)

case class InstallOptions(
    @Recurse
    common: CommonHelmOptions,
    @Name("n")
    @HelpMessage(
      "The name of the release, defaults to application name. Useful when installing multiple instances of the same application."
    )
    releaseName: Option[String] = None
)

case object Build extends Command[BuildOptions]:
    override def names: List[List[String]] = List(List("helm", "build"))

    override def help = super.help.copy(helpMessage = Some(HelpMessage("build a helm chart from gantry config")))

    override def run(options: BuildOptions, remainingArgs: RemainingArgs): Unit =
        runAndReportError:
            GantryUseCase.buildChart(options.common.gantryFile, options.outputDirectory)

case object Install extends Command[InstallOptions]:
    override def names: List[List[String]] = List(List("helm", "install"))

    override def help = super.help.copy(helpMessage = Some(HelpMessage("install a helm chart from gantry config")))

    override def run(options: InstallOptions, remainingArgs: RemainingArgs): Unit =
        runAndReportError:
            GantryUseCase.installChart(options.common.gantryFile, options.releaseName)

object Main extends CommandsEntryPoint:
    override val progName = "gantry"
    override val commands = List(
      Build,
      Install
    )

    override def help: RuntimeCommandsHelp =
        super.help.copy(commands = commands.map(cmd => RuntimeCommandHelp(cmd.names, cmd.help, cmd.group, cmd.hidden)))

def runAndReportError(f: => Either[AppError, Unit]): Unit =
    f match
        case Right(_)  => ()
        case Left(err) => System.err.println(err.message)
