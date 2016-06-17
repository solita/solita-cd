import util.AnsibleVars;
import util.Pipeline;

folder('Deployment')
folder('Deployment/CI')

job('Deployment/CI/Build') {
    deliveryPipelineConfiguration('CI Env', 'Build')
    wrappers {
        deliveryPipelineVersion('build #$BUILD_NUMBER', true)
        timestamps()
    }
    scm {
        git {
            remote {
                url('https://github.com/noidi/hello-java.git')
            }
            branch('master')
            extensions {
                cleanAfterCheckout()
            }
        }
    }
    triggers {
        scm('*/15 * * * *')
    }
    steps {
        shell('echo $BUILD_NUMBER > src/main/resources/build.txt')
        shell('mvn package')
        shell('mvn verify')
    }
    publishers {
        archiveJunit('target/failsafe-reports/*.xml')
        archiveArtifacts {
            pattern('target/hello.jar')
            pattern('start')
            pattern('stop')
            onlyIfSuccessful()
        }
        downstream('Deployment/CI/Deploy', 'SUCCESS')
    }
}

job('Deployment/CI/Deploy') {
    deliveryPipelineConfiguration('CI Env', 'Deploy')
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
        shell("ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/ci/inventory' deploy.yml")
    }
    publishers {
        downstream('Deployment/CI/E2ETest', 'SUCCESS')
    }
}

job('Deployment/CI/E2ETest') {
    deliveryPipelineConfiguration('CI Env', 'E2E Test')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
    }
    Pipeline.checkOut(delegate)
    steps {
        // Wait for the app server to start
        AnsibleVars.CI_APP_HOSTS.each { host ->
            shell("""\
            for i in \$(seq 10); do
                curl -s http://${host}:4567 && exit 0
                sleep 1
            done
            echo 'The app server failed to start in 10 seconds!'
            exit 1
            """.stripIndent())
        }
    }
    publishers {
        buildPipelineTrigger('Deployment/QA/Deploy')
    }
}
