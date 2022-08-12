base_path = 'cicd'
job_path = "${base_path}/GenericSeed"
folder("${base_path}")

freeStyleJob("${job_path}") {
    logRotator{
        daysToKeep(90)
    }
    label('master')
    parameters {
        stringParam('GERRIT_REFSPEC', 'origin/main', 'Gerrit Refspec')
        stringParam('GERRIT_BRANCH', 'main', 'Gerrit branch')
        stringParam('UPDATED_SITES', '', 'Comma delimited sites for pipelines update (as in site repo)\n' +
                        'By default al sites with the same cicdRef will be updated.\n' +
                        'Example: mtn15b.1,mtn13b.1,auk3')
        booleanParam {
                 defaultValue(true)
                 description('Custom update pipelines')
                 name('CUSTOM_LIST')
             }
        activeChoiceReactiveParam('CUSTOM_LIST_FEATURES') {
             description('Select pipelines to update')
             choiceType('CHECKBOX')
             filterable(false)
             groovyScript {
                 script("if (CUSTOM_LIST) {return ['DEPLOY:selected', 'UPDATE:selected', 'REDEPLOY_NODE:selected', 'RT', 'CERTIFICATE_ROTATION:selected', 'ROTATION', 'GHOST', 'HEALTH_CHECK:selected', 'MANAGE_OPENSTACK_VALIDATION_TENANT:selected']} else {return ['Update all pipelines']}")
                 }
             referencedParameter('CUSTOM_LIST')
             }
        choiceParam('GERRIT_HOST',
                    ['gerrit', 'CodeCloud'],
                    'Host for clone nc-cicd repo'
                   )
    }

    steps {
    //Wipe the workspace:
        wrappers {
            preBuildCleanup()
            sshAgent("${INTERNAL_GERRIT_KEY}")
        }
        shell(readFileFromWorkspace("${job_path}/superseed.sh"))
        jobDsl {
            /* groovylint-disable-next-line GStringExpressionWithinString */
            targets('${BUILD_NUMBER}/**/seed*.groovy')
            // Add ignoreMissingFiles to ignore when seeds are not copied for patchsets
            ignoreMissingFiles(true)
        }
    }
}
