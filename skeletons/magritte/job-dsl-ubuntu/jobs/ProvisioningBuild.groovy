import util.Pipeline;
import util.AnsibleVars;

folder('Provisioning')
folder('Provisioning/Build')

job('Provisioning/Build/Checkout') {
    deliveryPipelineConfiguration('Build Env', 'Checkout')
    wrappers {
        deliveryPipelineVersion('checkout #$BUILD_NUMBER', true)
        timestamps()
        preBuildCleanup()
    }
    Pipeline.checkOut(delegate)
    publishers {
        archiveArtifacts {
            pattern('**/*')
            onlyIfSuccessful()
        }
        downstream('Provisioning/Build/Provision', 'SUCCESS')
    }
}

job('Provisioning/Build/Provision') {
    deliveryPipelineConfiguration('Build Env', 'Provision')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
        preBuildCleanup {
            excludePattern("${AnsibleVars.INVENTORY_ROOT}/build/solita_jenkins_default_password/solita_jenkins")
        }
        colorizeOutput()
    }
    steps {
        copyArtifacts('Provisioning/Build/Checkout') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('**/*')
        }
        shell("env ANSIBLE_FORCE_COLOR=true ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/build/inventory' site.yml -e '{solita_jenkins_restart: no}'")
    }
    publishers {
        archiveArtifacts {
            pattern('imagination/jenkins_id_rsa.pub')
            onlyIfSuccessful()
        }
        buildPipelineTrigger('Provisioning/QA/Provision')
    }
}
