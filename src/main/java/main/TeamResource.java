package main;

import main.request.TeamCreateRequest;
import main.service.TeamService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/teams")
public class TeamResource {

    private final TeamService teamService;
    private static final Logger logger = Logger.getLogger(TeamResource.class);

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Create Team", description = "Create team" )
    @RolesAllowed({"app-admin"})
    public Response createTeam(TeamCreateRequest teamCreateRequest) {
        logger.info("Create team request received: "+teamCreateRequest.getTeamName());
        var teamEntity = teamService.saveNewTeam(teamCreateRequest);
        return Response.ok(teamEntity).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get All Teams", description = "Get all teams" )
    @RolesAllowed({"app-user", "app-admin"})
    public Response getTeamList() {
        logger.info("Get all teams request received");
        var teamList = teamService.getTeamList();
        return Response.ok(teamList).build();
    }
}
