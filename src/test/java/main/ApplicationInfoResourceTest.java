package main;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.when;

@QuarkusTest
@TestHTTPEndpoint(ApplicationInfoResource.class)
class ApplicationInfoResourceTest {

    @Test
    void getApplicationInfo() {
        when().get("version").then().statusCode(HttpStatus.SC_OK);
    }
}
