package org.ccctc.collegeadaptor

import com.fasterxml.jackson.databind.ObjectMapper
import org.ccctc.collegeadaptor.exceptions.CollegeAdaptorException
import org.ccctc.collegeadaptor.model.CABOGWaiver
import org.ccctc.collegeadaptor.model.CACohortTypeEnum
import org.ccctc.collegeadaptor.model.CACourse
import org.ccctc.collegeadaptor.model.CACourseExchangeEnrollment
import org.ccctc.collegeadaptor.model.CAEnrollment
import org.ccctc.collegeadaptor.model.CAFinancialAidUnits
import org.ccctc.collegeadaptor.model.CAPerson
import org.ccctc.collegeadaptor.model.CASection
import org.ccctc.collegeadaptor.model.CAStudent
import org.ccctc.collegeadaptor.model.CAStudentHomeCollege
import org.ccctc.collegeadaptor.model.CATerm
import org.ccctc.collegeadaptor.model.CAEnrollmentStatus
import org.ccctc.collegeadaptor.model.CAPrerequisiteStatus
import org.ccctc.collegeadaptor.model.CAPrerequisiteStatusEnum
import org.ccctc.collegeadaptor.model.CASectionStatus
import org.ccctc.collegeadaptor.model.placement.CAPlacement
import org.ccctc.collegeadaptor.model.placement.CAPlacementSubjectArea
import org.ccctc.collegeadaptor.model.placement.CAPlacementTransaction
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification

/**
 * Created by Zeke on 1/13/2017.
 */
class CollegeAdaptorServiceSpec extends Specification {

    def mockOAuth2RestTemplate = Mock(OAuth2RestTemplate)
    def collegeAdaptorRestClientTest = new CollegeAdaptorRestClient(mockOAuth2RestTemplate, "protocol", "host", "port")
    def collegeAdaptorService = new CollegeAdaptorService(collegeAdaptorRestClientTest)

    def "getTermById"() {
        setup:
        def term = new CATerm(misCode: "000", sisTermId: "sisTermId", description: "description")
        def okResponse = new ResponseEntity<CATerm>(term, HttpStatus.OK)
        def errorReponse = new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "500 INTERNAL_SERVER_ERROR",
                '{"code": "code", "message": "message"}'.bytes, null)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getTerm("000", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.misCode == term.misCode
        assert result.sisTermId == term.sisTermId
        assert result.description == term.description

        // Not found
        when:
        result = collegeAdaptorService.getTerm("000", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception - no body
        when:
        collegeAdaptorService.getTerm("000", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_REQUEST) }
        thrown CollegeAdaptorException

        // Exception - code / message body
        when:
        collegeAdaptorService.getTerm("000", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw errorReponse }
        def e = thrown CollegeAdaptorException
        e.code == "code"
        e.message == "message"

        // URIBuilder Exception - overload URL to something bad to get this thrown
        when:
        def oldHost = ReflectionTestUtils.getField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost")
        ReflectionTestUtils.setField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost", "...bad url!!!...")
        collegeAdaptorService.getTerm("000", "sisTermId")
        ReflectionTestUtils.setField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost", oldHost)

        then:
        thrown CollegeAdaptorException
    }

    def "getTerms"() {
        setup:
        def terms = [new CATerm(misCode: "000", sisTermId: "sisTermId", description: "description")]
        def okResponse = new ResponseEntity<List<CATerm>>(terms, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getTerms("000")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0].misCode == terms[0].misCode
        assert result[0].sisTermId == terms[0].sisTermId
        assert result[0].description == terms[0].description

        // Not found
        when:
        result = collegeAdaptorService.getTerms("000")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getTerms("000")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getCourse - by ID"() {
        setup:
        def course = new CACourse(misCode: "000", sisCourseId: "sisCourseId", sisTermId: "sisTermId", title: "title", description: "description")
        def okResponse = new ResponseEntity<CACourse>(course, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getCourse("000", "sisCourseId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.misCode == course.misCode
        assert result.sisCourseId == course.sisCourseId
        assert result.sisTermId == course.sisTermId
        assert result.title == course.title
        assert result.description == course.description

        // Not Found
        when:
        result = collegeAdaptorService.getCourse("000", "sisCourseId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getCourse("000", "sisCourseId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        thrown CollegeAdaptorException
    }

    def "getCourse - by ID and term"() {
        setup:
        def course = new CACourse(misCode: "000", sisCourseId: "sisCourseId", sisTermId: "sisTermId", title: "title", description: "description")
        def okResponse = new ResponseEntity<CACourse>(course, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getCourse("000", "sisCourseId", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.misCode == course.misCode
        assert result.sisCourseId == course.sisCourseId
        assert result.sisTermId == course.sisTermId
        assert result.title == course.title
        assert result.description == course.description

        // Not Found
        when:
        result = collegeAdaptorService.getCourse("000", "sisCourseId", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getCourse("000", "sisCourseId", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)
        }
        thrown CollegeAdaptorException
    }

    def "getSection"() {
        setup:
        def section = new CASection(sisTermId: "sisTermId", sisCourseId: "sisCourseId", sisSectionId: "sisSectionId", status: CASectionStatus.Active)
        def okResponse = new ResponseEntity<CASection>(section, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getSection("000", "sisTermId", "sisSectionId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.sisCourseId == section.sisCourseId
        assert result.sisSectionId == section.sisSectionId
        assert result.sisTermId == section.sisTermId
        assert result.status == section.status

        // Not found
        when:
        result = collegeAdaptorService.getSection("000", "sisTermId", "sisSectionId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getSection("000", "sisTermId", "sisSectionId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getSectionsByTermAndCourse, getSectionsByTerm"() {
        setup:
        def sections = [new CASection(sisTermId: "sisTermId", sisCourseId: "sisCourseId", sisSectionId: "sisSectionId", status: CASectionStatus.Active)]
        def okResponse = new ResponseEntity<List<CASection>>(sections, HttpStatus.OK)
        def result

        // OK response - by term
        when:
        result = collegeAdaptorService.getSectionsByTerm("000", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0].sisCourseId == sections[0].sisCourseId
        assert result[0].sisSectionId == sections[0].sisSectionId
        assert result[0].sisTermId == sections[0].sisTermId
        assert result[0].status == sections[0].status

        // OK response - by course
        when:
        result = collegeAdaptorService.getSectionsByTermAndCourse("000", null, "sisCourseId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0].sisCourseId == sections[0].sisCourseId
        assert result[0].sisSectionId == sections[0].sisSectionId
        assert result[0].sisTermId == sections[0].sisTermId
        assert result[0].status == sections[0].status

        // OK response - by term and course
        when:
        result = collegeAdaptorService.getSectionsByTermAndCourse("000", "sisTermId", "sisCourseId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0].sisCourseId == sections[0].sisCourseId
        assert result[0].sisSectionId == sections[0].sisSectionId
        assert result[0].sisTermId == sections[0].sisTermId
        assert result[0].status == sections[0].status

        // Not found
        when:
        result = collegeAdaptorService.getSectionsByTermAndCourse("000", "sisTermId", "sisCourseId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getSectionsByTermAndCourse("000", "sisTermId", "sisCourseId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getSectionsByTermFlexSearch"() {
        setup:
        def sections = [new CASection(sisTermId: "sisTermId", sisCourseId: "sisCourseId", sisSectionId: "sisSectionId", status: CASectionStatus.Active)]
        def okResponse = new ResponseEntity<List<CASection>>(sections, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getSectionsByTermFlexSearch("000", "sisTermId", ["word"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1

        // Not found
        when:
        result = collegeAdaptorService.getSectionsByTermFlexSearch("000", "sisTermId", ["word"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getSectionsByTermFlexSearch("000", "sisTermId", ["word"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getEnrollmentsBySection"() {
        setup:
        def enrollments = [new CAEnrollment(sisTermId: "sisTermId", sisCourseId: "sisCourseId", cccid: "cccid",
                sisSectionId: "sisSectionId", sisPersonId: "sisPersonId", enrollmentStatus: CAEnrollmentStatus.Enrolled)]
        def okResponse = new ResponseEntity<List<CAEnrollment>>(enrollments, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getEnrollmentsBySection("000", "sisTermId", "sisSectionId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0] == enrollments[0]

        // Not found
        when:
        result = collegeAdaptorService.getEnrollmentsBySection("000", "sisTermId", "sisSectionId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getEnrollmentsBySection("000", "sisTermId", "sisSectionId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getEnrollmentsByStudent"() {
        setup:
        def enrollments = [new CAEnrollment(sisTermId: "sisTermId", sisCourseId: "sisCourseId", cccid: "cccid",
                sisSectionId: "sisSectionId", sisPersonId: "sisPersonId", enrollmentStatus: CAEnrollmentStatus.Enrolled)]
        def okResponse = new ResponseEntity<List<CAEnrollment>>(enrollments, HttpStatus.OK)
        def result

        // OK response - student, term and section
        when:
        result = collegeAdaptorService.getEnrollmentsByStudent("000", "cccid", "sisTermId", "sisSectionId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0] == enrollments[0]

        // OK response - student, term
        when:
        result = collegeAdaptorService.getEnrollmentsByStudent("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0] == enrollments[0]

        // OK response - student
        when:
        result = collegeAdaptorService.getEnrollmentsByStudent("000", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 1
        assert result[0] == enrollments[0]

        // Not found
        when:
        result = collegeAdaptorService.getEnrollmentsByStudent("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getEnrollmentsByStudent("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "addEnrollment"() {
        setup:
        def enrollment = new CAEnrollment(sisTermId: "sisTermId", sisCourseId: "sisCourseId", cccid: "cccid",
                sisSectionId: "sisSectionId", sisPersonId: "sisPersonId", enrollmentStatus: CAEnrollmentStatus.Enrolled)
        def okResponse = new ResponseEntity<CAEnrollment>(enrollment, HttpStatus.OK)

        when:
        def result = collegeAdaptorService.addEnrollment("000", enrollment)

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == enrollment
    }

    def "getPrerequisiteStatus"() {
        setup:
        def prerequisiteStatus = new CAPrerequisiteStatus(message: "message", status: CAPrerequisiteStatusEnum.Complete)
        def okResponse = new ResponseEntity<CAPrerequisiteStatus>(prerequisiteStatus, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getPrerequisiteStatus("000", "sisCourseId", "cccid", new Date(2016, 1, 1))

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == prerequisiteStatus

        // OK response - no start date
        when:
        result = collegeAdaptorService.getPrerequisiteStatus("000", "sisCourseId", "cccid", null)

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == prerequisiteStatus

        // Not found
        when:
        result = collegeAdaptorService.getPrerequisiteStatus("000", "sisCourseId", "cccid", new Date(2016, 1, 1))

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getPrerequisiteStatus("000", "sisCourseId", "cccid", new Date(2016, 1, 1))

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getStudent"() {
        setup:
        def caStudent = new CAStudent(sisPersonId: "sisPersonId", sisTermId: "sisTermId", cccid: "cccid")
        def okResponse = new ResponseEntity<CAStudent>(caStudent, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getStudent("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == caStudent

        // OK response - no term
        when:
        result = collegeAdaptorService.getStudent("000", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == caStudent

        // Not found
        when:
        result = collegeAdaptorService.getStudent("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getStudent("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException

    }

    def "getStudentHomeCollege"() {
        setup:
        def caStudentHomeCollege = new CAStudentHomeCollege(cccid: "cccid", misCode: "misCode")
        def okResponse = new ResponseEntity<CAStudentHomeCollege>(caStudentHomeCollege, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getStudentHomeCollege("000", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == caStudentHomeCollege

        // Not found
        when:
        result = collegeAdaptorService.getStudentHomeCollege("000", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getStudentHomeCollege("000", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getPerson"() {
        setup:
        def caPerson = new CAPerson(sisPersonId: "sisPersonId", lastName: "lastName")
        def okResponse = new ResponseEntity<CAPerson>(caPerson, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getPerson("000", "sisPersonId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == caPerson

        // Not found
        when:
        result = collegeAdaptorService.getPerson("000", "sisPersonId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getPerson("000", "sisPersonId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getStudentPerson"() {
        setup:
        def caPerson = new CAPerson(sisPersonId: "sisPersonId", lastName: "lastName",loginId: "loginId",loginSuffix: "loginSuffix", cccid: "cccid")
        def okResponse = new ResponseEntity<CAPerson>(caPerson, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getStudentPerson("000", "sisPersonId","cccid","eppn")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == caPerson

        // Not found
        when:
        result = collegeAdaptorService.getStudentPerson("000", "sisPersonId","cccid","eppn")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Exception
        when:
        collegeAdaptorService.getStudentPerson("000", "sisPersonId","cccid","eppn")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getPersonsByCccIds"() {
        setup:
        def caPersons = [new CAPerson(sisPersonId: "sisPersonId1", lastName: "lastName1"),
                         new CAPerson(sisPersonId: "sisPersonId2", lastName: "lastName2")]
        def okResponse = new ResponseEntity<List<CAPerson>>(caPersons, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getPersonsByCccIds("000", ["cccid1","cccid2"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 2
        assert result[0] == caPersons[0]
        assert result[1] == caPersons[1]

        // Not found
        when:
        result = collegeAdaptorService.getPersonsByCccIds("000", ["cccid1","cccid2"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getPersonsByCccIds("000", ["cccid1","cccid2"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "getPersonsBySisPersonIds"() {
        setup:
        def caPersons = [new CAPerson(sisPersonId: "sisPersonId1", lastName: "lastName1"),
                         new CAPerson(sisPersonId: "sisPersonId2", lastName: "lastName2")]
        def okResponse = new ResponseEntity<List<CAPerson>>(caPersons, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.getPersonsBySisPersonIds("000", ["sisPersonId1","sisPersonId2"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.size() == 2
        assert result[0] == caPersons[0]
        assert result[1] == caPersons[1]

        // Not found
        when:
        result = collegeAdaptorService.getPersonsBySisPersonIds("000", ["sisPersonId1","sisPersonId2"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getPersonsBySisPersonIds("000", ["sisPersonId1","sisPersonId2"])

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException
    }

    def "addStudentCohort"() {
        // OK result
        when:
        collegeAdaptorService.addStudentCohort("000", "cccid", CACohortTypeEnum.COURSE_EXCHANGE, "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { return new ResponseEntity(HttpStatus.OK) }

        // Bad Request
        when:
        collegeAdaptorService.addStudentCohort("000", "cccid", CACohortTypeEnum.COURSE_EXCHANGE, "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_REQUEST) }
        thrown CollegeAdaptorException

        // URIBuilder Exception - overload URL to something bad to get this thrown
        when:
        def oldHost = ReflectionTestUtils.getField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost")
        ReflectionTestUtils.setField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost", "...bad url!!!...")
        collegeAdaptorService.addStudentCohort("000", "cccid", CACohortTypeEnum.COURSE_EXCHANGE, "sisTermId")
        ReflectionTestUtils.setField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost", oldHost)

        then:
        thrown CollegeAdaptorException
    }

    def "deleteStudentCohort"() {
        // OK result
        when:
        collegeAdaptorService.deleteStudentCohort("000", "cccid", CACohortTypeEnum.COURSE_EXCHANGE, "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { return new ResponseEntity(HttpStatus.NO_CONTENT) }

        // Bad Request
        when:
        collegeAdaptorService.deleteStudentCohort("000", "cccid", CACohortTypeEnum.COURSE_EXCHANGE, "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_REQUEST) }
        thrown CollegeAdaptorException

        // URIBuilder Exception - overload URL to something bad to get this thrown
        when:
        def oldHost = ReflectionTestUtils.getField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost")
        ReflectionTestUtils.setField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost", "...bad url!!!...")
        collegeAdaptorService.deleteStudentCohort("000", "cccid", CACohortTypeEnum.COURSE_EXCHANGE, "sisTermId")
        ReflectionTestUtils.setField(collegeAdaptorRestClientTest, "collegeAdaptorRouterHost", oldHost)

        then:
        thrown CollegeAdaptorException
    }

    def "getBOGWaiver"() {
        setup:
        def okResponse = new ResponseEntity<CABOGWaiver>(new CABOGWaiver(cccid: "cccid", sisTermId: "sisTermId"), HttpStatus.OK)

        // OK result
        when:
        def result = collegeAdaptorService.getBOGWaiver("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.cccid == "cccid"
        assert result.sisTermId == "sisTermId"

        // Not found
        when:
        result = collegeAdaptorService.getBOGWaiver("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result == null

        // Bad Request
        when:
        collegeAdaptorService.getBOGWaiver("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_REQUEST) }
        thrown CollegeAdaptorException

    }

    def "addBOGWavier"() {
        setup:
        def waiver = new CABOGWaiver(cccid: "cccid", sisTermId: "sisTermId")
        def okResponse = new ResponseEntity<CABOGWaiver>(waiver, HttpStatus.CREATED)

        // Created result
        when:
        def result = collegeAdaptorService.addBOGWavier("000", waiver)

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result.cccid == "cccid"
        assert result.sisTermId == "sisTermId"
    }

    def "getFinancialAidUnits"() {
        setup:
        def faUnits = new CAFinancialAidUnits(cccid: "cccid", sisTermId: "sisTermId",
                ceEnrollment: new CACourseExchangeEnrollment(misCode: "001", collegeName: "college", units: 3, c_id: "COURSE 001",
                        section: new CASection()))
        def okResponse = new ResponseEntity<List<CAFinancialAidUnits>>([faUnits], HttpStatus.OK)

        // OK result
        when:
        def result = collegeAdaptorService.getFinancialAidUnits("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result[0] == faUnits

        // Not found
        when:
        result = collegeAdaptorService.getFinancialAidUnits("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Bad request
        when:
        collegeAdaptorService.getFinancialAidUnits("000", "cccid", "sisTermId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_REQUEST) }
        thrown CollegeAdaptorException
    }

    def "addFinancialAidUnits"() {
        setup:
        def faUnits = new CAFinancialAidUnits(cccid: "cccid", sisTermId: "sisTermId",
                ceEnrollment: new CACourseExchangeEnrollment(misCode: "001", collegeName: "college", units: 3, c_id: "COURSE 001",
                        section: new CASection()))
        def okResponse = new ResponseEntity<CAFinancialAidUnits>(faUnits, HttpStatus.CREATED)

        // Created result
        when:
        def result = collegeAdaptorService.addFinancialAidUnits("000", faUnits)

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == faUnits
    }

    def "deleteFinancialAidUnits"() {
        setup:
        def faUnits = new CAFinancialAidUnits(cccid: "cccid", sisTermId: "sisTermId",
                ceEnrollment: new CACourseExchangeEnrollment(misCode: "001", collegeName: "college", units: 3, c_id: "COURSE 001",
                        section: new CASection()))
        def okResponse = new ResponseEntity(HttpStatus.NO_CONTENT)

        // Ok response
        when:
        collegeAdaptorService.deleteFinancialAidUnits("000", "cccid", "sisTermId", "COURSE 001", "001")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
    }

    def "addPlacementTransaction"() {
        setup:
        def placement = new CAPlacementTransaction(cccid: "cccid", misCode: "000")

        // Created result
        when:
        collegeAdaptorService.addPlacementTransaction("000", placement)

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { new ResponseEntity(HttpStatus.NO_CONTENT) }
        0 * _
    }

    def "addPlacementTransaction - isAssigned"() {

        // there was a bug prior to 3.1.12 that renamed isAssigned to "assigned" based on the getters/setters that
        // IntelliJ created which removed "is" - this makes sure that doesn't happen again / the code doesn't regress
        // if someone recreates those

        setup:
        def placement = new CAPlacement(isAssigned: true)
        def subjectArea = new CAPlacementSubjectArea(placements: [placement])
        def transaction = new CAPlacementTransaction(cccid: "cccid", misCode: "000", subjectArea: subjectArea)
        def mapper = new ObjectMapper()

        when:
        def result = mapper.writeValueAsString(transaction)

        then:
        result.contains("\"isAssigned\":true")
    }

    def "getStudentCCCIds"() {
        setup:
        def cccIds = ["cccId"]
        def okResponse = new ResponseEntity<List<String>>(cccIds, HttpStatus.OK)
        def result

       // OK response
        when:
        result = collegeAdaptorService.getStudentCCCIds("000", "sisPersonId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == cccIds

        // Not found
        when:
        result = collegeAdaptorService.getStudentCCCIds("000", "sisPersonId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size() == 0

        // Exception
        when:
        collegeAdaptorService.getStudentCCCIds("000", "sisPersonId")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException

    }

    def "postStudentCCCId"() {
        setup:
        def cccIds = ["cccId"]
        def okResponse = new ResponseEntity<List<String>>(cccIds, HttpStatus.OK)
        def result

        // OK response
        when:
        result = collegeAdaptorService.postStudentCCCId("000", "sisPersonId", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> okResponse
        assert result == cccIds

        // Not found
        when:
        result = collegeAdaptorService.postStudentCCCId("000", "sisPersonId", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND) }
        assert result.size()== 0

        // Exception
        when:
        collegeAdaptorService.postStudentCCCId("000", "sisPersonId", "cccid")

        then:
        1 * mockOAuth2RestTemplate.exchange(_, _) >> { throw new HttpClientErrorException(HttpStatus.BAD_GATEWAY) }
        thrown CollegeAdaptorException

    }

}