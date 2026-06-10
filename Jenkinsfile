
pipeline { 
	agent any 
	tools { 
	 maven 'maven-3.9.16' 
	  } 
	stages { 
     stage('Checkout') { 
		steps { 
	     git branch: 'main', url: 'https://github.com/Divyadevi-g25/Selenium-TestNG-Framework1.git' 
		} 
	} 
	stage('Build') { 
	 steps { 
	  bat 'mvn clean install' 
	 } 
	} 
	
	stage('Test') { 
		steps { 
		 bat 'mvn clean test'
		} 
	} 
	stage('Reports') { 
		steps {
		    publishHTML(target: [ 
				reportDir: 'src/test/resources/ExtentReport', 
				reportFiles: 'ExtentReport.html', 
				reportName: 'Extent Report' 
			]) 
		} 
	  } 
     } 
     post { 
		always { 
			archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true 
			junit 'target/surefire-reports/*.xml' 
		} 
	success { 
		emailext ( to: 'divyadevi.ece@gmail.com', 
		subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}", 
		body: """ 
		<p>Hello Team,</p> 
		
		<p>The latest Jenkins build has completed successfully.</p> 
		
		<p><b>Project Name:</b> ${env.JOB_NAME}</p> 
		<p><b>Build Number:</b> #${env.BUILD_NUMBER}</p> 
		<p><b>Build Status:</b> <span style="color: green;"><b>SUCCESS</b></span></p> 
		<p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p> 
		
		<p><b>Last Commit:</b></p> 
		<p>${env.GIT_COMMIT}</b></p> 
		<p><b>BRANCH:</b>${env.GIT_COMMIT}</b></p> 
		
		<p><b>Build log is attached.</b></p> 
		
		<p><b>Extent Report:</b> <a href="http://localhost:8081/job/${env.JOB_NAME}/HTML_20Extent_20Report/">Click here</a></p> 
		
		<p><b>Automation Team</b></p> 
		""", 
		mimeType: 'text/html', 
		attachLog: true 
		) 
	}
	 failure { 
             emailext ( 
                 to: 'divyadevi.ece@gmail.com', 
                 subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}", 
                 body: """ 
                   
                 <p>Hello Team,</p> 
                 <p>The latest Jenkins build has <b style="color: red;">FAILED</b>.</p> 
                 <p><b>Project Name:</b> ${env.JOB_NAME}</p> 
                 <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p> 
                 <p><b>Build Status:</b> <span style="color: red;"><b>FAILED ❌</b></span></p> 
                 <p><b>Build URL:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p> 
                 
                 <p><b>Last Commit:</b></p> 
				 <p>${env.GIT_COMMIT}</b></p> 
		         <p><b>BRANCH:</b>${env.GIT_COMMIT}</b></p> 
		
		         <p><b>Build log is attached.</b></p> 
		
                 <p><b>Please check the logs and take necessary actions.</b></p> 
                 
                 <p><b>Extent Report (if available):</b> <a href="http://localhost:8080/job/${env.JOB_NAME}/HTML_20Extent_20Report/">Click here</a></p> 
                 
                 <p>Best regards,</p> 
                 <p><b>Automation Team</b></p> 		
                 
                 """, 
                 mimeType: 'text/html', 
                 attachLog: true 
                 ) 
              } 
    } 
  }
