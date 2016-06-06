import util.AnsibleVars;

folder('Provisioning')
folder('Provisioning/Staging')

job('Provisioning/Staging/Provision') {
    deliveryPipelineConfiguration('Staging Env', 'Provision')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
        preBuildCleanup()
    }
    steps {
        copyArtifacts('Provisioning/CI/Checkout') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('**/*')
        }
        copyArtifacts('Provisioning/CI/Provision') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('jenkins_id_rsa.pub')
        }
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/staging/inventory' site.yml")
    }
    publishers {
        buildPipelineTrigger('Provisioning/Prod/Provision')
    }
}
