package main;

import io.quarkus.security.UnauthorizedException;
import main.dto.PredictionDto;
import main.entity.PredictionEntity;
import main.service.PredictionService;
import main.utility.Utility;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Path("/predictions")
public class PredictionResource {

    private static final Logger logger = Logger.getLogger(PredictionResource.class);

    private final Random random = new Random();
    private final PredictionService predictionService;

    @Inject
    public PredictionResource(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @GET
    @Path("/")
    @Produces("application/json")
    @Operation(summary = "Get all predictions of specific user.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getPredictionDtosByUUID(@QueryParam("clientUuid") UUID clientUuid) {
        return Response.ok(predictionService.findAllPredictionsByClientUuid(clientUuid)).build();
    }

    @POST
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Make new prediction to an upcaming game.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response makePrediction(PredictionDto predictionDto) {
        try {
            Optional<PredictionEntity> optionalEntity = this.predictionService.makePrediction(predictionDto);
            if (optionalEntity.isPresent()) {
                return Response.ok(optionalEntity.get()).build();
            } else {
                logger.warn("Bad request for prediction: "+predictionDto.getPrediction()+"; Game "+predictionDto.getGameUuid());
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }catch (UnauthorizedException unauthorizedException){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

    }

    @PUT
    @Path("/")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Update prediction to an upcaming game.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response updatePrediction(PredictionDto predictionDto) {
        return Response.ok(predictionDto).build();
    }


    //Get All Predictions
    @GET
    @Path("/all")
    @Produces("application/json")
    @Operation(summary = "Get all predictions.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getAllPredictions() {
        return Response.ok(predictionService.findAllPredictions()).build();
    }

    private PredictionDto generateRandomPredictionDto() {
        return new PredictionDto(UUID.randomUUID(), Utility.getRandoomGameUUID(),random.nextInt(10)+"-"+random.nextInt(10) );
    }

    }
