package main;

import main.dto.GameDto;
import main.dto.PredictionDto;
import main.entity.GameEntity;
import main.request.CreateGameRequest;
import main.request.GameUpdateRequest;
import main.service.GameService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/games")
public class GameResource {
    private final GameService gameService;

    @Inject
    public GameResource(GameService gameService) {
        this.gameService = gameService;
    }

    @POST
    @Path("/")
    @Produces("application/json")
    @Consumes("application/json")
    @Operation(summary = "Create new game")
    @RolesAllowed({"app-admin"})
    public Response postMatch(CreateGameRequest createGameRequest){
        Optional<GameEntity> optionalResponse = gameService.saveNewGame(createGameRequest);
        if (optionalResponse.isPresent()) {
            return Response.ok(optionalResponse.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/all")
    @Produces("application/json")
    @Operation(summary = "Get all games from the database.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getGames() {
        return Response.ok(this.gameService.getAllGames()).build();
    }

    //Get Game By UUID
    @GET
    @Path("/game")
    @Produces("application/json")
    @Operation(summary = "Get a specific game by uuid")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getGameByUuid(@QueryParam("uuid") UUID uuid) {
        Optional<GameDto> responseDto = this.gameService.getGameByUuid(uuid);
        if (responseDto.isPresent()) {
            return Response.ok(responseDto.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/user")
    @Produces("application/json")
    @Operation(summary = "Get all games of a specific user that are not predicted by him.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getUnpredictedGamesByUser(@QueryParam("clientUuid") UUID clientUuid) {
        return Response.ok(this.gameService.getUnpredictedGamesByUser(clientUuid)).build();
    }

    //Get All Games of a specific user that are predicted by him.
    @GET
    @Path("/predicted/user")
    @Produces("application/json")
    @Operation(summary = "Get all games of a specific user that are predicted by him.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getPredictedGamesByUser(@QueryParam("clientUuid") UUID clientUuid) {
        return Response.ok(this.gameService.getPredictedGamesByUser(clientUuid)).build();
    }

    //Update Game
    @PUT
    @Path("/update")
    @Produces("application/json")
    @Consumes("application/json")
    @RolesAllowed("app-admin")
    @Operation(summary = "Update a game", description = "If a result is give, the game is finished and the result is saved. All Predictions will be analyzed and the points will be calculated.")
    public Response updateGameResult(GameUpdateRequest gameDto) {
        Optional<GameDto> optionalResponse = this.gameService.updateGame(gameDto);
        if (optionalResponse.isPresent()) {
            return Response.ok(optionalResponse.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


}
