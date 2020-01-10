package org.ccctc.collegeadaptor

import org.apache.http.message.BasicNameValuePair
import org.ccctc.collegeadaptor.exceptions.CollegeAdaptorException
import org.ccctc.collegeadaptor.model.CAEnrollment
import org.ccctc.collegeadaptor.model.CAPerson
import org.ccctc.collegeadaptor.model.CASection
import org.ccctc.collegeadaptor.model.CAStudent
import org.ccctc.collegeadaptor.model.CATerm
import org.springframework.core.ParameterizedTypeReference
import spock.lang.Specification

/**
 * This tests the College Adaptor Client Service integration against the QA College Adaptor Router for the Mock College
 * Adaptor (001). If the QA service-router or 001 mock adaptor are down, these tests will fail - so they are dependent
 * on those services being up.
 *
 * Previously this code was optional and not part of the unit tests, however, due to some errors popping up over lack
 * of testing, I made the choice to include this as part of the required unit tests.
 */
class CollegeAdaptorServiceIntegrationSpec extends Specification {

    static CollegeAdaptorRestTemplateConfig config
    static CollegeAdaptorRestClient client
    static CollegeAdaptorService service
    static String misCode
    static MockUtils mockUtils

    def setupSpec() {
        // Configure REST Template
        String caClientId = "gateway-client-tester"
        String caClientSecret = System.getProperty("adaptor.clientSecret")
        String caAccessTokenUri = "https://service-router.qa.ccctechcenter.org/token/v1/token"
        String caGrantType = "client_credentials"
        boolean usePoolingClient = true
        int maxPerRoute = 100
        int maxTotal = 100

        config = new CollegeAdaptorRestTemplateConfig(caClientId, caClientSecret, caAccessTokenUri, caGrantType,
                        usePoolingClient, maxPerRoute, maxTotal)

        // Configure Client
        String collegeAdaptorRouterProtocol = "https"
        String collegeAdaptorRouterHost = "service-router.qa.ccctechcenter.org"
        String collegeAdaptorRouterPort = "443"
        misCode = "001"

        client = new CollegeAdaptorRestClient(config.collegeAdaptorRestTemplate(), collegeAdaptorRouterProtocol,
                        collegeAdaptorRouterHost, collegeAdaptorRouterPort)

        // Configure the College Adaptor Service
        service = new CollegeAdaptorService(client)

        // Configure mock utils
        mockUtils = new MockUtils(client, service)
    }

    def "test getSectionsByTermFlexSearch"() {
        setup:
        def sections = mockUtils.get(misCode, "mock/sections", [new BasicNameValuePair("misCode", misCode)], new ParameterizedTypeReference<List<CASection>>() {})

        // by instructor
        when:
        def section = sections.find { i -> i.instructors }
        def result = service.getSectionsByTermFlexSearch(misCode, section.sisTermId, [section.instructors[0].lastName.toLowerCase()])

        then:
        result != null
        result.size() > 0
    }

    def "getEnrollmentsByStudent - by student, term, section"() {
        setup:
        def enrollments = mockUtils.get(misCode, "mock/enrollments", [new BasicNameValuePair("misCode", misCode)], new ParameterizedTypeReference<List<CAEnrollment>>() {})
            .findAll { i -> i.cccid != null}

        when:
        def result = service.getEnrollmentsByStudent(misCode, enrollments[0].cccid, enrollments[0].sisTermId, enrollments[0].sisSectionId)

        then:
        result != null
        result.size() == 1
    }

    def "getStudentPerson"() {
        setup:
        def id = "getStudentPerson-test"
        def cccid = "getStudentPerson-test-cccid"
        def loginId = "getStudentPerson-test"
        def loginSuffix = "test.edu"
        def term = new CATerm(misCode: misCode, sisTermId: id, start: new Date(), end: new Date()+1)
        def person = new CAPerson(misCode: misCode, sisPersonId: id, loginId: loginId, loginSuffix: loginSuffix, cccid: cccid)
        def student = new CAStudent(sisTermId: id, sisPersonId: id)

        def cleanup = {
            // delete test data
            try { mockUtils.deleteStudent(misCode, id, id) } catch (CollegeAdaptorException ignored) {}
            try { mockUtils.deleteTerm(misCode, id) } catch (CollegeAdaptorException ignored) {}
            try { mockUtils.deletePerson(misCode, id) } catch (CollegeAdaptorException ignored) {}
        }

        cleanup()

        mockUtils.addTerm(misCode, term)
        mockUtils.addPerson(misCode, person)
        mockUtils.addStudent(misCode, student)

        // by eppn
        when:
        def result = service.getStudentPerson(misCode, null, null, "$loginId@$loginSuffix")

        then:
        result.toString() == person.toString()

        // by ccc id
        when:
        result = service.getStudentPerson(misCode, null, cccid, null)

        then:
        result.toString() == person.toString()

        cleanup:
        cleanup()
    }

    def "getInfo"() {
        when:
        def result = service.getInfo(misCode)

        then:
        result != null
        result.containsKey("build")
        result.containsKey("git")
        result.containsKey("image")
    }

    def "postStudentCCCId and getStudentCCCIds"() {
        setup:
        def id = "postStudentCCCId-test"
        def cccId = "postStudentCCCId-test-cccid"
        def term = new CATerm(misCode: misCode, sisTermId: id, start: new Date(), end: new Date()+1)
        def person = new CAPerson(misCode: misCode, sisPersonId: id)
        def student = new CAStudent(sisTermId: id, sisPersonId: id)

        def cleanup = {
            // delete test data
            try { mockUtils.deleteStudent(misCode, id, id) } catch (CollegeAdaptorException ignored) {}
            try { mockUtils.deleteTerm(misCode, id) } catch (CollegeAdaptorException ignored) {}
            try { mockUtils.deletePerson(misCode, id) } catch (CollegeAdaptorException ignored) {}
        }

        cleanup()

        mockUtils.addTerm(misCode, term)
        mockUtils.addPerson(misCode, person)
        mockUtils.addStudent(misCode, student)

        when:
        def result = service.postStudentCCCId(misCode, id, cccId)
        def result2 = service.getStudentCCCIds(misCode, id)

        then:
        result[0] == cccId
        result2[0] == cccId

        cleanup:
        cleanup()
    }
}
