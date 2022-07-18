# SonarCloud

SonarQube analyzes branches and Pull Requests so you spot and resolve issues BEFORE you merge to main.

### To set up SonarCloud locally run the following command :

* install SonarLint plugin
* go to Preferences -> Tools -> SonarLint
* tick Bind Project to SonarQube/SonarCloud
* go to Configure connection
* press + to create new connection
* choose sonarCloud and enter any name 
* In the next step enter the token (provided in slack) and add a new connection 
* go back to SonarLint -> Project Settings and choose your connection from the dropdown list 
* enter 'maingroon_svydovets-bring' as Project_key and apply changes

### Run SonarCloud locally

In the bottom action bar you should see a new tab called SonarLint otherwise just search SonarLint through the IDE search     
In SonarLint there's a Report tab where you can Analyse All Project Files