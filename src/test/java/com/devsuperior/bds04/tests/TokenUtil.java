package com.devsuperior.bds04.tests;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@Component
public class TokenUtil {

    @Value("${security.client-id}")
    private String clientId;

    @Value("${security.client-secret}")
    private String clientSecret;

    public String obtainAccessToken(MockMvc mockMvc, String username, String password) throws Exception {

        ResultActions result = mockMvc
                .perform(post("/oauth2/token")
                        .with(httpBasic(clientId, clientSecret))
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .accept(APPLICATION_JSON)
                        .param("grant_type", "password")
                        .param("client_id", clientId)
                        .param("username", username)
                        .param("password", password));

        result.andExpect(status().isOk());

        String resultString = result
                .andReturn()
                .getResponse()
                .getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();

        return jsonParser
                .parseMap(resultString)
                .get("access_token")
                .toString();
    }
}
