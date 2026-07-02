package com.jio.party;

import com.jio.party.security.JwtUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.MariaDBContainer;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PartyRoleControllerIntegrationTest {

    static MariaDBContainer<?> mariaDB;

    @BeforeAll
    static void setUp() {
        try {
            DockerClientFactory.instance().client();
        } catch (Exception e) {
            Assumptions.assumeTrue(false,
                "Docker not available — skipping integration tests (requires compatible Docker Desktop)");
        }
        mariaDB = new MariaDBContainer<>("mariadb:10.11")
                .withDatabaseName("jio_party")
                .withUsername("jio")
                .withPassword("jio123");
        mariaDB.start();
    }

    @AfterAll
    static void tearDown() {
        if (mariaDB != null) mariaDB.stop();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        if (mariaDB != null && mariaDB.isRunning()) {
            registry.add("spring.datasource.url", mariaDB::getJdbcUrl);
            registry.add("spring.datasource.username", mariaDB::getUsername);
            registry.add("spring.datasource.password", mariaDB::getPassword);
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    private static String createdId;
    private String adminToken;

    private static final String BASE = "/tmf-api/partyManagement/v4/partyRole";

    @BeforeEach
    void generateToken() {
        adminToken = jwtUtil.generateToken("test-admin", List.of("ROLE_ADMIN"));
    }

    private HttpHeaders adminHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        return headers;
    }

    private HttpHeaders adminJsonHeaders() {
        HttpHeaders headers = adminHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @Order(1)
    void post_validPayload_returns201() {
        Map<String, Object> body = Map.of("name", "Global Pirates", "roleType", "ContentProvider");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, adminJsonHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(BASE, HttpMethod.POST, request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).containsKey("id");
        assertThat(response.getBody()).containsEntry("name", "Global Pirates");
        assertThat(response.getBody()).containsEntry("roleType", "ContentProvider");
        assertThat(response.getBody().get("href").toString()).contains("/partyRole/");

        createdId = (String) response.getBody().get("id");
    }

    @Test
    @Order(2)
    void post_missingName_returns400() {
        Map<String, Object> body = Map.of("roleType", "Banking");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, adminJsonHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(BASE, HttpMethod.POST, request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsKey("code");
        assertThat(response.getBody()).containsKey("reason");
        assertThat(response.getBody()).containsKey("message");
        assertThat(response.getBody()).containsKey("status");
    }

    @Test
    @Order(3)
    void getAll_returns200WithList() {
        HttpEntity<?> request = new HttpEntity<>(adminHeaders());

        ResponseEntity<List> response = restTemplate.exchange(BASE, HttpMethod.GET, request, List.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    @Order(4)
    void getById_exists_returns200() {
        HttpEntity<?> request = new HttpEntity<>(adminHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(BASE + "/" + createdId, HttpMethod.GET, request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("id", createdId);
    }

    @Test
    @Order(5)
    void getById_notFound_returns404WithErrorFormat() {
        HttpEntity<?> request = new HttpEntity<>(adminHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(BASE + "/nonexistent-id", HttpMethod.GET, request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).containsKey("code");
        assertThat(response.getBody()).containsKey("reason");
        assertThat(response.getBody()).containsKey("message");
        assertThat(response.getBody()).containsKey("status");
    }

    @Test
    @Order(6)
    void patch_existingRole_returns200() {
        Map<String, Object> patch = Map.of("status", "Active", "statusReason", "Verified");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(patch, adminJsonHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(BASE + "/" + createdId, HttpMethod.PATCH, request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("status", "Active");
    }

    @Test
    @Order(7)
    void delete_existingRole_returns204() {
        HttpEntity<?> request = new HttpEntity<>(adminHeaders());

        ResponseEntity<Void> response = restTemplate.exchange(BASE + "/" + createdId, HttpMethod.DELETE, request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @Order(8)
    void delete_notFound_returns404() {
        HttpEntity<?> request = new HttpEntity<>(adminHeaders());

        ResponseEntity<Map> response = restTemplate.exchange(BASE + "/nonexistent-id", HttpMethod.DELETE, request, Map.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
