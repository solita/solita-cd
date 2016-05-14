package util;

import util.AnsibleVars;

class Pipeline {
    static checkOut(context) {
        context.with {
            if (AnsibleVars.PIPELINE_REPO_URL) {
                scm {
                    git {
                        remote {
                            url(AnsibleVars.PIPELINE_REPO_URL)
                        }
                        branch('master')
                        clean()
                    }
                }
            }

            if (AnsibleVars.PIPELINE_PATH) {
                steps {
                    // Don't fail the build even if rsync fails (it's probably because some
                    // files are missing the o+r permission).
                    shell("rsync -av '${AnsibleVars.PIPELINE_PATH}/' . || true")
                }
            }
        }
    }
}
