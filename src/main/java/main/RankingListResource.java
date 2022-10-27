package main;

import main.dto.UserDto;
import main.service.UserService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/ranking")
public class RankingListResource {

    private final UserService userService;

    @Inject
    public RankingListResource(UserService userService) {
        this.userService = userService;
    }

    //Get Ranking List
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Ranking List", description = "Get ranking list" )
    @RolesAllowed({"app-user", "app-admin"})
    public Response getRankingList() {
        List<UserDto> rankedList = this.userService.getRankingList();
        return Response.ok(rankedList).build();
    }

    @GET
    @Path("/all/cached")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Ranking List", description = "Get ranking list" )
    @RolesAllowed({"app-user", "app-admin"})
    public Response getRankingListCached() {
        List<UserDto> rankedList = this.userService.getRankingListCached();
        return Response.ok(rankedList).build();
    }

    @GET
    @Path("/all/experimental")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Ranking List", description = "Get ranking list" )
    @RolesAllowed({"app-user", "app-admin"})
    public Response getRankingListExperimental() {
        List<UserDto> rankedList = this.userService.getRankingListExperimental();
        return Response.ok(rankedList).build();
    }


}
