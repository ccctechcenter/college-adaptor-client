/**
* Copyright (c) 2019 California Community Colleges Technology Center
* Licensed under the MIT license.
* A copy of this license may be found at https://opensource.org/licenses/mit-license.php
**/

package org.ccctc.collegeadaptor;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.ccctc.collegeadaptor.exceptions.CollegeAdaptorException;
import org.ccctc.collegeadaptor.model.*;

import javax.validation.constraints.NotNull;

import org.ccctc.collegeadaptor.model.placement.CAPlacementTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service for performing college adaptor operations
 *
 * @author zrogers
 */
@Service
public class CollegeAdaptorService {

    private static final Logger logger = LoggerFactory.getLogger(CollegeAdaptorService.class);
    private final CollegeAdaptorRestClient collegeAdaptorRestClient;

    @Autowired
    public CollegeAdaptorService(CollegeAdaptorRestClient collegeAdaptorRestClient) {
        this.collegeAdaptorRestClient = collegeAdaptorRestClient;
    }

    /**
     * Perform a request against the College adaptor using RestTemplate.exchange()
     *
     * @param request Request
     * @param typeRef Parameterized Type Reference of return type
     * @param <T>     Return type
     * @return Return value
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    protected <T> T exchange(RequestEntity request, ParameterizedTypeReference<T> typeRef)
            throws CollegeAdaptorException {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Sending request to College Adaptor: URL = {}  Body = {}", request.getUrl(),
                        (request.getBody() != null) ? request.getBody().getClass().getName() : null);

                if (request.getBody() != null)
                    logger.debug("Body of request: \n{}", request.getBody().toString());
            }

            ResponseEntity<T> responseEntity = collegeAdaptorRestClient.template().exchange(request, typeRef);

            if (logger.isDebugEnabled()) {
                logger.debug("Received {} response from College Adaptor, Body = {}", responseEntity.getStatusCode(),
                        (responseEntity.getBody() != null) ? responseEntity.getBody().getClass().getName() : null);

                if (responseEntity.getBody() != null && logger.isDebugEnabled()) {
                    logger.debug("Body of response:\n {}", responseEntity.getBody().toString());
                }
            }

            return responseEntity.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.debug("Received {} response from College Adapter, Body = {}", e.getStatusCode(),
                        e.getResponseBodyAsString());
            } else {
                logger.error("Received {} response from College Adapter, Body = {}", e.getStatusCode(),
                        e.getResponseBodyAsString());
            }

            throw new CollegeAdaptorException(e);
        }
    }

    /**
     * Perform a GET operation against the College Adaptor
     *
     * @param misCode      MIS Code
     * @param relativePath Relative path of request
     * @param params       Parameters
     * @param typeRef      Parameterized Type Reference of return type
     * @param <T>          Return type
     * @return Results
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    protected <T> T get(String misCode, String relativePath, List<NameValuePair> params,
                        ParameterizedTypeReference<T> typeRef) throws CollegeAdaptorException {
        try {
            URIBuilder uriBuilder = collegeAdaptorRestClient.applicationUriBuilder(misCode, relativePath);
            if (params != null) uriBuilder.addParameters(params);
            RequestEntity request = RequestEntity.get(uriBuilder.build()).build();
            return exchange(request, typeRef);
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException exception encountered", e);
            throw new CollegeAdaptorException(String.format("Caught URISyntaxException exception %s", e.getMessage()),
                    null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * Perform a POST operation against the College Adaptor
     *
     * @param misCode      MIS Code
     * @param relativePath Relative path of request
     * @param params       Parameters
     * @param body         Body of the POST operation
     * @param returnType   Parameterized Type Reference of return type
     * @return Results
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    protected <T> T post(String misCode, String relativePath, List<NameValuePair> params, Object body,
                         ParameterizedTypeReference<T> returnType)
            throws CollegeAdaptorException {
        try {
            URIBuilder uriBuilder = collegeAdaptorRestClient.applicationUriBuilder(misCode, relativePath);
            if (params != null) uriBuilder.addParameters(params);
            RequestEntity request = RequestEntity.post(uriBuilder.build()).body(body);
            return exchange(request, returnType);
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException exception encountered", e);
            throw new CollegeAdaptorException(String.format("Caught URISyntaxException exception %s", e.getMessage()),
                    null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * Perform a DELETE operation against the College Adaptor
     *
     * @param misCode      MIS Code
     * @param relativePath Relative path of request
     * @param params       Parameters
     * @param typeRef      Parameterized Type Reference of return type
     * @param <T>          Return type
     * @return Results
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    protected <T> T delete(String misCode, String relativePath, List<NameValuePair> params,
                           ParameterizedTypeReference<T> typeRef) throws CollegeAdaptorException {
        try {
            URIBuilder uriBuilder = collegeAdaptorRestClient.applicationUriBuilder(misCode, relativePath);
            if (params != null) uriBuilder.addParameters(params);
            RequestEntity request = RequestEntity.delete(uriBuilder.build()).build();
            return exchange(request, typeRef);
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException exception encountered", e);
            throw new CollegeAdaptorException(String.format("Caught URISyntaxException exception %s", e.getMessage()),
                    null, HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * Get a term from an SIS
     *
     * @param misCode   MIS Code
     * @param sisTermId SIS Term ID
     * @return Term
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CATerm getTerm(@NotNull String misCode, @NotNull String sisTermId) throws CollegeAdaptorException {
        try {
            logger.info("Retrieving term {} from {}", sisTermId, misCode);
            return get(misCode, String.format("terms/%s", sisTermId), null,
                    new ParameterizedTypeReference<CATerm>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get all terms from an SIS
     *
     * @param misCode MIS Code
     * @return List of terms
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CATerm> getTerms(@NotNull String misCode) throws CollegeAdaptorException {
        try {
            logger.info("Retrieving all terms from {}", misCode);
            List<CATerm> terms = get(misCode, "terms", null,
                    new ParameterizedTypeReference<List<CATerm>>() {
                    });
            if (terms != null) return terms;
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Get a course from an SIS
     *
     * @param misCode     MIS Code
     * @param sisTermId   SIS Term ID
     * @param sisCourseId SIS Course ID
     * @return Course
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CACourse getCourse(@NotNull String misCode, @NotNull String sisCourseId, String sisTermId)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        if (sisTermId != null)
            params.add(new BasicNameValuePair("sisTermId", sisTermId));

        try {
            logger.info("Retrieving course {} for term {} from {}", sisCourseId, sisTermId, misCode);
            return get(misCode, String.format("courses/%s", sisCourseId), params,
                    new ParameterizedTypeReference<CACourse>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get a course from an SIS
     *
     * @param misCode     MIS Code
     * @param sisCourseId SIS Course ID
     * @return Course
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CACourse getCourse(@NotNull String misCode, @NotNull String sisCourseId) throws CollegeAdaptorException {
        return getCourse(misCode, sisCourseId, null);
    }

    /**
     * Get a section from an SIS
     *
     * @param misCode      MIS Code
     * @param sisTermId    SIS Term ID
     * @param sisSectionId SIS Section ID
     * @return Section
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CASection getSection(@NotNull String misCode, @NotNull String sisTermId, @NotNull String sisSectionId)
            throws CollegeAdaptorException {
        try {
            logger.info("Retrieving section {} for term {} from {}", sisSectionId, sisTermId, misCode);
            return get(misCode, String.format("sections/%s?sisTermId=%s", sisSectionId, sisTermId), null,
                    new ParameterizedTypeReference<CASection>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get a list of sections from an SIS by term and/or course.
     *
     * @param misCode     MIS Code
     * @param sisTermId   SIS Term ID
     * @param sisCourseId SIS Course ID
     * @return List of sections
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CASection> getSectionsByTermAndCourse(@NotNull String misCode, String sisTermId, String sisCourseId)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();

        if (sisTermId != null)
            params.add(new BasicNameValuePair("sisTermId", sisTermId));
        if (sisCourseId != null)
            params.add(new BasicNameValuePair("sisCourseId", sisCourseId));

        try {
            logger.info("Retrieving all sections for course {} and term {} from {}", sisCourseId, sisTermId, misCode);
            List<CASection> sections = get(misCode, "sections", params,
                    new ParameterizedTypeReference<List<CASection>>() {
                    });
            if (sections != null) return sections;
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Get sections from an SIS by term.
     *
     * @param misCode   MIS Code
     * @param sisTermId SIS Term ID
     * @return List of sections
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CASection> getSectionsByTerm(@NotNull String misCode, @NotNull String sisTermId)
            throws CollegeAdaptorException {
        return getSectionsByTermAndCourse(misCode, sisTermId, null);
    }

    /**
     * Get sections from an SIS by term with flex search
     *
     * @param misCode     MIS Code
     * @param sisTermId   SIS Term ID
     * @param searchWords Words or phrases to search for
     * @return List of sections
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CASection> getSectionsByTermFlexSearch(@NotNull String misCode, @NotNull String sisTermId,
                                                       @NotNull List<String> searchWords) throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sisTermId", sisTermId));
        for (String word : searchWords)
            params.add(new BasicNameValuePair("words", word));

        try {
            logger.info("Retrieving all sections for term {}, searching on {} from {}", sisTermId, String.join(",", searchWords), misCode);
            List<CASection> sections = get(misCode, "sections/search", params,
                    new ParameterizedTypeReference<List<CASection>>() {
                    });
            if (sections != null) return sections;
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Get a list of enrollments from an SIS by Section
     *
     * @param misCode      MIS Code
     * @param sisTermId    SIS Term ID
     * @param sisSectionId SIS Section ID
     * @return List of Enrollments
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CAEnrollment> getEnrollmentsBySection(@NotNull String misCode, @NotNull String sisTermId,
                                                      @NotNull String sisSectionId)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sisTermId", sisTermId));

        try {
            logger.info("Retrieving enrollments for section {} for term {} from {}", sisSectionId, sisTermId, misCode);
            return get(misCode, String.format("enrollments/section/%s", sisSectionId), params,
                    new ParameterizedTypeReference<List<CAEnrollment>>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Get a list of enrollments from an SIS by Student, term and section
     *
     * @param misCode      MIS Code
     * @param cccId        CCC ID of the student
     * @param sisTermId    SIS Term ID
     * @param sisSectionId SIS Section ID
     * @return List of Enrollments
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CAEnrollment> getEnrollmentsByStudent(@NotNull String misCode, @NotNull String cccId, String sisTermId,
                                                      String sisSectionId)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        if (sisTermId != null) params.add(new BasicNameValuePair("sisTermId", sisTermId));
        if (sisSectionId != null) params.add(new BasicNameValuePair("sisSectionId", sisSectionId));

        try {
            logger.info("Retrieving enrollments for student {} for term {} from {}", cccId, sisTermId, misCode);
            return get(misCode, String.format("enrollments/student/%s", cccId), params,
                    new ParameterizedTypeReference<List<CAEnrollment>>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Get a list of enrollments from an SIS by Student and term
     *
     * @param misCode   MIS Code
     * @param cccId     CCC ID of the student
     * @param sisTermId SIS Term ID
     * @return List of Enrollments
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CAEnrollment> getEnrollmentsByStudent(@NotNull String misCode, @NotNull String cccId, String sisTermId)
            throws CollegeAdaptorException {
        return getEnrollmentsByStudent(misCode, cccId, sisTermId, null);
    }

    /**
     * Get a list of enrollments from an SIS by Student
     *
     * @param misCode MIS Code
     * @param cccId   CCC ID of the student
     * @return List of Enrollments
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CAEnrollment> getEnrollmentsByStudent(@NotNull String misCode, @NotNull String cccId)
            throws CollegeAdaptorException {
        return getEnrollmentsByStudent(misCode, cccId, null, null);
    }

    /**
     * Enroll student in a section
     *
     * @param misCode      MIS Code
     * @param caEnrollment Enrollment
     * @return Enrollment
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAEnrollment addEnrollment(@NotNull String misCode, @NotNull CAEnrollment caEnrollment)
            throws CollegeAdaptorException {
        logger.info("Adding Enrollment to student with CCC ID {} for term {}, section {} from {}", caEnrollment.getCccid(),
                caEnrollment.getSisTermId(), caEnrollment.getSisSectionId(), misCode);
        return post(misCode, "enrollments", null, caEnrollment,
                new ParameterizedTypeReference<CAEnrollment>() {
                });
    }

    /**
     * Get prerequisite status from an SIS for a student and course
     *
     * @param misCode     MIS Code
     * @param sisCourseId SIS Course ID
     * @param cccId       CCC ID
     * @param startDate   Start Date
     * @return Prerequisite Status
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAPrerequisiteStatus getPrerequisiteStatus(@NotNull String misCode, @NotNull String sisCourseId,
                                                      @NotNull String cccId, Date startDate)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cccid", cccId));

        // Pass the date as an epoch date (numeric)
        if (startDate != null)
            params.add(new BasicNameValuePair("startDate", ((Long) startDate.getTime()).toString()));

        try {
            logger.info("Retrieving prerequisite status for student {} for course {}, starting date {} from {}",
                    cccId, sisCourseId, startDate, misCode);
            return get(misCode, String.format("enrollments/prerequisitestatus/%s", sisCourseId), params,
                    new ParameterizedTypeReference<CAPrerequisiteStatus>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get a student from an SIS by CCC ID and Term
     *
     * @param misCode   MIS Code
     * @param cccId     CCC ID of the student
     * @param sisTermId SIS Term ID
     * @return Student
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAStudent getStudent(@NotNull String misCode, @NotNull String cccId, String sisTermId)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        if (sisTermId != null)
            params.add(new BasicNameValuePair("sisTermId", sisTermId));

        try {
            logger.info("Retrieving student {} for term {} from {}", cccId, sisTermId, misCode);
            return get(misCode, String.format("students/%s", cccId), params,
                    new ParameterizedTypeReference<CAStudent>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get a student from an SIS by CCC ID
     *
     * @param misCode MIS Code
     * @param cccId   CCC ID of the student
     * @return Student
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAStudent getStudent(@NotNull String misCode, @NotNull String cccId) throws CollegeAdaptorException {
        return getStudent(misCode, cccId, null);
    }

    /**
     * Get a student's home college information from an SIS by CCC ID
     *
     * @param misCode MIS Code
     * @param cccId   CCC ID of the student
     * @return Student Home College
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAStudentHomeCollege getStudentHomeCollege(@NotNull String misCode, @NotNull String cccId)
            throws CollegeAdaptorException {
        try {
            logger.info("Retrieving home college for student {} from {}", cccId, misCode);
            return get(misCode, String.format("students/homecollege/%s", cccId), null,
                    new ParameterizedTypeReference<CAStudentHomeCollege>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get student CCCIDs from an SIS by SIS Person ID
     *
     * @param misCode     MIS Code
     * @param sisPersonId SIS Person ID
     * @return List of Student CCCIDs
     * @throws CollegeAdaptorException College Adaptor Exception
     */

    public List<String> getStudentCCCIds(@NotNull String misCode, @NotNull String sisPersonId) throws CollegeAdaptorException {
        try {
            logger.info("Retrieving Student CCC ID with SIS Person ID {}  from {}", sisPersonId, misCode);
            return get(misCode, String.format("students/%s/cccids", sisPersonId), null,
                    new ParameterizedTypeReference<List<String>>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Add CCCID to the student by SIS Person ID
     *
     * @param misCode     MIS Code
     * @param cccId       CCC ID
     * @param sisPersonId SIS Person ID
     * @return List of Student CCCIDs
     * @throws CollegeAdaptorException College Adaptor Exception
     */

    public List<String> postStudentCCCId(@NotNull String misCode, @NotNull String sisPersonId, @NotNull String cccId) throws CollegeAdaptorException {
        try {
            logger.info("Update new CCC ID {} for Student {} from {}", cccId, sisPersonId, misCode);
            return post(misCode, String.format("students/%s/cccids/%s", sisPersonId, cccId), null, null,
                    new ParameterizedTypeReference<List<String>>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Add student to a cohort
     *
     * @param misCode          MIS Code
     * @param cccId            CCC ID
     * @param caCohortTypeEnum Cohort
     * @param sisTermId        SIS Term ID
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public void addStudentCohort(@NotNull String misCode, @NotNull String cccId, @NotNull CACohortTypeEnum caCohortTypeEnum,
                                 @NotNull String sisTermId) throws CollegeAdaptorException {
        logger.info("Adding student with CCC ID {} to cohort {} with term {} from {}", cccId, caCohortTypeEnum, sisTermId, misCode);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sisTermId", sisTermId));

        post(misCode, String.format("students/%s/cohorts/%s", cccId, caCohortTypeEnum), params, null,
                new ParameterizedTypeReference<Void>() {
                });
    }

    /**
     * Delete student from a cohort
     *
     * @param misCode          MIS Code
     * @param cccId            CCC ID
     * @param caCohortTypeEnum Cohort
     * @param sisTermId        SIS Term ID
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public void deleteStudentCohort(@NotNull String misCode, @NotNull String cccId, @NotNull CACohortTypeEnum caCohortTypeEnum,
                                    @NotNull String sisTermId) throws CollegeAdaptorException {
        logger.info("Removing student with CCC ID {} from cohort {} with term {} from {}", cccId, caCohortTypeEnum, sisTermId, misCode);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sisTermId", sisTermId));

        delete(misCode, String.format("students/%s/cohorts/%s", cccId, caCohortTypeEnum), params,
                new ParameterizedTypeReference<Void>() {
                });
    }

    /**
     * Get person from an SIS by SIS Person ID
     *
     * @param misCode     MIS Code
     * @param sisPersonId SIS Person ID
     * @return Person
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAPerson getPerson(@NotNull String misCode, @NotNull String sisPersonId) throws CollegeAdaptorException {
        try {
            logger.info("Retrieving person with SIS Person ID {} from {}", sisPersonId, misCode);
            return get(misCode, String.format("persons/%s", sisPersonId), null,
                    new ParameterizedTypeReference<CAPerson>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get Student person from an SIS by SIS Person ID, CCC ID, eppn
     *
     * @param misCode     MIS Code
     * @param sisPersonId SIS Person ID
     * @param cccId       CCC ID
     * @param eppn        eppn
     * @return Person
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAPerson getStudentPerson(@NotNull String misCode, String sisPersonId, String cccId, String eppn) throws CollegeAdaptorException {
        try {
            logger.info("Retrieving Student person with SIS Person ID {} or CCC ID {} or eppn {} from {}", sisPersonId, cccId, eppn, misCode);
            List<NameValuePair> params = new ArrayList<>();
            if (sisPersonId != null) params.add(new BasicNameValuePair("sisPersonId", sisPersonId));
            if (cccId != null) params.add(new BasicNameValuePair("cccid", cccId));
            if (eppn != null) params.add(new BasicNameValuePair("eppn", eppn));

            return get(misCode, "persons/student", params,
                    new ParameterizedTypeReference<CAPerson>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Get list of a Persons from an SIS by a list of SIS Person IDs
     *
     * @param misCode      MIS Code
     * @param sisPersonIds SIS Person IDs
     * @return List of Persons
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CAPerson> getPersonsBySisPersonIds(@NotNull String misCode, @NotNull List<String> sisPersonIds)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        for (String sisPersonId : sisPersonIds) {
            params.add(new BasicNameValuePair("sisPersonIds", sisPersonId));
        }

        try {
            logger.info("Retrieving person(s) with SIS Person IDs {} from {}", String.join(",", sisPersonIds), misCode);
            return get(misCode, "persons", params,
                    new ParameterizedTypeReference<List<CAPerson>>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Get list of a Persons from an SIS by a list of CCC IDs
     *
     * @param misCode MIS Code
     * @param cccIds  CCC IDs
     * @return List of Persons
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CAPerson> getPersonsByCccIds(@NotNull String misCode, @NotNull List<String> cccIds)
            throws CollegeAdaptorException {
        List<NameValuePair> params = new ArrayList<>();
        for (String cccId : cccIds) {
            params.add(new BasicNameValuePair("cccids", cccId));
        }

        try {
            logger.info("Retrieving person(s) with CCC IDs {} from {}", String.join(",", cccIds), misCode);
            return get(misCode, "persons", params,
                    new ParameterizedTypeReference<List<CAPerson>>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Get BOG Waiver information for a student
     *
     * @param misCode   MIS Code
     * @param cccId     CCC IDs
     * @param sisTermId SIS Term ID
     * @return BOG Waiver Information
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CABOGWaiver getBOGWaiver(@NotNull String misCode, @NotNull String cccId, @NotNull String sisTermId)
            throws CollegeAdaptorException {
        try {
            logger.info("Retrieving BOG Waiver information for student with CCC ID {} for term {} from {}", cccId, sisTermId, misCode);
            return get(misCode, String.format("bogfw/%s/%s", cccId, sisTermId), null,
                    new ParameterizedTypeReference<CABOGWaiver>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return null;
    }

    /**
     * Add BOG Waiver information to a student
     *
     * @param misCode     MIS Code
     * @param caBOGWaiver BOG Waiver information
     * @return BOG Waiver information
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CABOGWaiver addBOGWavier(@NotNull String misCode, @NotNull CABOGWaiver caBOGWaiver)
            throws CollegeAdaptorException {
        logger.info("Adding BOG Waiver information to student with CCC ID {} for term {} from {}", caBOGWaiver.getCccid(),
                caBOGWaiver.getSisTermId(), misCode);
        return post(misCode, "bogfw", null, caBOGWaiver,
                new ParameterizedTypeReference<CABOGWaiver>() {
                });
    }

    /**
     * Get Financial Aid Units for a student
     *
     * @param misCode   MIS Code
     * @param cccId     CCC IDs
     * @param sisTermId SIS Term ID
     * @return List of Financial Aid Units
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public List<CAFinancialAidUnits> getFinancialAidUnits(@NotNull String misCode, @NotNull String cccId,
                                                          @NotNull String sisTermId) throws CollegeAdaptorException {
        try {
            logger.info("Retrieving Financial Aid Units for student with CCC ID {} for term {} from {}", cccId, sisTermId, misCode);
            return get(misCode, String.format("faunits/%s/%s", cccId, sisTermId), null,
                    new ParameterizedTypeReference<List<CAFinancialAidUnits>>() {
                    });
        } catch (CollegeAdaptorException e) {
            // Ignore 404s
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
        }

        return new ArrayList<>();
    }

    /**
     * Add Financial Aid Units to a student
     *
     * @param misCode             MIS Code
     * @param caFinancialAidUnits Financial Aid Units
     * @return Financial Aid Units
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public CAFinancialAidUnits addFinancialAidUnits(@NotNull String misCode, @NotNull CAFinancialAidUnits caFinancialAidUnits)
            throws CollegeAdaptorException {
        logger.info("Adding Financial Aid Units to student with CCC ID {} for term {} from {}",
                caFinancialAidUnits.getCccid(), caFinancialAidUnits.getSisTermId(), misCode);

        return post(misCode, "faunits", null, caFinancialAidUnits,
                new ParameterizedTypeReference<CAFinancialAidUnits>() {
                });
    }

    /**
     * Delete Financial Aid Units from a student
     *
     * @param misCode         MIS Code
     * @param cccId           CCC ID
     * @param sisTermId       SIS Term ID
     * @param cId             C-ID of the enrollment
     * @param enrolledMisCode MIS Code associated with the enrollment
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public void deleteFinancialAidUnits(@NotNull String misCode, @NotNull String cccId,
                                        @NotNull String sisTermId, @NotNull String cId,
                                        @NotNull String enrolledMisCode) throws CollegeAdaptorException {
        logger.info("Deleting Financial Aid Units for student with CCC ID {}, term {}, C-ID {}, enrolled MIS {} from {}",
                cccId, sisTermId, cId, enrolledMisCode, misCode);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("enrolledMisCode", enrolledMisCode));

        delete(misCode, String.format("faunits/%s/%s/%s", cccId, sisTermId, cId), params,
                new ParameterizedTypeReference<List<CAFinancialAidUnits>>() {
                });
    }

    /**
     * Add a Placement Transaction for a student
     *
     * @param misCode                MIS Code
     * @param caPlacementTransaction Placement Transaction
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public void addPlacementTransaction(@NotNull String misCode, @NotNull CAPlacementTransaction caPlacementTransaction)
            throws CollegeAdaptorException {
        logger.info("Adding Placement Transaction to student with CCC ID {} from {}",
                caPlacementTransaction.getCccid(), misCode);

        post(misCode, "placements", null, caPlacementTransaction,
                new ParameterizedTypeReference<CAPlacementTransaction>() {
                });
    }

    /**
     * Get a map of values from the info endpoint
     *
     * @param misCode MIS Code
     * @return Info
     * @throws CollegeAdaptorException College Adaptor Exception
     */
    public Map getInfo(@NotNull String misCode) throws CollegeAdaptorException {
        logger.info("Retrieving data from college adaptor info endpoint");

        return get(misCode, "info", null, new ParameterizedTypeReference<Map>() {
        });
    }
}
