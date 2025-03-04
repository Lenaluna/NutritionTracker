package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerE2ETest {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate; // REST client for making API calls

    @Test
    void testRetrieveUserFromDevDataLoader() {
        // Dynamically construct the base URL with the randomized server port
        String url = "http://localhost:" + port + "/api/user";

        // Call the GET /user endpoint
        ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);

        // Verify that the response status is 200 OK
        assertThat(response.getStatusCode()).isEqualTo(OK);

        // Extract the user from the response body and verify its properties
        UserDTO user = response.getBody();
        assertThat(user).isNotNull(); // Ensure a user is returned
        assertThat(user.getName()).isEqualTo("Test User"); // Name as defined in DevDataLoader
        assertThat(user.getAge()).isEqualTo(30); // Age as defined in DevDataLoader
        assertThat(user.getWeight()).isEqualTo(70.0); // Weight as defined in DevDataLoader
        assertThat(user.getIsAthlete()).isFalse(); // Athlete status as defined in DevDataLoader
    }
}