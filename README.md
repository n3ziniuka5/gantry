# Gantry

> **Note:** Gantry binaries are not yet published. The project is currently in development.

Gantry is a tool designed to simplify Kubernetes deployments by abstracting away the intricacies and challenges often associated with Kubernetes and Helm charts.

## Overview

[Helm](https://github.com/helm/helm) and [Helmfile](https://github.com/helmfile/helmfile) are amazing tools, and they are required for Gantry to work. However, development teams using these tools often struggle with:
- Steep learning curve for team members not familiar with Kubernetes and Helm
- Enforcing common configuration across charts, e.g. making sure every deployment contains a pod disruption budget
- Creation, maintenance and storage of Helm charts

## Example Usage

create a `gantry.yaml` file for your application, here's an example one for nginx:
```yaml
name: nginx

image:
  repository: nginx
  tag: latest

ports:
  - name: http
    containerPort: 80 # the port the container listens on
    servicePort: 80 # if specified, creates a corresponding Service resource so that the pod is accessible via the service port
```

Then run `gantry helm install` to generate a Helm chart and install it into your cluster. You do not need to publish the Helm chart to a remote repository.

This creates a Helm release named `nginx`, and then you can use standard Helm commands to interact with the release, such as `helm uninstall` or `helm rollback`.

# Development

## Prerequisites

For local development and testing, we use the following tools:

1. **Devbox**: A command-line tool that lets you easily create isolated shells for development. It's used to manage project dependencies and provide a consistent development environment.

   To get started with Devbox:
   ```
   curl -fsSL https://get.jetpack.io/devbox | bash
   devbox shell
   # optionally install direnv to automatically initialize devbox - https://direnv.net/#basic-installation
   direnv allow
   ```

1. **Just**: A handy command runner that serves as a modern alternative to `make`. It's used to define and run project-specific commands.

   To run a command defined in the `Justfile`:
   ```
   just <command-name>
   ```
   Commands:
   - `build`: Compile the project
   - `clean`: Clean build artifacts
   - `up`: Start Kind cluster for local testing
   - `down`: Remove local Kind cluster
   - `install-chart`: Install Helm chart
   - `uninstall-chart`: Uninstall Helm chart
   - `reinstall-chart`: Reinstall Helm chart
   - `build-chart`: Generate Helm chart to `generated-chart` directory

1. **SBT (Scala Build Tool)**: The de facto build tool for Scala projects. It's used to compile, test, and run the Scala code.

   To start an SBT session:
   ```
   ./sbt
   ```

1. **Docker**: Used for containerizing the application and running it locally.

   Make sure you have Docker installed and running on your machine.


## TODO

- [x] Basic deployment configuration
- [x] Service and port configuration
- [ ] Use HOCON instead of Yaml to configure Gantry
- [ ] Publish binaries
- [ ] Publish doc site
- [ ] Resource and number of replicas configuration
- [ ] Liveness/Readiness probes
- [ ] Reference/include other configuration files, e.g. to share between applications
- [ ] Deployment strategy configuration
- [ ] Pod Disruption Budget configuration
- [ ] Secret configuration
- [ ] Config configuration
- [ ] Write tests
- [ ] Pre-stop wait to improve networking
- [ ] Sidecar support
- [ ] Cronjob support
- [ ] Option to replace deployment on service major version change
- [ ] Configurable entrypoint and command
- [ ] Add "Gantry Compose" to deploy multipe services
- [ ] GitHub action to install Gantry CLI
