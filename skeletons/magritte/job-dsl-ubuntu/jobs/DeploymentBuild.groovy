import util.AnsibleVars;
import util.Pipeline;

folder('Deployment')
folder('Deployment/Build')

job('Deployment/Build/Build') {
    deliveryPipelineConfiguration('Build Env', 'Build')
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
        shell('mvn -B package')
        shell('mvn -B verify')
    }
    publishers {
        archiveJunit('target/failsafe-reports/*.xml')
        archiveArtifacts {
            pattern('target/hello.jar')
            pattern('start')
            pattern('stop')
            onlyIfSuccessful()
        }
        downstream('Deployment/Build/Deploy', 'SUCCESS')
    }
}

job('Deployment/Build/Deploy') {
    deliveryPipelineConfiguration('Build Env', 'Deploy')
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
        shell("env ANSIBLE_FORCE_COLOR=true ansible-playbook -i '${AnsibleVars.INVENTORY_ROOT}/build/inventory' deploy.yml")
    }
    publishers {
        downstream('Deployment/Build/E2ETest', 'SUCCESS')
    }
}

job('Deployment/Build/E2ETest') {
    deliveryPipelineConfiguration('Build Env', 'E2E Test')
    quietPeriod(0)
    wrappers {
        buildName('$PIPELINE_VERSION')
        timestamps()
        preBuildCleanup()
    }
    Pipeline.checkOut(delegate)
    steps {
        // Wait for the app server to start
        AnsibleVars.BUILD_APP_HOSTS.each { host ->
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
