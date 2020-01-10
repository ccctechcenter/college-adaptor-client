package org.ccctc.collegeadaptor.exceptions;

import org.ccctc.collegeadaptor.util.CoverageIgnore;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.EOFException;
import java.util.Map;

/**
 * Exception returned from a failed College Adaptor call
 *
 * @author zrogers
 */
public class CollegeAdaptorException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(CollegeAdaptorException.class);

    private String code;
    private String message;
    private HttpStatus statusCode;

    public CollegeAdaptorException(String message, String code, HttpStatus statusCode) {
        super(message);
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public CollegeAdaptorException(HttpStatusCodeException httpStatusCodeException) {
        super(httpStatusCodeException.getMessage());
        this.statusCode = httpStatusCodeException.getStatusCode();

        // Extract code and message from body
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> mapped = mapper.readValue(httpStatusCodeException.getResponseBodyAsString(),
                    new TypeReference<Map<String, Object>>() {
                    });

            if (mapped.containsKey("code")) {
                Object code = mapped.get("code");
                if (code != null) {
                    if (code instanceof String) {
                        this.code = (String) code;
                    } else {
                        this.code = code.toString();
                    }
                }
            }

            if (mapped.containsKey("message")) {
                Object message = mapped.get("message");
                if (message != null) {
                    if (message instanceof String) {
                        this.message = (String) message;
                    } else {
                        this.message = message.toString();
                    }
                }
            }
        } catch (EOFException | JsonParseException e) {
            // ignore - empty or non-JSON data in body
        } catch (Exception e) {
            logger.error("Error generating CollegeAdaptorException", e);
        }

        if (this.message == null) {
            this.message = httpStatusCodeException.toString();
        }
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    @Override
    @CoverageIgnore
    public String toString() {
        return "CollegeAdaptorException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}