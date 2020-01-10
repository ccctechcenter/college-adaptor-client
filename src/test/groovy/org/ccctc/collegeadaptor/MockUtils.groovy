package org.ccctc.collegeadaptor

import org.apache.http.NameValuePair
import org.apache.http.client.utils.URIBuilder
import org.ccctc.collegeadaptor.exceptions.CollegeAdaptorException
import org.ccctc.collegeadaptor.model.CAPerson
import org.ccctc.collegeadaptor.model.CAStudent
import org.ccctc.collegeadaptor.model.CATerm
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity

class MockUtils {

    CollegeAdaptorRestClient client
    CollegeAdaptorService service

    MockUtils(CollegeAdaptorRestClient client, CollegeAdaptorService service) {
        this.client = client
        this.service = service
    }

    // these version of "get", "post", etc will strip out the mis=xxx parameter passed in other college adaptor calls
    // as this fails with some mock calls. this is a quick hack to make them work.
    def <T> T get(String misCode, String relativePath, List<NameValuePair> params,
                  ParameterizedTypeReference<T> typeRef) {
        try {
            URIBuilder uriBuilder = client.applicationUriBuilder(misCode, relativePath)
            uriBuilder.clearParameters()
            if (params != null) uriBuilder.addParameters(params)
            RequestEntity request = RequestEntity.get(uriBuilder.build()).build()
            return service.exchange(request, typeRef)
        } catch (URISyntaxException e) {
            throw new CollegeAdaptorException(String.format("Caught URISyntaxException exception %s", e.getMessage()),
                    null, HttpStatus.EXPECTATION_FAILED)
        }
    }

    def <T> T post(String misCode, String relativePath, List<NameValuePair> params, Object body,
                   ParameterizedTypeReference<T> returnType)
            throws CollegeAdaptorException {
        try {
            URIBuilder uriBuilder = client.applicationUriBuilder(misCode, relativePath)
            uriBuilder.clearParameters()
            if (params != null) uriBuilder.addParameters(params)
            RequestEntity request = RequestEntity.post(uriBuilder.build()).body(body)
            return service.exchange(request, returnType)
        } catch (URISyntaxException e) {
            throw new CollegeAdaptorException(String.format("Caught URISyntaxException exception %s", e.getMessage()),
                    null, HttpStatus.EXPECTATION_FAILED)
        }
    }

    def <T> T delete(String misCode, String relativePath, List<NameValuePair> params,
                           ParameterizedTypeReference<T> typeRef) throws CollegeAdaptorException {
        try {
            URIBuilder uriBuilder = client.applicationUriBuilder(misCode, relativePath)
            uriBuilder.clearParameters()
            if (params != null) uriBuilder.addParameters(params)
            RequestEntity request = RequestEntity.delete(uriBuilder.build()).build()
            return service.exchange(request, typeRef)
        } catch (URISyntaxException e) {
            throw new CollegeAdaptorException(String.format("Caught URISyntaxException exception %s", e.getMessage()),
                    null, HttpStatus.EXPECTATION_FAILED)
        }
    }

    CATerm deleteTerm(String misCode, String sisTermId) {
        return delete(misCode, "mock/colleges/$misCode/terms/$sisTermId", null, new ParameterizedTypeReference<CATerm>() {})
    }

    CATerm addTerm(String misCode, CATerm term) {
        return post(misCode, "mock/terms", null, term, new ParameterizedTypeReference<CATerm>() {})
    }

    CAPerson deletePerson(String misCode, String sisPersonId) {
        return delete(misCode, "mock/colleges/$misCode/persons/$sisPersonId", null, new ParameterizedTypeReference<CAPerson>() {})
    }

    CAPerson addPerson(String misCode, CAPerson person) {
        return post(misCode, "mock/persons", null, person, new ParameterizedTypeReference<CAPerson>() {})
    }

    CAStudent deleteStudent(String misCode, String sisTermId, String sisPersonId) {
        delete(misCode, "mock/colleges/$misCode/terms/$sisTermId/students/$sisPersonId", null, new ParameterizedTypeReference<CAStudent>() {})
    }

    CAStudent addStudent(String misCode, CAStudent student) {
        // CAStudent is missing misCode, but mock DB requires it
        def studentMap = student.properties << [misCode: misCode]

        return post(misCode, "mock/students", null, studentMap, new ParameterizedTypeReference<CAStudent>() {})
    }
}
