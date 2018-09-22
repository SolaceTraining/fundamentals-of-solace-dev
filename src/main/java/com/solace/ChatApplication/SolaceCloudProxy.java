package com.solace.ChatApplication;

import com.google.gson.Gson;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

@Controller
public class SolaceCloudProxy {

    Gson gson = new Gson();

    @Value("${solace.rest.host}")
    private String solaceRESTHost;

    @Value("${solace.username}")
    private String solaceUsername;

    @Value("${solace.password}")
    private String solacePassword;

    private HttpHeaders httpHeaders;

    @PostConstruct
    public void init() {
        httpHeaders = new HttpHeaders() {{
            String auth = solaceUsername + ":" + solacePassword;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            set("Solace-Reply-Wait-Time-In-ms", "3000");
            set("Content-Type","application/json");
        }};
    }

    @RequestMapping(value = "/solace/cloud/proxy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity LoginToSolaceCloud(@RequestBody UserObject userObject) {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<UserObject> request = new HttpEntity<UserObject>(userObject, httpHeaders);

        AuthenticatedObject authenticatedObject = restTemplate.postForObject(solaceRESTHost + "/LOGIN/MESSAGE/REQUEST", request, AuthenticatedObject.class);

        if (authenticatedObject.isAuthenticated()) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
