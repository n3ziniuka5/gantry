package gantry.domain

import org.virtuslab.yaml.YamlEncoder

case class Deployment(
    apiVersion: String = "apps/v1",
    kind: String = "Deployment",
    metadata: Deployment.DeploymentMetadata,
    spec: Deployment.Spec
) derives YamlEncoder

object Deployment:
    val fileName = "templates/deployment.yaml"

    def generate(config: GantryConfig): (fileName: String, value: Deployment) =
        val imagePullPolicy = config.image.tag match
            case "latest" => "Always"
            case _        => "IfNotPresent"
        (
          fileName,
          Deployment(
            metadata = Deployment.DeploymentMetadata(
              name = Helpers.includeName,
              labels = Helpers.commonLabels
            ),
            spec = Deployment.Spec(
              replicas = 1, // TODO: Make this configurable
              selector = Map("matchLabels" -> Helpers.selectorLabels),
              template = Deployment.Template(
                metadata = Deployment.TemplateMetadata(
                  annotations = Map.empty, // TODO: Add pod annotations
                  labels = Helpers.commonLabels
                ),
                spec = Deployment.TemplateSpec(
                  containers = List(
                    Deployment.Container(
                      name = config.name,
                      image = s"${config.image.repository}:${config.image.tag}",
                      ports = config.ports.map(port =>
                          Deployment.Port(Some(port.name), port.containerPort, port.protocol.toModel)
                      ),
                      imagePullPolicy = imagePullPolicy
                    )
                  )
                )
              )
            )
          )
        )

    case class DeploymentMetadata(name: String, labels: Map[String, String]) derives YamlEncoder
    case class TemplateMetadata(annotations: Map[String, String], labels: Map[String, String]) derives YamlEncoder

    case class Spec(
        replicas: Int,
        selector: Map[String, Map[String, String]],
        template: Template
    ) derives YamlEncoder

    case class Template(
        metadata: TemplateMetadata,
        spec: TemplateSpec
    ) derives YamlEncoder

    case class TemplateSpec(
        containers: List[Container]
    ) derives YamlEncoder

    case class Container(
        name: String,
        image: String,
        ports: List[Port],
        imagePullPolicy: String
    ) derives YamlEncoder

    case class Port(
        name: Option[String],
        containerPort: Int,
        protocol: Protocol
    ) derives YamlEncoder
