package gantry.domain

import org.virtuslab.yaml.*

case class Service(
    apiVersion: String = "v1",
    kind: String = "Service",
    metadata: Service.Metadata,
    spec: Service.Spec
) derives YamlEncoder

object Service:
    val fileName = "templates/service.yaml"

    case class Metadata(
        name: String,
        labels: Map[String, String]
    ) derives YamlEncoder

    case class Spec(
        `type`: String,
        selector: Map[String, String],
        ports: List[Port]
    ) derives YamlEncoder

    case class Port(
        name: Option[String],
        port: Int,
        protocol: Protocol,
        targetPort: String
    ) derives YamlEncoder

    def generate(config: GantryConfig): Option[(fileName: String, value: Service)] =
        if !config.ports.exists(_.servicePort.isDefined) then None
        else
            Some(
              fileName,
              Service(
                metadata = Metadata(
                  name = Helpers.includeName,
                  labels = Helpers.commonLabels
                ),
                spec = Spec(
                  `type` = "ClusterIP",
                  selector = Helpers.selectorLabels,
                  ports = config.ports
                      .filter(_.servicePort.isDefined)
                      .map(port =>
                          Port(
                            name = Some(port.name),
                            port = port.servicePort.get,
                            protocol = port.protocol.toModel,
                            targetPort = port.name
                          )
                      )
                )
              )
            )
