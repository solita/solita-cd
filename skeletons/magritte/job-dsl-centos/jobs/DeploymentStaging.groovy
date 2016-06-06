import util.AnsibleVars;
import util.Pipeline;

folder('Deployment')
folder('Deployment/Staging')

job('Deployment/Staging/Deploy') {
    deliveryPipelineConfiguration('Staging Env', 'Deploy')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
    }
    Pipeline.checkOut(delegate)
    steps {
        copyArtifacts('Deployment/CI/Build') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('**/*')
            flatten()
        }
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/staging/inventory' deploy.yml")
    }
    publishers {
        buildPipelineTrigger('Deployment/Prod/Deploy')
    }
}
