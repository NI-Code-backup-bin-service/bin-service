/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin;

import ae.network.commons.monitoring.metrics.JerseyResourceMetricsConfigurator;
import ae.network.domain.errors.mappers.ExceptionMappers;
import ae.network.domain.errors.mappers.i18n.HibernateValidatorMessageInterpolatorResolver;
import ae.network.domain.errors.mappers.i18n.SpringMessageSourceContextResolver;
import ae.network.gateway.bin.rest.ResourceConfiguration;
import ae.network.logging.AuditUser;
import ae.network.logging.filters.AuditLoggingFilter;
import com.google.common.collect.ImmutableSet;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.web.context.ServletConfigAware;

import javax.servlet.ServletConfig;
import javax.ws.rs.Path;
import javax.ws.rs.Priorities;

@Configuration
@ComponentScan(basePackages = {"ae.network.gateway.bin"})
public class ApplicationConfiguration implements ServletConfigAware {

    private ServletConfig servletConfig;

    @Bean
    FilterRegistrationBean<ServletContainer> jersey(
        ApplicationContext context,
        MeterRegistry meterRegistry,
        AuditLoggingFilter auditLoggingFilter,
        MessageSource messageSource
    ) throws OpenApiConfigurationException {

        ResourceConfig resourceConfig = new ResourceConfiguration();
        resourceConfig.property(ServletProperties.FILTER_FORWARD_ON_404, true);
        resourceConfig.property(ServletProperties.FILTER_STATIC_CONTENT_REGEX, "/(actuator|error).*");

        swaggerConfiguration(resourceConfig);
        resourceConfig.register(JerseyResourceMetricsConfigurator.configureFor(meterRegistry));

        resourceConfig.register(auditLoggingFilter, Priorities.AUTHENTICATION - 100);
        resourceConfig.registerClasses(ExceptionMappers.EXCEPTION_MAPPERS);
        resourceConfig.register(new SpringMessageSourceContextResolver(messageSource));
        resourceConfig.register(new HibernateValidatorMessageInterpolatorResolver(
            new MessageSourceResourceBundleLocator(messageSource)
        ));

        context.getBeansWithAnnotation(Path.class).values().forEach(resourceConfig::register);
        ServletContainer jersey = new ServletContainer(resourceConfig);

        resourceConfig.register(OpenApiResource.class);
        return new FilterRegistrationBean<>(jersey);
    }

    @Bean
    public MessageSource messageSource(@Value("${messageSource.baseName:ErrorMessages}") String baseName) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(baseName);
        return messageSource;
    }

    private void swaggerConfiguration(ResourceConfig resourceConfig) throws OpenApiConfigurationException {
        OpenAPI oas = new OpenAPI();
        Info info = new Info()
            .title("Network International NextGen Payment Gateway")
            .version("0.0.1");
        oas.info(info);

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
            .openAPI(oas)
            .prettyPrint(true)
            .resourcePackages(ImmutableSet.of("ae.network.gateway.bin.rest"));

        new JaxrsOpenApiContextBuilder<>()
            .servletConfig(servletConfig)
            .application(resourceConfig)
            .openApiConfiguration(oasConfig)
            .buildContext(true);
    }

    @Bean
    AuditLoggingFilter auditFilter() {
        return new AuditLoggingFilter(() -> new AuditUser("anonymoususer", "anonymous"));
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }
}
