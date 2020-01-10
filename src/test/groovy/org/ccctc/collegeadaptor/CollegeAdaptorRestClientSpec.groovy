package org.ccctc.collegeadaptor

import org.springframework.data.domain.PageRequest
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import spock.lang.Specification

/**
 * Created by zekeo on 2/7/2017.
 */
class CollegeAdaptorRestClientSpec extends Specification {

    def mockOAuth2RestTemplate = Mock(OAuth2RestTemplate)
    def collegeAdaptorRestClientTest = new CollegeAdaptorRestClient(mockOAuth2RestTemplate, "protocol", "host", "port")

    def "applicationUriBuilder"() {
        setup:
        def result

        // null mis code
        when:
        result = collegeAdaptorRestClientTest.applicationUriBuilder(null, "path").build()

        then:
        result.toString() == "protocol://host:port/adaptor/null/path?mis"

        // regular mis code
        when:
        result = collegeAdaptorRestClientTest.applicationUriBuilder("999", "path").build()

        then:
        result.toString() == "protocol://host:port/adaptor/999/path?mis=999"

        // null path
        when:
        result = collegeAdaptorRestClientTest.applicationUriBuilder("999", null).build()

        then:
        result.toString() == "protocol://host:port/adaptor/999/?mis=999"

        // pageable
        when:
        result = collegeAdaptorRestClientTest.applicationUriBuilder("999", "path", new PageRequest(2,5))

        then:
        result.toString() == "protocol://host:port/adaptor/999/path?mis=999&limit=5&offset=10&size=5&number=2"

        // pageable w/ null
        when:
        result = collegeAdaptorRestClientTest.applicationUriBuilder("999", "path", null)

        then:
        result.toString() == "protocol://host:port/adaptor/999/path?mis=999"
    }
}