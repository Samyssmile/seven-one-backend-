package main;

import main.request.CreateUserRequest;
import main.service.UserService;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;
import org.modelmapper.ModelMapper;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/user/")
@OpenAPIDefinition(tags = {
        @Tag(name = "User", description = "User API")
}, info = @Info(title = "User API", version = "1.0"))
public class UserResource {

    private final ModelMapper modelMapper = new ModelMapper();
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(UserResource.class);


    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }


    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create User Accoount", description = "Create user")
    @PermitAll
    public Response createUser(CreateUserRequest createUserRequest) {
        var userEntity = userService.saveNewUser(createUserRequest, false);
        logger.info("Create User Request: " + createUserRequest.getNickname() + " " + createUserRequest.getClientUuid());
        return Response.ok(userEntity).build();
    }


    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get User Account by his UUID", description = "Get user account")
    @PermitAll
    public Response getUser(@QueryParam("clientUuid") UUID clientUuid) {
        var optionalUserEntity = userService.findUserByClientUuid(clientUuid);
        logger.info("Incoming Get User Request");
        return optionalUserEntity.map(userLoginSucceedResponse -> Response.ok(userLoginSucceedResponse).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    //Get All Users
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get All Users", description = "Get all users")
    @RolesAllowed({"app-admin"})
    public Response getAllUsers() {
        logger.info("Incoming Get All Users Request");
        return Response.ok(userService.findAllUsers()).build();
    }

    @DELETE
    @Path("/delete")
    @Operation(summary = "Delete User Account by his UUID", description = "Delete user account")
    @RolesAllowed({"app-user"})
    public Response deleteUser(@QueryParam("clientUuid") UUID clientUuid) {
        logger.info("Incoming Delete User Request -" + clientUuid);
        var deleted = userService.removeUserByUuid(clientUuid);
        return deleted ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
