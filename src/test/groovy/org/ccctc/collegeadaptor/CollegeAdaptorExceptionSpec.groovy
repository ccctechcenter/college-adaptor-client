package org.ccctc.collegeadaptor

import org.apache.commons.codec.Charsets
import org.ccctc.collegeadaptor.exceptions.CollegeAdaptorException
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import spock.lang.Specification

/**
 * Created by Zeke on 2/7/2017.
 */
class CollegeAdaptorExceptionSpec extends Specification {

    def "HttpClientErrorException"() {
        when:
        def result = new CollegeAdaptorException("message", "code", HttpStatus.NOT_FOUND)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        result.code == "code"
        result.message == "message"
    }

    def "HttpClientErrorException - no body"() {
        setup:
        def exception = new HttpClientErrorException(HttpStatus.NOT_FOUND)

        when:
        def result = new CollegeAdaptorException(exception)

        then:
        result.statusCode == HttpStatus.NOT_FOUND
        result.code == null
        result.message == exception.toString()
    }

    def "HttpClientErrorException - proper body"() {
        setup:
        def body = '{"code":"code", "message":"message"}'
        def exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", body.getBytes(), Charsets.US_ASCII)

        when:
        def result = new CollegeAdaptorException(exception)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        result.code == "code"
        result.message == "message"
    }

    def "HttpClientErrorException - non-string data types"() {
        setup:
        def body = '{"code":1, "message":1}'
        def exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", body.getBytes(), Charsets.US_ASCII)

        when:
        def result = new CollegeAdaptorException(exception)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        result.code == "1"
        result.message == "1"
    }

    def "HttpClientErrorException - null code and message"() {
        setup:
        def body = '{"code":null, "message":null}'
        def exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", body.getBytes(), Charsets.US_ASCII)

        when:
        def result = new CollegeAdaptorException(exception)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        result.code == null
        result.message == exception.toString()
    }

    def "HttpClientErrorException - bad JSON"() {
        setup:
        def body = '{"code":"code'
        def exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", body.getBytes(), Charsets.US_ASCII)

        when:
        def result = new CollegeAdaptorException(exception)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        result.code == null
        result.message == exception.toString()
    }

    def "HttpClientErrorException - other JSON"() {
        setup:
        def body = '{"aValue":"a","bValue":"b"}'
        def exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request", body.getBytes(), Charsets.US_ASCII)

        when:
        def result = new CollegeAdaptorException(exception)

        then:
        result.statusCode == HttpStatus.BAD_REQUEST
        result.code == null
        result.message == exception.toString()
    }
}
