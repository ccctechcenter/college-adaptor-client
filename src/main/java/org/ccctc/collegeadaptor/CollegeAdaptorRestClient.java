package org.ccctc.collegeadaptor;

import org.apache.http.client.utils.URIBuilder;
import org.ccctc.collegeadaptor.util.CoverageIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;

/**
 * College Adaptor REST Client. Standardizes the process of creating URLs and connecting to the College Adaptor
 * Router.
 *
 * Usage:
 * - Use applicationUriBuilder() to create a URI
 * - Use template() to get the RestOperations object that will be used to make the REST requests (using the URI
 *   you created with applicationUriBuilder())
 *
 * @author bschroeder, zrogers
 */
@Service
public class CollegeAdaptorRestClient {

    private final RestOperations collegeAdaptorRestTemplate;
    private final String collegeAdaptorRouterProtocol;
    private final String collegeAdaptorRouterHost;
    private final String collegeAdaptorRouterPort;

    @Autowired
    public CollegeAdaptorRestClient(RestOperations collegeAdaptorRestTemplate,
                                    @Value("${collegeadaptor.router.protocol}") String collegeAdaptorRouterProtocol,
                                    @Value("${collegeadaptor.router.host}") String collegeAdaptorRouterHost,
                                    @Value("${collegeadaptor.router.port}") String collegeAdaptorRouterPort) {
        this.collegeAdaptorRestTemplate = collegeAdaptorRestTemplate;
        this.collegeAdaptorRouterProtocol = collegeAdaptorRouterProtocol;
        this.collegeAdaptorRouterHost = collegeAdaptorRouterHost;
        this.collegeAdaptorRouterPort = collegeAdaptorRouterPort;
    }

    /**
     * Instantiates a new URIBuilder where the URI is:
     * {routerUrl} + "/" relativePath + "?mis=" + misCode
     *
     * Note: spaces are converted to %20. All other characters are left alone.
     *
     * @param misCode      MIS Code
     * @param relativePath Relative path for the URI
     * @return URIBuilder
     * @throws URISyntaxException
     */
    public URIBuilder applicationUriBuilder(@NotNull String misCode, String relativePath) throws URISyntaxException {
        String resourceUrl = serverUrl(misCode) + "/" + (relativePath == null ? "" : relativePath.replace(" ", "%20"));
        URIBuilder uriBuilder = new URIBuilder(resourceUrl);
        uriBuilder.addParameter("mis", misCode);
        return uriBuilder;
    }

    /**
     * Instantiates a new URIBuilder where the URI is:
     * {routerUrl} + "/" relativePath + "?mis=" + misCode
     *
     * Additionally, pageable attributes limit, offset, size and number are included as parameters if present.
     *
     * @param misCode      MIS Code
     * @param relativePath Relative path for the URI
     * @param pageable     Pageable information
     * @return URIBuilder
     * @throws URISyntaxException
     */

    public URIBuilder applicationUriBuilder(@NotNull String misCode, String relativePath, Pageable pageable)
            throws URISyntaxException {
        URIBuilder uriBuilder = applicationUriBuilder(misCode, relativePath);
        if (pageable != null) {
            uriBuilder.addParameter("limit", String.format("%s", pageable.getPageSize()));
            uriBuilder.addParameter("offset", String.format("%s", pageable.getOffset()));
            uriBuilder.addParameter("size", String.format("%s", pageable.getPageSize()));
            uriBuilder.addParameter("number", String.format("%s", pageable.getPageNumber()));
        }
        return uriBuilder;
    }

    /**
     * Get the URL used to call the College Adaptor Router for a particular MIS Code
     *
     * @param misCode MIS Code
     * @return URL
     */
    public String serverUrl(@NotNull String misCode) {
        return String.format("%s://%s:%s/adaptor/%s", collegeAdaptorRouterProtocol, collegeAdaptorRouterHost,
                collegeAdaptorRouterPort, misCode);
    }

    /**
     * Get the REST template object, which is used to make the actual REST calls.
     *
     * @return RestOperations
     */
    public RestOperations template() {
        return collegeAdaptorRestTemplate;
    }

    @Override
    @CoverageIgnore
    public String toString() {
        return "CollegeAdaptorRestClient{" +
                "collegeAdaptorRestTemplate=" + collegeAdaptorRestTemplate +
                ", collegeAdaptorRouterHost='" + collegeAdaptorRouterHost + '\'' +
                ", collegeAdaptorRouterPort='" + collegeAdaptorRouterPort + '\'' +
                ", collegeAdaptorRouterProtocol='" + collegeAdaptorRouterProtocol + '\'' +
                '}';
    }
}