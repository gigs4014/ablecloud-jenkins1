import java.text.SimpleDateFormat
def NEW_DATE = "null"
pipeline {
    agent any

    environment {
        JWF = '/mnt/jenkins-work'
        BRF = '${JWF}/build'
    }

    stages {
       stage('Version Change') {
           steps {
               // build job : 'works_build_test'
               script {
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
                   println(newVersionInfo)

                   writeFile(file: '${JWF}/versionInfo.txt', text: newVersionInfo)

                   sh('cat ${JWF}/versionInfo.txt')
                   sh('mkdir ${JWF}/'+newDate)
                   NEW_DATE=newDate
               }
           }
       }

       stage('Cockpit Build') {
//            steps{
//                build 'cockpit'
//            }
       }

//         stage('Glue Build') {
//             steps{
//                 build 'glue-build'
//             }
//        }

       stage('Glue RPM Copy to mirroring') {
           steps{
               sh("""ssh root@10.10.0.10 'rm -rf /data/repos/centos/glue/*'""")
               sh("""scp ${BRF}/*Glue*.rpm 10.10.0.10:/data/repos/centos/glue""")
               sh("""ssh root@10.10.0.10 'createrepo /data/repos/centos/glue/.'""")
           }
       }
//
//        stage('Glue Image Build And DockerHub Push') {
//            steps{
//                build 'glue-image'
//            }
//        }

        stage('Mold Build') {
//             steps{
//                 build 'mold'
//             }
        }

        stage('Netdive Build') {
            steps{
                build 'netdive-ui'
            }
        }

        stage('Wall Build') {
            steps{
                build 'wall-build'
            }
        }

       stage('Build result file Move to version folder') {
           steps{
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
                sh("""ssh root@10.10.1.2 'rm -rf /var/lib/libvirt/images/ablestack-cerato.qcow2'""")
                sh("""ssh root@10.10.1.2 'cp /var/lib/libvirt/images/ablestack-cerato-original.qcow2 /var/lib/libvirt/images/ablestack-cerato.qcow2'""")
                sh("""ssh root@10.10.1.2 'virsh create /var/lib/libvirt/images/ablestack-cerato.xml'""")

//                server_ip="10.10.0.1"
//                SYS_PING="ping -c 5 ${server_ip} | grep 'received' | awk -F',' '{print \$2}'"
//                echo "Pinging ${server_ip} with response: "${SYS_PING}" "
            }
        }
    }
}
