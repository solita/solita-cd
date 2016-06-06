import util.AnsibleVars;
import util.Pipeline;

folder('Deployment')
folder('Deployment/Prod')

job('Deployment/Prod/Deploy') {
    deliveryPipelineConfiguration('Prod Env', 'Deploy')
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
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/prod/inventory' deploy.yml")
    }
}
