import util.AnsibleVars;

folder('Provisioning')
folder('Provisioning/Staging')

job('Provisioning/Staging/Provision') {
    deliveryPipelineConfiguration('Staging Env', 'Provision')
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
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_FILE}' -l staging site.yml")
    }
    publishers {
        buildPipelineTrigger('Provisioning/Prod/Provision')
    }
}
