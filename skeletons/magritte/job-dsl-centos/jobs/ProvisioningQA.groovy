import util.AnsibleVars;

folder('Provisioning')
folder('Provisioning/QA')

job('Provisioning/QA/Provision') {
    deliveryPipelineConfiguration('QA Env', 'Provision')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
    }
    steps {
        copyArtifacts('Provisioning/CI/Checkout') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('**/*')
        }
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_FILE}' -l qa site.yml")
    }
    publishers {
        buildPipelineTrigger('Provisioning/Staging/Provision')
    }
}
