up:
    #!/usr/bin/env bash
    if ! kind get clusters | grep -q "^gantry$"; then
        echo "Creating new kind cluster 'gantry'..."
        kind create cluster --name gantry
    else
        echo "Kind cluster 'gantry' already exists. Skipping creation."
    fi
    kubectl config use-context kind-gantry

down: clean
    #!/usr/bin/env bash
    if kind get clusters | grep -q "^gantry$"; then
        echo "Deleting kind cluster 'gantry'..."
        kind delete cluster --name gantry
    else
        echo "Kind cluster 'gantry' does not exist. Skipping deletion."
    fi

build-chart:
    SBT_NATIVE_CLIENT=true ./sbt "run helm build -o generated-chart"

install-chart: up
    SBT_NATIVE_CLIENT=true ./sbt "run helm install"

uninstall-chart:
    helm uninstall test-application --wait

reinstall-chart: uninstall-chart install-chart

clean:
    SBT_NATIVE_CLIENT=true ./sbt "clean"
    rm -rf generated-chart

build:
    SBT_NATIVE_CLIENT=true ./sbt "compile"
