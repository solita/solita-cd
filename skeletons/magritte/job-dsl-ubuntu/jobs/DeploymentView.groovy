folder('Deployment') {
    primaryView('Pipeline')
}

deliveryPipelineView('Deployment/Pipeline') {
    pipelineInstances(5)
    allowPipelineStart(true)
    enableManualTriggers(true)
    allowRebuild(true)
    showAggregatedPipeline(true)
    pipelines {
        component('Deployment', 'Deployment/CI/Build')
    }
}
