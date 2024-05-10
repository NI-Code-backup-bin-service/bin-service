library identifier: 'jenkins-shared-libs@stable', retriever: modernSCM(
        [$class: 'GitSCMSource',
         remote: 'git@bitbucket.org:network-international/jenkins-shared-libs.git',
         credentialsId: 'gitcreds']
) _

buildPushDeploy appName: 'bin-service',
       gitProvider: 'bitbucket.org',
       appRepo: 'network-international',
       isDependencies: false,
       environment: 'dev'
