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
        preBuildCleanup()
        colorizeOutput()
    }
    Pipeline.checkOut(delegate)
    steps {
        copyArtifacts('Deployment/Build/Build') {
            buildSelector() {
                upstreamBuild(true)
            }
            includePatterns('**/*')
            flatten()
        }
        shell("env ANSIBLE_FORCE_COLOR=true ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/prod/inventory' deploy.yml")
    }
}
