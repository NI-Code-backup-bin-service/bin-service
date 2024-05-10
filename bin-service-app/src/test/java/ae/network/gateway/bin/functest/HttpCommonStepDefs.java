/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.functest;

import ae.network.gateway.bin.BinConfigurationBean;
import com.jayway.jsonassert.JsonAssert;
import com.jayway.jsonassert.JsonAsserter;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import cucumber.api.DataTable;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HttpCommonStepDefs {

    private Client client;

    private final ScenarioContext scenarioContext;
    private final LocalSpringBootEnvironment env;

    @Before
    public void initialiseClient() {

        ClientConfig config = new ClientConfig()
                .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO");
        client = ClientBuilder.newClient(config);
    }


    public HttpCommonStepDefs(ScenarioContext scenarioContext, LocalSpringBootEnvironment env) {
        this.scenarioContext = scenarioContext;
        this.env = env;
    }

    @When("^I (GET|DELETE) from uri (.+)$")
    public void invoke(String httpMethod, String path) {

        Builder builder = client.target(env.getBaseUri())
                .path(path)
                .request();

        Response response = addAuthorization(builder).method(httpMethod);
        scenarioContext.consume(response);
    }

    @When("^I (PUT|PATCH|POST) to uri (.+) with body$")
    public void invoke(String httpMethod, String path, String body) {

        Builder builder = client.target(env.getBaseUri())
                .path(path)
                .request();

        Response response = addAuthorization(builder)
                .method(httpMethod, Entity.json(body));
        scenarioContext.consume(response);
    }

    @When("^I (PUT|PATCH|POST) to link rel '(.+)' with body$")
    public void invokeLinkWithBody(String httpMethod, String rel, String body) {

        String responseBody = scenarioContext.getResponseBody();
        List<String> links = JsonPath.parse(responseBody).read("$.._links." + rel + ".href");

        Builder builder = client.target(links.get(0))
                .request(scenarioContext.getMediaType());

        Response response = addAuthorization(builder)
                .method(httpMethod, Entity.entity(body, scenarioContext.getMediaType()));
        scenarioContext.consume(response);
    }

    @When("^I (GET|DELETE) from link rel '(//w+)'$")
    public void invokeLink(String httpMethod, String rel) {
        String responseBody = scenarioContext.getResponseBody();

        Configuration jsonPathConf = Configuration.builder()
                .options(Option.ALWAYS_RETURN_LIST).build();
        List<String> links = JsonPath.using(jsonPathConf).parse(responseBody).read("$.._links." + rel + ".href");

        Builder builder = client.target(links.get(0))
                .request();

        Response response = addAuthorization(builder)
                .method(httpMethod);

        scenarioContext.consume(response);
    }

    @When("^I (GET|DELETE) from templated link rel '(.+)' with parameters '(.+)'$")
    public void invokeTemplatedLink(String httpMethod, String rel, List<String> parameters) {
        String responseBody = scenarioContext.getResponseBody();

        Configuration jsonPathConf = Configuration.builder()
                .options(Option.ALWAYS_RETURN_LIST).build();
        List<String> links = JsonPath.using(jsonPathConf).parse(responseBody).read("$.._links." + rel + ".href");

        URI link = UriBuilder.fromUri(links.get(0))
            .build(parameters.toArray());

        Builder builder = client.target(link)
                .request();

        Response response = addAuthorization(builder)
                .method(httpMethod);

        scenarioContext.consume(response);
    }

    @When("^I (GET|DELETE) from location header$")
    public void invokeLocation(String httpMethod) {
        URI location = scenarioContext.getResponse().getLocation();

        Builder builder = client.target(location)
                .request();

        Response response = addAuthorization(builder)
                .method(httpMethod);

        scenarioContext.consume(response);
    }

    private Builder addAuthorization(Builder builder) {

        return (scenarioContext.getAccessToken() != null)
                ? builder.header(HttpHeaders.AUTHORIZATION, "Bearer " + scenarioContext.getAccessToken())
                : builder;
    }

    @Then("^the response code is (\\d{3})$")
    public void assertThatResponseCodeIs(int responseCode) {
        assertThat(scenarioContext.getResponse().getStatus(), is(responseCode));

    }

    @Then("^the response matches$")
    public void assertThatResponseJsonMatches(List<Map<String, String>> assertions) {

        JsonAsserter asserter = JsonAssert.with(scenarioContext.getResponseBody());

        assertions.forEach(a -> {
            String path = a.get("path");
            String matcher = a.get("matcher");
            String expected = a.get("expected");

            switch (matcher) {
                case "isInt":
                    asserter.assertThat(path, is(Integer.valueOf(expected)));
                    break;
                case "isString":
                    asserter.assertThat(path, is(String.valueOf(expected)));
                    break;
                case "isBool":
                    asserter.assertThat(path, is(Boolean.valueOf(expected)));
                    break;
                default:
                    asserter.assertThat(path, is(expected));
            }
        });
    }

    @Given("^I use version (.+) of the api$")
    public void useApiVersion(String version) {
        scenarioContext.setMediaType(version);
    }

    @Given("^I use mada file bin search$")
    public void setConfigData() {
        BinConfigurationBean configurationBean
                = LocalSpringBootEnvironment.SpringHolder.INSTANCE.getBean(BinConfigurationBean.class);
        configurationBean.setIncludeMadaSchemeBins(true);
    }

    @When("^I GET mediaType (.+) from uri (.+)$")
    public void getWithMediaType(String mediaType, String path) {
        WebTarget target = client.target(env.getBaseUri()).path(path);
        Builder builder = target.request(mediaType);
        Response response = builder.method("GET", Entity.entity(null, mediaType));
        scenarioContext.consume(response);
    }

    @And("text body contains lines matching$")
    public void assertTextBodyContainsLinesMatching(DataTable dataTable) {
        List<String> expectedLines = dataTable.asList(String.class);
        final String metrics = scenarioContext.getResponseBody();
        expectedLines.stream().forEach(r -> {

            Pattern pattern = Pattern.compile("^" + r, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(metrics);

            assertThat("line matching " + r, matcher.find(), is(true));
        });
    }

}
