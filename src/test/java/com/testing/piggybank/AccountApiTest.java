package com.testing.piggybank;

import com.testing.piggybank.account.AccountResponse;
import com.testing.piggybank.account.GetAccountsResponse;
import com.testing.piggybank.account.UpdateAccountRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAccount() {
        ResponseEntity<AccountResponse> response = restTemplate
                .getForEntity("/api/v1/accounts/1", AccountResponse.class);

        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1L, response.getBody().getId());
        Assertions.assertEquals("Rekening van Melvin", response.getBody().getName());
    }

    @Test
    public void testGetAccounts() {
        // Voeg de juiste X-User-Id header toe aan de aanvraag
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-User-Id", "1");  // Stel hier de juiste User Id in
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Stuur de request met de header
        ResponseEntity<GetAccountsResponse> response = restTemplate.exchange(
                "/api/v1/accounts",
                HttpMethod.GET,
                entity,
                GetAccountsResponse.class
        );

        // Voer de assertions uit
        Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
        Assertions.assertNotNull(response.getBody());
        List<AccountResponse> accounts = response.getBody().getAccounts();
        Assertions.assertFalse(accounts.isEmpty());
        Assertions.assertEquals("Rekening van Melvin", accounts.get(0).getName());
    }

    @Test
    public void testUpdateAccount() {
        UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
        updateAccountRequest.setAccountId(1L);
        updateAccountRequest.setAccountName("Updated Account");

        HttpEntity<UpdateAccountRequest> request = new HttpEntity<>(updateAccountRequest);

        ResponseEntity<HttpStatus> response = restTemplate.exchange(
                "/api/v1/accounts",
                HttpMethod.PUT,
                request,
                HttpStatus.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}