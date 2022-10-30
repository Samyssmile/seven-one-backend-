package main;

import main.dto.MatchDto;
import main.entity.MatchEntity;
import main.request.CreateGameRequest;
import main.request.GameUpdateRequest;
import main.service.MatchService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/matches")
public class MatchResource {
    private final MatchService matchService;

    @Inject
    public MatchResource(MatchService matchService) {
        this.matchService = matchService;
    }

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Create new match", description = "A match between two teams")
    @RolesAllowed({"app-admin"})
    public Response postMatch(CreateGameRequest createGameRequest){
        Optional<MatchEntity> optionalResponse = matchService.saveNewGame(createGameRequest);
        if (optionalResponse.isPresent()) {
            return Response.ok(optionalResponse.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    @Operation(summary = "Get all matches from the database.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getMatches() {
        return Response.ok(this.matchService.getAllMatches()).build();
    }

    //Get Game By UUID
    @GET
    @Path("/match")
    @Produces("application/json")
    @Operation(summary = "Get a specific match by uuid")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getMatchByMatchUuid(@QueryParam("uuid") UUID uuid) {
        Optional<MatchDto> responseDto = this.matchService.getGameByUuid(uuid);
        if (responseDto.isPresent()) {
            return Response.ok(responseDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/user")
    @Produces("application/json")
    @Operation(summary = "Get all matches of a specific user that are not predicted by him.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getUnpredictedGamesByUser(@QueryParam("clientUuid") UUID clientUuid) {
        return Response.ok(this.matchService.getUnpredictedGamesByUser(clientUuid)).build();
    }

    //Get All Games of a specific user that are predicted by him.
    @GET
    @Path("/predicted/user")
    @Produces("application/json")
    @Operation(summary = "Get all matches of a specific user that are predicted by him.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getPredictedMatchesByUser(@QueryParam("clientUuid") UUID clientUuid) {
        return Response.ok(this.matchService.getPredictedMatchesByUser(clientUuid)).build();
    }

    //Update Game
    @PUT
    @Path("/update")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed("app-admin")
    @Operation(summary = "Update a match", description = "If a result is give, the game is finished and the result is saved. All Predictions will be analyzed and the points will be calculated.")
    public Response updateGameResult(GameUpdateRequest gameDto) {
        Optional<MatchDto> optionalResponse = this.matchService.updateGame(gameDto);
        if (optionalResponse.isPresent()) {
            return Response.ok(optionalResponse.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


}
