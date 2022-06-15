import java.text.SimpleDateFormat
def NEW_DATE = "null"
pipeline {
    agent any

    environment {
        JWF = '/mnt/jenkins-work'
        BRF = '/mnt/jenkins-work/build'
        SF = '/mnt/jenkins-work/service'
        SBINF = '/mnt/jenkins-work/sbin'
    }

    stages {
       stage('Version Change') {
           steps {
               // build job : 'works_build_test'
               script {
                   sh('rm -rf ${BRF}/*')
                   def versionInfo = readFile(file: '/mnt/jenkins-work/versionInfo.txt')
                   println(versionInfo)
                   def (codeName, version, date) = versionInfo.split('-')
                   println(codeName+version+date)
                   def (a,b,c) = version.split('\\.')
                   int intC = c as int
                   intC = intC +1
                   println(intC)
                   def dateFormat = new SimpleDateFormat('MMdd')
                   def today = new Date()
                   def newDate = dateFormat.format(today)
                   def newVersionInfo = codeName + '-' + a + '.' + b + '.' + intC.toString() + '-' + newDate
                   println('newVersionInfo : '+newVersionInfo)
                   writeFile file: '/mnt/jenkins-work/versionInfo.txt', text: newVersionInfo

                   sh('cat ${JWF}/versionInfo.txt')
                   sh('rm -rf ${JWF}/'+newDate)
                   sh('mkdir ${JWF}/'+newDate)
                   NEW_DATE=newDate
               }
           }
       }

       stage('Cockpit Build') {
           steps{
//                sh('echo Cockpit Build')
               build 'cockpit'
           }
       }
        
        stage('Cockpit-plugin Build') {
           steps{
//                sh('echo Cockpit Build')
               build 'cockpit-plugin'
           }
       }

        stage('Glue Build') {
            steps{
//                 sh('echo Glue Build')
                build 'glue-build'
            }
       }

       stage('Glue RPM Copy to mirroring') {
           steps{
//                sh('echo Glue RPM Copy to mirroring 완료')
               sh("""ssh root@10.10.0.10 'rm -rf /data/repos/centos/glue/*'""")
               sh("""scp ${BRF}/*Glue*.rpm 10.10.0.10:/data/repos/centos/glue""")
               sh("""ssh root@10.10.0.10 'createrepo /data/repos/centos/glue/.'""")
           }
       }

       stage('Glue Image Build And DockerHub Push') {
           steps{
//                sh('echo Glue Image Build And DockerHub Push 완료')
               build 'glue-image'
           }
       }

        stage('Mold Build') {
            steps{
//                 sh('echo Mold Build')
                build 'mold'
            }
        }

        stage('Netdive Build') {
            steps{
//                 sh('echo Netdive Build-220530 완료')
                build 'netdive-ui'
            }
        }

        stage('Wall Build') {
            steps{
//                 sh('echo Netdive Build')
                build 'wall-build'
            }
        }

       stage('Build result file Move to version folder') {
           steps{
//                 sh('echo Build result file Move to version folder 완료')
//                 sh("""cp ${BRF}/cloudstack-agent-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-agent-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-baremetal-agent-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-baremetal-agent-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-cli-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-cli-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-common-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-common-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-integration-tests-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-integration-tests-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-management-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-management-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-marvin-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-marvin-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-mysql-ha-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-mysql-ha-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-ui-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-ui-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cloudstack-usage-4.17.0.0-SNAPSHOT.1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cloudstack-usage-4.17.0.0-SNAPSHOT.1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cockpit-255.v2.0.12.0525-1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cockpit-255.v2.0.12.0525-1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cockpit-bridge-255.v2.0.12.0525-1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cockpit-bridge-255.v2.0.12.0525-1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cockpit-packagekit-255.v2.0.12.0525-1.el8.noarch.rpm ${JWF}/${NEW_DATE}/cockpit-packagekit-255.v2.0.12.0525-1.el8.noarch-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cockpit-pcp-255.v2.0.12.0525-1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cockpit-pcp-255.v2.0.12.0525-1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/cockpit-ws-255.v2.0.12.0525-1.el8.x86_64.rpm ${JWF}/${NEW_DATE}/cockpit-ws-255.v2.0.12.0525-1.el8.x86_64-${NEW_DATE}.rpm""")
//                 sh("""cp ${BRF}/skydive ${JWF}/${NEW_DATE}/skydive-${NEW_DATE}""")
//                 sh("""cp -r ${BRF}/grafana ${JWF}/${NEW_DATE}/grafana-${NEW_DATE}""")
                  sh("""cp -r ${BRF}/* ${JWF}/${NEW_DATE}/""")
           }
       }

        stage('Ablestack Template Create') {
            steps{
//                 sh('echo Build result file Move to version folder 완료')
                build 'make-qcow2-template'
            }
        }
        
        stage('Ablestack ISO Create') {
            steps{
//                 sh('echo Build result file Move to version folder 완료')
                build 'ablestack-kickstart'
            }
        }
        
        stage('Ablestack ISO Copy') {
            steps{
                sh("""rm -rf ${JWF}/ISO/*""")
                sh("""cp -r ${JWF}/ISO/* ${JWF}/${NEW_DATE}/""")
            }
        }
    }
}
