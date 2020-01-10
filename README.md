```
Copyright (c) 2019 California Community Colleges Technology Center
Licensed under the MIT license.
A copy of this license may be found at https://opensource.org/licenses/mit-license.php
```

# README #

## Overview

The college-adaptor-client provides:

- A College Adaptor Service to interface with college adaptors by MIS code.
- A set of model objects for interacting with college adaptor data.
- A College Adaptor REST Client to directly interact with the college adaptors.

_Note: It is highly recommended that you use the College Adaptor Service instead of the REST client as it simplifies the process of interacting with the College Adaptor and does not require writing REST calls._

## How To Compile

Obtain the client secret for the gateway-client-tester client from a keycloak administrator, substituting it for "SECRET_VALUE" below
Open a terminal at the root of the project and run:
```
mvn -Dadaptor.clientSecret=SECRET_VALUE clean package
```

## Github Package Registry ##

The github package registry associated with this repository is used to store the published maven package for use as a dependency by other projects

### Publishing a new version of the college-adaptor-client:

Jenkins has been integrated with this project to perform build, test, and publish actions.

Prerequisites: https://help.github.com/en/github/managing-packages-with-github-package-registry/configuring-apache-maven-for-use-with-github-package-registry

When a push to master occurs, _and_ the following criteria are met, a new version is published:
- the version in the pom has been incremented (cannot match any prior published version)
- the version is not a snapshot version

Alternatively (and assuming Prerequisites have been met locally as well), a package can be published by executing the following command:

    mvn -Dadaptor.clientSecret=SECRET_VALUE deploy

Troubleshooting:
```Return code is: 422, ReasonPhrase: Unprocessable Entity. -> [Help 1]```
This occurs if you're trying to publish a version which has already been published. Bump the version and repeat prior publish steps


## Adding to your Maven project


Currently available package versions:
https://github.com/ccctechcenter/colleague-dmi-client/packages/41836/versions

To include this package as a dependency in another project, add the following to that project's pom.xml

```xml
    <dependencies>
        <dependency>
            <groupId>org.ccctechcenter</groupId>
            <artifactId>college-adaptor-client</artifactId>
            <version>3.1.12</version> <!-- latest version at time of this writing -->
        </dependency>
    </dependencies>

    <repositories>
        <repository>
            <id>github</id>
            <name>GitHub CCCTechcenter Apache Maven Packages</name>
            <url>https://maven.pkg.github.com/ccctechcenter/college-adaptor-client</url>
            <snapshots><enabled>false</enabled></snapshots> <!-- github does not support snapshots -->
            <releases><enabled>true</enabled></releases>
        </repository>
    </repositories>
```

## Adding to a Spring Boot project (1.4+)

The college-adaptor-client is meant to seamlessly integrate with Spring Boot with very little configuration. To add to your project you only need to add it to your Component Scan like so:

```java
@ComponentScan(basePackages = {"...","org.ccctc.collegeadaptor"})
```

You will also need to add the following parameters to your application.properties (note these are sample values for the QA router):

```java
# College Adaptor Router Configuration
collegeadaptor.router.protocol=https
collegeadaptor.router.host=course-exchange-router-qa.ccctechcenter.org
collegeadaptor.router.port=443

# OAuth2 Configuration
college_adaptor.security.oauth2.client.client-id=ENC(... make sure to encrypt your client id and secret ...)
college_adaptor.security.oauth2.client.client-secret=ENC(... make sure to encrypt your client id and secret ...)
college_adaptor.security.oauth2.client.access-token-uri=${collegeadaptor.router.protocol}://${collegeadaptor.router.host}:${collegeadaptor.router.port}/token/v1/token
college_adaptor.security.oauth2.client.grant-type=client_credentials

# Pooling HTTP Client
# Note: if these values are not specified they will default to the values below (true, 100, 100)
college_adaptor.pooling.client=true
college_adaptor.pooling.client.max.per.route=100
college_adaptor.pooling.client.max.total=100
```

This will add three beans to your project:

- `OAuth2RestTemplate collegeAdaptorRestTemplate` (Bean): Configured to handle OAuth2 authentication with the college adaptor router. Configured to use Jackson for HAL compatible JSON. 
- `CollegeAdaptorRestClient` (Component): Standardizes the process of creating URLs and making REST calls to the college adaptor router. 
- `CollegeAdaptorService` (Service): Service that handles college adaptor operations without the need for directly writing REST calls. Relies on the previous two beans.  

## Adding to a Spring Boot project (1.3)

The @ComponentScan(basePackages = {"...","org.ccctc.collegeadaptor"}) may not work in Spring Boot 1.3 or prior. Instead you will need to create your own beans as in the example below. 

```java
@Configuration
public class CollegeAdaptorClientConfig {
    // Example config values
    String caClientId = "client-id";
    String caClientSecret = "client-secret";
    String caAccessTokenUri = "access-token-rul";
    String caGrantType = "client_credentials";
    String collegeAdaptorRouterProtocol = "https";
    String collegeAdaptorRouterHost = "host";
    String collegeAdaptorRouterPort = "443";
    boolean usePoolingClient = true;
    int maxPerRoute = 100;
    int maxTotal = 100;
    
    // Create all three beans
    @Bean
    public CollegeAdaptorRestTemplateConfig collegeAdaptorRestTemplateConfig() {
        return new CollegeAdaptorRestTemplateConfig(caClientId, caClientSecret, caAccessTokenUri, caGrantType,
            usePoolingClient, maxPerRoute, maxTotal);
    }
    
    @Bean
    public CollegeAdaptorRestClient collegeAdaptorRestClient() {
        return new CollegeAdaptorRestClient(collegeAdaptorRestTemplateConfig().collegeAdaptorRestTemplate(),
                collegeAdaptorRouterProtocol, collegeAdaptorRouterHost, collegeAdaptorRouterPort);
    }
    
    @Bean
    public CollegeAdaptorService collegeAdaptorService() {
        return new CollegeAdaptorService(collegeAdaptorRestClient());
    }
    
    // Alternatively, you could just create a single bean for the Service:
    @Bean
    public CollegeAdaptorService collegeAdaptorServiceOnly() {
        CollegeAdaptorRestTemplateConfig config =
                new CollegeAdaptorRestTemplateConfig(caClientId, caClientSecret, caAccessTokenUri, caGrantType,
                    usePoolingClient, maxPerRoute, maxTotal);
        
        CollegeAdaptorRestClient client = 
            new CollegeAdaptorRestClient(config.collegeAdaptorRestTemplate(), collegeAdaptorRouterProtocol, 
                                         collegeAdaptorRouterHost, collegeAdaptorRouterPort);
        
        return new CollegeAdaptorService(client);
    }
}
```
## Manually Adding to your project

If you don't want to take advantage of the Spring integration in the previous step, you can manually add these items to your project. This might be useful if you are planning on extending or overriding any of the classes and default configuration.

```java
// Configure OAuth2 and pooling client
String caClientId = "client-id";
String caClientSecret = "client-secret";
String caAccessTokenUri = "access-token-rul";
String caGrantType = "client_credentials";
boolean usePoolingClient = true;
int maxPerRoute = 100;
int maxTotal = 100;

CollegeAdaptorRestTemplateConfig config =
        new CollegeAdaptorRestTemplateConfig(caClientId, caClientSecret, caAccessTokenUri, caGrantType,
            usePoolingClient, maxPerRoute, maxTotal);

// Configure the REST Client
String collegeAdaptorRouterProtocol = "https";
String collegeAdaptorRouterHost = "host";
String collegeAdaptorRouterPort = "443";

CollegeAdaptorRestClient client = 
    new CollegeAdaptorRestClient(config.collegeAdaptorRestTemplate(), collegeAdaptorRouterProtocol, 
                                 collegeAdaptorRouterHost, collegeAdaptorRouterPort);

// Configure the College Adaptor Service
CollegeAdaptorService service = new CollegeAdaptorService(client);
```

## Using the College Adaptor Service

The College Adaptor Service makes interacting with the college adaptors simple with methods such as ``getTerm``, ``getSection``, etc.

Tips:

- 404 (not found) responses from the College Adaptor will not result in an exception being thrown. Instead, the method will return either null or an empty list, depending on whether the return value is a single object or an array.
- If  a problem occurs retrieving the data, a ``CollgeAdaptorException`` will be thrown. This exception contains the HTTP Status, a code and a message. The code and message usually come from the College Adaptor itself, indicating what the nature of the problem was. If the exception was not caused by a REST request to a College Adaptor, HTTP Status will not be present.
- The ``code`` value of a ``CollegeAdaptorException`` represents an error or specfic condition that is pre-defined in the College Adaptor application. Therefore if you need to trap for a specific error condition, this would be the value to use. The ``message`` value by contrast is free-form text containing more detail about the error or condition.

Usage example:
```java
@Autowired
private CollegeAdaptorService collegeAdaptorService;

public void example() {
    String misCode = "001";
    String sisTermId = "2016FA"

    // Get the term
    CATerm term = collegeAdaptorService.getTerm(misCode, sisTermId);
}
```

## Using the College Adaptor REST Client

_Note: it is recommended that you use the College Adaptor Service instead._

Usage Example:

```java

@Autowired
private CollegeAdaptorRestClient collegeAdaptorRestClient;

public void example() {
    String misCode = "001"
    String sisTermId = "2016FA"
        
    // Create the request
    RequestEntity request = RequestEntity.get(
        new URI(collegeAdaptorRestClient.applicationUrl(
            misCode, String.format("terms/%s", sisTermId)))).build();

    // Get the term            
    CATerm term = collegeAdaptorRestClient.template().exchange(request, CATerm.class);
}
```

## Disabling the pooled connection manager

By default the REST template uses a PoolingHttpClientConnectionManager. You can disable this by setting a configuration 
value to false in application.properties:

```java
college_adaptor.pooling.client=false
```

## Logging

The College Adaptor Service uses slf4j for logging. The following is logged:

- ``INFO`` - the request is logged with its parameters. Example: `Retrieving student ABC123 for term 2017SP from 001`
- ``DEBUG`` - the REST request and response are logged including the URL and full body of each.
- ``ERROR`` - non-200 responses from the REST client are logged, including the full body of the response, except 404 Not Found responses which are logged at the `DEBUG` level.

## Upgrading from a version prior to 3.0.0-SNAPSHOT

Version 3.0.0-SNAPSHOT made a number of significant changes (see Version History). To upgrade from a previous version you will need to do the following:

- Remove the Bean ``collegeAdaptorRestTemplate`` from your project as this bean is now part of the College Adaptor Client. 
- If you implemented the ``CollegeAdaptorException`` in your project you may remove it as this exception is now part of the college-adaptor-client.
- If you are using ``CaPojoMapper``, you will need to bring that class into your application as it has been removed from the college-adaptor-client. To find the source code you will need to check an earlier commit in BitBucket. At the time of writing this was available here: https://bitbucket.org/cccnext/college-adaptor-client/src/cef44e66f9212b463a504fbdbbaef1f836dd0abc/src/main/java/org/ccctc/collegeadaptor/mapper/CaPojoMapper.java?at=develop&fileviewer=file-view-default
- Remove any references to the classes ``SchoolConnectionInfoException``, ``SchoolConnectionInfo`` and the methods ``CollegeAdaptorRestClient.createSchoolConnectionInfo``, ``CollegeAdaptorRestClient.getQuerySeparator`` as these are obsolete.
- Update references to the class ``PrerequisiteStatus`` to reflect its new name ``CAPrerequisiteStatus`` 
- (optional) Convert your REST calls to use the Service instead. Note that with the service you no longer need to trap 404 responses; instead the service will return null for a method that returns a single object or an empty array for a method than returns an array. All other non-200 responses will return a ``CollegeAdaptorException`` (such as Bad Request, Internal Server Error, etc) so you may need to alter your exception handling to trap this exception.

## Version History

### 3.1.12-SNAPSHOT

- Fixed a bug in CAPlacement that caused jackson to serialize/deserialize "isAssigned" as "assigned"

### 3.1.11-SNAPSHOT

- Fixed bugs in postStudentCCCId and getStudentCCCIds that caused them to fail for all calls

### 3.1.10-SNAPSHOT

- Added the pooling HTTP client as the default HTTP client. It can be turned off and configured as needed for max connections.
- Fixed issues with getStudentPerson that caused it to fail for all calls
- Added getInfo that returns a Map of properties from the /info endpoint

### 3.1.9-SNAPSHOT

- Added optional parameter sisSectionId to getEnrollmentsByStudent

### 3.1.8-SNAPSHOT

- Added new endpoints to get the student cccid and to add the cccid using sisPersonId.

### 3.1.7-SNAPSHOT

- Added new endpoint to get the person record by sisPersonId, cccid, eppn.

### 3.1.6-SNAPSHOT

- Added crosslisting information to CASection.

### 3.1.5-SNAPSHOT

- Replaced placement model with new CCC Assess 2.0 model. The old model (which was unused) has been removed.
- Added addPlacementTransaction method to CollegeAdaptorService

### 3.1.4-SNAPSHOT

- Corrected parameter name issue with flex search that was causing it to not work

### 3.1.3-SNAPSHOT

- Renamed all models that did not have "CA" as a prefix:
    - CourseContact to CACourseContact
    - CourseStatus to CACourseStatus
    - CreditStatus to CACreditStatus
    - EmailType to CAEmailType
    - EnrollmentStatus to CAEnrollmentStatus
    - GradingMethod to CAGradingMethod
    - InstructionalMethod to CAInstructionalMethod
    - Instructor to CAInstructor
    - PrerequisiteStatusEnum to CAPrerequisiteStatusEnum
    - ResidentStatus to CAResidentStatus
    - SectionStatus to CASectionStatus
    - TermSession to CATermSession
    - TermType to CATermType
    - TransferStatus to CATransferStatus
- Added missing element cccid to CAPerson
- Added flex search to CollegeAdaptorService: getSectionsByTermFlexSearch
- Added missing method addEnrollment to CollegeAdaptorService

### 3.1.2-SNAPSHOT

- Fixed a bug in where spaces in the URL caused an exception. They are now properly converted to %20.
- Fixed a bug in deleteFinancialAidUnits - it was not properly passing the enrolledMisCode as a parameter.

### 3.1.1-SNAPSHOT

- Updated CABOGWaiver with four new fields: determinedResidentCA, determinedAB540Eligible, determinedNonResExempt, determinedHomeless
- Updated CAPerson with one new field: loginSuffix

### 3.1.0-SNAPSHOT

- Added new models for Financial Aid information and Cohorts:
    - CABOGWaiver
    - CACohort
    - CACohortTypeEnum
    - CACourseExchangeEnrollment
    - CAFinancialAidUnits
    - CAMaritalStatus
- Added new methods to CollegeAdaptorService to support Financial Aid information and Cohorts:
    - addStudentCohort
    - deleteStudentCohort
    - getBOGWaiver
    - addBOGWavier
    - getFinancialAidUnits
    - addFinancialAidUnits
    - deleteFinancialAidUnits
- Updated ``CAStudent`` model to include new element ``cohorts``
- Added ``CAErrors`` model to include error codes that may be returned in a ``CollegeAdaptorException`` exception. Error codes returned from the college adaptor should be one of those in this class, though this is not strictly enforced.


### 3.0.0-SNAPSHOT

- Added College Adaptor Service
- Added College Adaptor Exception
- Added REST Template configuration (previously this had to be done in any project that used the client)
- Renamed PrerequisiteStatus to CAPrerequisiteStatus
- Removed obsolete school connection information from REST client
- Improved injection capabilities and allowed for non-injected solutions
- Added unit tests
- Removed the POJO mapper
- Updated the POM file
