job('demo') {
    steps {
        shell('echo Hello World!')
    }
}

pipelineJob("github-demo") {
    description("Upgrading baremetal firmware")
    logRotator {
        daysToKeep(90)
    }
    parameters {
        stringParam('Your name', '', 'Site name to display in build topic')
    }
}
