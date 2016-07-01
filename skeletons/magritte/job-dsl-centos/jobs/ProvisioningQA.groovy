import util.AnsibleVars;

folder('Provisioning')
folder('Provisioning/QA')

job('Provisioning/QA/Provision') {
    deliveryPipelineConfiguration('QA Env', 'Provision')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
        preBuildCleanup()
        colorizeOutput()
    }
    steps {
        copyArtifacts('Provisioning/Build/Checkout') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('**/*')
        }
        copyArtifacts('Provisioning/Build/Provision') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('imagination/jenkins_id_rsa.pub')
        }
        shell("env ANSIBLE_FORCE_COLOR=true ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/qa/inventory' site.yml")
    }
    publishers {
        buildPipelineTrigger('Provisioning/Prod/Provision')
    }
}
