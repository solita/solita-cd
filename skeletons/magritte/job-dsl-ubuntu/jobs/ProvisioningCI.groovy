import util.Pipeline;
import util.AnsibleVars;

folder('Provisioning')
folder('Provisioning/CI')

job('Provisioning/CI/Checkout') {
    deliveryPipelineConfiguration('CI Env', 'Checkout')
    wrappers {
        deliveryPipelineVersion('checkout #$BUILD_NUMBER', true)
        timestamps()
    }
    Pipeline.checkOut(delegate)
    publishers {
        archiveArtifacts {
            pattern('**/*')
            onlyIfSuccessful()
        }
        downstream('Provisioning/CI/Provision', 'SUCCESS')
    }
}

job('Provisioning/CI/Provision') {
    deliveryPipelineConfiguration('CI Env', 'Provision')
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
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_FILE}' -l ci site.yml")
    }
    publishers {
        buildPipelineTrigger('Provisioning/QA/Provision')
    }
}
