# The Bring, svydovets team
The Bring is the [Inversion of Control (IoC) framework](https://en.wikipedia.org/wiki/Inversion_of_control#:~:text=In%20software%20engineering%2C%20inversion%20of,control%20from%20a%20generic%20framework.).  
The Bring takes care of [Dependency Injection (DI)](https://en.wikipedia.org/wiki/Dependency_injection) into the Bean and manages full Bean lifecycle.

#### NOTE: THE BRING FRAMEWORK IS WRITTEN ON JAVA 17 

<details>
<summary> Description for annotations and ApplicationContext: </summary> 

<details>
<summary> @Configuration: </summary> 

**[@Configuration](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Configuration.java)**
> This annotation is a marker that applicable to class and by this annotation the Bring is looking for beans configurations.
</details>
<details>
<summary> @Bean: </summary> 

**[@Bean](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Bean.java)**
> This annotation is usually applicable to POJO that cannot be marked as **[@Component](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Component.java)**
> and in cases when we need to provide custom initialization for the POJO object.   
> **NOTE: CLASS HAVE TO CONTAIN EMPTY CONSTRUCTOR**
>
>> ### Example:
>> **Imagine that you have external library, and you want to pass to the Bring all the control of lifecycle of this POJO:**
>>> Step 1 -> Create the configuration class (class that is marked as **[@Configuration](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Configuration.java)**)  
>>> ![bean_config](https://user-images.githubusercontent.com/55089853/180369044-7af2ffe2-fc35-4c95-a625-909702b92db6.png)
>>
>>> Step 2 -> Create the method that returns required POJO and mark this method as ***@Bean***  
>>> ![name_without_name](https://user-images.githubusercontent.com/55089853/180370309-98a8e083-d631-425f-beaf-900a9a34f767.png)  
>>> If you need to pass some unique name -> ***@Bean("bean_name")***  

>>
>>> Full class  
>>> ![bean_full](https://user-images.githubusercontent.com/55089853/180369802-cb99a79d-5a9f-414e-8d58-ab30d57fc246.png)
</details>
<details>
<summary> @Component: </summary> 

**[@Component](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Component.java)**
> This annotation is applicable to class. The Bring will create a bean and will manage all lifecycle of this bean.  
> **NOTE: CLASS HAVE TO CONTAIN EMPTY CONSTRUCTOR**
>
>> ### Example:
>> Annotate class as **@Component**, in case presented on the picture below, the name for the bean will be
>> *"com.bobocode.svydovets.beans.scanner.quoter.books.HarryPotterQuoter"*  
>> ![component](https://user-images.githubusercontent.com/55089853/180456037-c38d2cc1-c59c-4c87-b028-122dc388b407.png)
>>
>> You can pass your custom name for bean. This is helpful in cases when tou have beans that implement one interface,
>> this works in conjunction with **[@Inject](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Inject.java)**
>> annotation.
>> In the case on the picture below the bean name will be *"hp""*
>> ![component_name](https://user-images.githubusercontent.com/55089853/180457950-8b18416e-0f73-4fd4-af2c-b42d978e1ef9.png)
</details>
<details>
<summary> @Inject: </summary> 

**[@Inject](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/Inject.java)**
> This annotation makes [Dependency Injection (DI)](https://en.wikipedia.org/wiki/Dependency_injection) into bean.   
> This annotation applicable in classes that marked as *@Component* or if we create a bean (via *@Bean* annotation) for
> the class where we use it. You can mark by this annotation a field that is a bean or an interface that have realization
> that is marked as *@Component* or *@Bean* and the Bring-Bean-Container contains it, then the
> Bring will inject it. If Bring-Bean-Container doesn't have it -> the
> [NoSuchBeanDefinitionException](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/exception/NoSuchBeanDefinitionException.java)
> will be thrown. If an interface contains more than one realization, then the
> [NoUniqueBeanDefinitionException](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/exception/NoUniqueBeanDefinitionException.java)
> will be thrown.
>> ### Example:
>> ![quoter](https://user-images.githubusercontent.com/55089853/180592649-d162226e-1871-458a-a448-cda86ac68a6a.png)
>> ![quoter_r_1](https://user-images.githubusercontent.com/55089853/180594022-98e33b75-28ec-43fe-8df2-4d3abd7ed2af.png)
>> ![quoter_r_2](https://user-images.githubusercontent.com/55089853/180594024-98219402-ce96-4d1c-b8ae-f249abbe6ab2.png)
>> ### Example:
>> ![hp_injected_quoterpng](https://user-images.githubusercontent.com/55089853/180594031-483426c6-149c-47ee-a2df-051822a86fba.png)
</details>

<details>
<summary> @PostConstruct: </summary> 

**[@PostConstruct](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/annotation/PostConstruct.java)**
> This annotation is a bean lifecycle callback applied to the method that performs additional initialization
>> ### Example:
>>![image](https://user-images.githubusercontent.com/12940663/183023353-a9c8c638-b335-451d-be4c-01c6914df4a9.png)
</details>
<br>
<details>
<summary> ApplicationContext: </summary>

## Description for the [ApplicationContext](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/context/ApplicationContext.java):
> *The *ApplicationContext* is the main interface in the Bring. It has realizations for some configurations of bean
> lifecycle regulations and control. Nowadays, the Bring contains only AnnotationConfigurationApplicationContext but
> we are on the way of evolving and will add other ConfigurationApplicationContext (for instance XmlConfigurationApplicationContext).*
>
> Also, the ApplicationContext provides the methods that provide required beans:
> - *getBean(Class<T> beanType)* - provides bean by the bean type
> - *getBean(String beanName)* - provides bean by the bean name
> - *getBean(String beanName, Class<T> beanType)* - provides bean by the bean name and by the bean type
> - *getBeans(Class<?> beanType)* - provides beans by the bean type
>
</details>
<details>
<summary> BeanPostProcessor: </summary> 

**[BeanPostProcessor](https://github.com/maingroon/svydovets-bring/blob/master/src/main/java/com/bobocode/svydovets/beans/bpp/BeanPostProcessor.java)**
> This interface defines callback methods that you can implement to provide you own bean instantiation logic to customize beans someway, etc. PostConstruct methods will be applied to all beans during creation.  You can define one or more postprocessors they will work sequentially.
>> ### Example:
>>![image](https://user-images.githubusercontent.com/12940663/183030773-699d0bb9-c2bc-4c83-a651-589588a3a7bd.png)
</details>
</details>
<br>
<details>
<summary> How to add dependency to Gradle project: </summary>

1. Add maven repository to your build.gradle. You need your github username and github packages read token.
```
repositories {
  mavenCentral()
  maven {
    url = uri("https://maven.pkg.github.com/maingroon/svydovets-bring")
      credentials {
        username = 'your_github_email'
        password = 'bring_token'
      }
   }
}
```
2. Replace *your_github_emailt* on your email from the GitHub account
3. [Contact Bring team and ask a token for downloading dependency](https://github.com/khshanovskyi/get_token/blob/main/README.md)
4. Replace *bring_token* on provided from Bring team
5. Add dependency:
```
implementation 'com.svydovets:svydovets-bring-framework:0.0.1-SNAPSHOT'
```
</details>

<details>
<summary> How to add dependency to maven project: </summary>

1. Create in your .m2 (Windows example of this folder C:\Users\username.m2) folder setting.xml file. If the file is
   already exists go to point 2.
2. Add to the file:
```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
<activeProfiles>
    <activeProfile>github</activeProfile>
</activeProfiles>
<profiles>
    <profile>
        <id>github</id>
        <repositories>
            <repository>
                <id>central</id>
                <url>https://repo1.maven.org/maven2</url>
            </repository>
            <repository>
                <id>github</id>
                <url>https://maven.pkg.github.com/maingroon/svydovets-bring</url>
                <snapshots>
                    <enabled>true</enabled>
                </snapshots>
            </repository>
        </repositories>
    </profile>
</profiles>
<servers>
    <server>
        <id>github</id>
        <username>your_github_email</username>
        <password>bring_token</password>
    </server>
</servers>
</settings>             
```
3. Replace *your_github_email* in this file on your email from the GitHub account
4. [Contact Bring team and ask a token for downloading dependency](https://github.com/khshanovskyi/get_token/blob/main/README.md)
5. Replace *bring_token* on provided from Bring team
6. Add dependency:
```
<dependency>
     <groupId>com.svydovets</groupId>
     <artifactId>svydovets-bring-framework</artifactId>
     <version>0.0.1-SNAPSHOT</version>
</dependency>
```
</details>

[Here you can clone preconfigured maven Web project](https://github.com/khshanovskyi/preconfiguredMavenPtojectForWebApplication)

[Here you can clone preconfigured maven project with 'main' method as start point for application](https://github.com/khshanovskyi/preconfiguredMavenProjectWitnMainStartPoint)

[Here you can an example of using Bring framework](https://github.com/khshanovskyi/bring-nasa-pictures)
