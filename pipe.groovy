pipeline {
    agent any

    environment {
        mvn_home = '/opt/maven/apache-maven-3.8.5/bin/mvn'
        //File_name= '*.jar'
        //ARTIFACTORY_SERVER_ID='artifactory1'
        //multiplefileupload="["*.jar","*.war"]"
        //filestoupload="*.jar"
    
    }

    stages {
        stage('Cloning') {
            steps {
                git 'https://github.com/pmrubenrao/time-tracker.git'
            }
         }
         stage(' buiding using maven') {
             steps {
                 sh "${mvn_home} -Dmaven.test.failure.ignore=true clean package" 
             }
         }
	  stage('Artifactory server details'){
        steps {
		 rtServer (
            id: 'Artifactory-1',
			url: 'https://hemalataanjoori.jfrog.io/artifactory',
			username: 'hema',
			password: 'Artifactoryhem@789',
			
			bypassProxy: true,
			
			timeout: 300
            )
		 }
	   }
	   
         stage('deploying to artifactory'){
            
             steps {
                 rtUpload (
    serverId: 'Artifactory-1',
    spec: '''{
         "files: [
              {
                    "pattern": "*.jar",
                    "target": "happy"
    
                },
              {
                    "pattern": "*.war",
                    "target": "happy"
    
                },
              
         ]"
  
    }''',
 
    // Optional - Associate the uploaded files with the following custom build name and build number,
    // as build artifacts.
    // If not set, the files will be associated with the default build name and build number (i.e the
    // the Jenkins job name and number).
   // buildName: 'holyFrog',
    //buildNumber: '42',
    // Optional - Only if this build is associated with a project in Artifactory, set the project key as follows.
   // project: 'my-project-key'
)
                  
                      
                  }
                  }
         }
        }
		
		stage ('Publish build info') {
            rtPublishBuildInfo (
                    serverId: "Artifactory-1"
                )
            }
        
    
	