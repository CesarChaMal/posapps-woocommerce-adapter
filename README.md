# [posApps](https://posapps.io) WooCommerce Adapter 
### Free and Open source #FOSS #GNU-GPL3
This project is provided free and open sourced under the [GNU GPL3 License](https://www.gnu.org/licenses/quick-guide-gplv3.html)

---
### What is the purpose project?
* This is a REST API (micro service) to manage WooCommerce data. 
* It is the rest API that [uniCenta oPos](https://unicenta.com) uses to synchronize WooCommerce data across many devices and services.
* Use the API endpoints to connect with your WooCommerce API.
* Documentation for each endpoint can be found [here](http://docs.posapps.io/) 

---
### System requirements 

##### [Groovy](http://groovy-lang.org/)
##### [Gradle](https://gradle.org/)
##### [NodeJS](https://nodejs.org/) 
##### [Serverless](https://serverless.com/)
##### [MySQL](https://www.mysql.com/)

---
### How to build the project

#### Database
* The project requires persistence and uses MySql as the default data-source
* To be able to run the tests locally you will need to create a schema with with following details:
    * schema name: **posapps_woo_customer_test**
    * user name: **posapps_test**
    * password: **password**
* The project uses [flyway](https://flywaydb.org/) for migrations.

#### Gradle
* This project uses gradle as its build tool
* ```./gradlew clean build```

---
### Continuous Integration / Deployments
* We use the [bitbucket pipeline](./bitbucket-pipelines.yml) as our CI solution
* Each time there is a commit the default pipeline will be triggered which does the following:
    * Builds the project ```./gradlew clean build```
    * Run tests
    * Deploys the build artifact to aws as a lambda ```serverless deploy```

---
### Serverless Deployments
* This is a [serverless](https://serverless.com/) project and uses [AWS lambda](https://aws.amazon.com/lambda/) as its implementation
* To deploy to your own environments you will need to configure your own aws credentials
---
### Code contributions / Pull Requests
* If you would like to make a pull request into master please add hughs@unicenta.com (project owner) to the PR with a detailed description and tests illustrating the changes and impacts.

---
### Architecture 
![Alt text](https://confluence-connect.gliffy.net/embed/image/8aef5aa9-9c47-4b79-8c5c-23e973c9de67.png?utm_medium=live&utm_source=custom)

---