package main.security;


import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/jwt")

@ApplicationScoped
public class JwtResource {
    //swagger tags
    private static final String TAG = "TEST_ONLY";
    private final JwtService jwtService;

    @Inject
    public JwtResource(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GET
    @Path("/test/admin")
    @Tag(name = TAG, description = "Generate Super Admin JWT - For Testing purposes only. This is disabled on production")
    @Produces("application/json")
    public Response getJwt() {
        String jwt = jwtService.generateSuperAdminJwt();
        return Response.ok(new JwtDto(jwt)).build();
    }


}
