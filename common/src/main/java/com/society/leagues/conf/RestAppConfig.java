package com.society.leagues.conf;

import com.society.leagues.infrastructure.security.SecurityFilter;
import com.society.leagues.resource.ApiResource;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Application;
import java.util.List;

@Component
public class RestAppConfig extends ResourceConfig {
    private static Logger logger = LoggerFactory.getLogger(RestAppConfig.class);
    @Autowired List<ApiResource> resources;
    @Autowired SecurityFilter securityFilter;

    public RestAppConfig() {
        super();
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setBasePath("http://localhost:8002/api");
        beanConfig.setResourcePackage("com.society");
        beanConfig.setScan(true);
    }

    @PostConstruct
    public void init() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setBasePath("http://localhost:8002/api");
        beanConfig.setResourcePackage("com.society");
        beanConfig.setScan(true);

        register(com.wordnik.swagger.jaxrs.listing.ApiListingResource.class);
        register(com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider.class);
        register(com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON.class);
        register(com.wordnik.swagger.jaxrs.listing.ResourceListingProvider.class);

        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.JSON_PROCESSING_FEATURE_DISABLE, false);
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, true);
        property(ServerProperties.WADL_FEATURE_DISABLE, true);
        property(ServerProperties.MONITORING_STATISTICS_ENABLED,true);

        register(RolesAllowedDynamicFeature.class);
        //register(LoggingFilter.class);
        register(JacksonFeature.class);
        register(securityFilter);
        logger.info("Found " + resources.size() + " resources");
        for (ApiResource resource : resources) {
            logger.info("Registering " + resource.getClass().getSimpleName());
        }
        resources.forEach(this::register);

    }

}
