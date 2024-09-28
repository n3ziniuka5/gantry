package gantry.domain

import org.virtuslab.yaml.YamlCodec

case class Deployment(
    apiVersion: String = "apps/v1",
    kind: String = "Deployment",
    metadata: Deployment.DeploymentMetadata,
    spec: Deployment.Spec
) derives YamlCodec

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
                      ports = List(Map("containerPort" -> 80)), // TODO: Make this configurable
                      imagePullPolicy = imagePullPolicy
                    )
                  )
                )
              )
            )
          )
        )

    case class DeploymentMetadata(name: String, labels: Map[String, String]) derives YamlCodec
    case class TemplateMetadata(annotations: Map[String, String], labels: Map[String, String]) derives YamlCodec

    case class Spec(
        replicas: Int,
        selector: Map[String, Map[String, String]],
        template: Template
    ) derives YamlCodec

    case class Template(
        metadata: TemplateMetadata,
        spec: TemplateSpec
    ) derives YamlCodec

    case class TemplateSpec(
        containers: List[Container]
    ) derives YamlCodec

    case class Container(
        name: String,
        image: String,
        ports: List[Map[String, Int]],
        imagePullPolicy: String
    ) derives YamlCodec
