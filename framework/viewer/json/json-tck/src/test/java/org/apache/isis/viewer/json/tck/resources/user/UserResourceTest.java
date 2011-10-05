package org.apache.isis.viewer.json.tck.resources.user;

import static org.apache.isis.viewer.json.tck.RepresentationMatchers.assertThat;
import static org.apache.isis.viewer.json.tck.RepresentationMatchers.isArray;
import static org.apache.isis.viewer.json.tck.RepresentationMatchers.isFollowableLinkToSelf;
import static org.apache.isis.viewer.json.tck.RepresentationMatchers.isLink;
import static org.apache.isis.viewer.json.tck.RepresentationMatchers.isMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

import org.apache.isis.runtimes.dflt.webserver.WebServer;
import org.apache.isis.viewer.json.applib.RepresentationType;
import org.apache.isis.viewer.json.applib.RestfulClient;
import org.apache.isis.viewer.json.applib.RestfulResponse;
import org.apache.isis.viewer.json.applib.RestfulResponse.HttpStatusCode;
import org.apache.isis.viewer.json.applib.blocks.Method;
import org.apache.isis.viewer.json.applib.user.UserRepresentation;
import org.apache.isis.viewer.json.applib.user.UserResource;
import org.apache.isis.viewer.json.tck.IsisWebServerRule;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;


public class UserResourceTest {

    @Rule
    public IsisWebServerRule webServerRule = new IsisWebServerRule();
    
    private RestfulClient client;
    private UserResource resource;

    @Before
    public void setUp() throws Exception {
        WebServer webServer = webServerRule.getWebServer();
        client = new RestfulClient(webServer.getBase());
        
        resource = client.getUserResource();
    }

    @Ignore("self link is broken")
    @Test
    public void representation() throws Exception {

        // given
        Response resp = resource.user();
        
        // when
        RestfulResponse<UserRepresentation> jsonResp = RestfulResponse.ofT(resp);
        assertThat(jsonResp.getStatus().getFamily(), is(Family.SUCCESSFUL));
        
        // then
        assertThat(jsonResp.getStatus(), is(HttpStatusCode.OK));
        assertThat(jsonResp.getHeader(RestfulResponse.Header.CONTENT_TYPE), is(RepresentationType.USER.getMediaType()));
        assertThat(jsonResp.getHeader(RestfulResponse.Header.CACHE_CONTROL).getMaxAge(), is(3600));
        
        UserRepresentation repr = jsonResp.getEntity();
        assertThat(repr, is(not(nullValue())));
        assertThat(repr.isMap(), is(true));
        
        assertThat(repr.getSelf(), isLink(client).method(Method.GET));
        assertThat(repr.getUsername(), is(not(nullValue())));
        assertThat(repr.getFriendlyName(), is(nullValue())); // TODO: change fixture so populated
        assertThat(repr.getEmail(), is(nullValue())); // TODO: change fixture so populated
        assertThat(repr.getRoles(), is(not(nullValue()))); // TODO: change fixture so have non-empty list
        
        assertThat(repr.getLinks(), isArray());
        assertThat(repr.getExtensions(), isMap());
    }

    @Ignore("self link is broken")
    @Test
    public void self_isFollowable() throws Exception {
        // given
        UserRepresentation repr = givenRepresentation();

        // when, then
        assertThat(repr, isFollowableLinkToSelf(client));
    }
    
    private UserRepresentation givenRepresentation() throws JsonParseException, JsonMappingException, IOException {
        RestfulResponse<UserRepresentation> jsonResp = RestfulResponse.ofT(resource.user());
        return jsonResp.getEntity();
    }

}


    