package main;

import main.response.ApplicationInfoResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/info")
//Swagger Documentation
@OpenAPIDefinition(
        info = @org.eclipse.microprofile.openapi.annotations.info.Info(
                title = "Application Info",
                version = "1.0.0",
                description = "This is the application info endpoint."
        )
)
@Tag(name = "Application Info", description = "This will be disabled on production.")
public class ApplicationInfoResource {
    private static final Logger logger = Logger.getLogger(ApplicationInfoResource.class);

    @ConfigProperty(name = "quarkus.application.version")
    String version;
    @ConfigProperty(name = "quarkus.datasource.db-kind")
    String dbKind;
    @ConfigProperty(name = "quarkus.datasource.jdbc.url")
    String dbUrl;



    @GET
    @Path("/version")
    @Produces("application/json")
    @Operation(summary = "Get the version of the application.")
    public Response getVersion() {
        ApplicationInfoResponse applicationInfoResponse = new ApplicationInfoResponse(version, dbKind, dbUrl);
        logger.info("Returning version: " + applicationInfoResponse);
        return Response.ok(applicationInfoResponse).build();
    }
}
