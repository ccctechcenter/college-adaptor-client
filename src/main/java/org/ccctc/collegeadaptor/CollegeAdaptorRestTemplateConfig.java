package org.ccctc.collegeadaptor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2 Configuration for the College Adaptor REST Client
 *
 * @author bshroeder, zrogers
 */
@Configuration
public class CollegeAdaptorRestTemplateConfig {

    private final String caClientId;
    private final String caClientSecret;
    private final String caAccessTokenUri;
    private final String caGrantType;
    private final boolean usePoolingClient;
    private final int maxPerRoute;
    private final int maxTotal;

    @Autowired
    public CollegeAdaptorRestTemplateConfig(@Value("${college_adaptor.security.oauth2.client.client-id}") String caClientId,
                                            @Value("${college_adaptor.security.oauth2.client.client-secret}") String caClientSecret,
                                            @Value("${college_adaptor.security.oauth2.client.access-token-uri}") String caAccessTokenUri,
                                            @Value("${college_adaptor.security.oauth2.client.grant-type}") String caGrantType,
                                            @Value("${college_adaptor.pooling.client:true}") boolean usePoolingClient,
                                            @Value("${college_adaptor.pooling.client.max.per.route:100}") int maxPerRoute,
                                            @Value("${college_adaptor.pooling.client.max.total:100}") int maxTotal) {
        this.caClientId = caClientId;
        this.caClientSecret = caClientSecret;
        this.caAccessTokenUri = caAccessTokenUri;
        this.caGrantType = caGrantType;
        this.usePoolingClient = usePoolingClient;
        this.maxPerRoute = maxPerRoute;
        this.maxTotal = maxTotal;
    }

    /**
     * Set up the REST Client template for the college adaptor
     *
     * @return
     */
    @Bean
    public OAuth2RestTemplate collegeAdaptorRestTemplate() {
        // Set up credential details
        ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
        details.setId("college-adaptor");
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        details.setAuthenticationScheme(AuthenticationScheme.form);
        details.setClientId(caClientId);
        details.setClientSecret(caClientSecret);
        details.setAccessTokenUri(caAccessTokenUri);
        details.setGrantType(caGrantType);

        // Create and configure REST template
        OAuth2RestTemplate template = new OAuth2RestTemplate(details, new DefaultOAuth2ClientContext());
        List<HttpMessageConverter<?>> converters = template.getMessageConverters();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jackson2HalModule());
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json,application/json"));
        converter.setObjectMapper(mapper);
        List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
        newConverters.add(converter);
        newConverters.addAll(converters);
        template.setMessageConverters(newConverters);

        if (usePoolingClient) {
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setDefaultMaxPerRoute(maxPerRoute);
            connectionManager.setMaxTotal(maxTotal);
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            template.setRequestFactory(factory);
        }

        return template;
    }
}
