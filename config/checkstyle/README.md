# Checkstyle

All the configurations are located in /config/checkstyle/

### To execute checkstyle locally run the following command :

* On Unix-like systems:
  `./gradlew checkstyleMain`
* On Windows:
  `.\gradlew.bat checkstyleMain`

In case there is a need to avoid checks in some file - it should be added as a separate rule to in
suppressions.xml configuration file.


### Local IDE Checkstyle setup(Intellij IDEA):

* Go to Preferences -> Editor -> Code Style.
* Click a cog on the right side from the Scheme
* Load checkstyle.xml into Import Scheme -> Checkstyle Configuration 