folder('Provisioning') {
    primaryView('Pipeline')
}

deliveryPipelineView('Provisioning/Pipeline') {
    pipelineInstances(5)
    allowPipelineStart(true)
    enableManualTriggers(true)
    showAggregatedPipeline(true)
    allowRebuild(true)
    pipelines {
        component('Provisioning', 'Provisioning/CI/Checkout')
    }
}
