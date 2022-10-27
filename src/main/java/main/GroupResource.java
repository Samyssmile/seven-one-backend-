package main;

import main.entity.GroupEntity;
import main.request.CreateGroupRequest;
import main.service.GroupService;
import org.eclipse.microprofile.openapi.annotations.Operation;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/groups")
public class GroupResource {

    private final GroupService groupService;

    @Inject
    public GroupResource(GroupService groupService) {
        this.groupService = groupService;
    }


    @GET
    @Path("/all")
    @Produces("application/json")
    @Operation(summary = "Get all groups from the database.")
    @RolesAllowed({"app-user", "app-admin"})
    public Response getGroups() {
        return Response.ok(this.groupService.getAllGroups()).build();
    }

    @POST
    @Path("/new")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Post a new group to the database.")
    @RolesAllowed({"app-admin"})
    public Response postGroup(CreateGroupRequest createGroupRequest) {
        Optional<GroupEntity> createdEntity = this.groupService.createNewGroup(createGroupRequest);
        if (createdEntity.isPresent()) {
            return Response.ok(createdEntity.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Update a group in the database.
     */
    @PUT
    @Path("/update")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Update a group in the database.")
    @RolesAllowed({"app-admin"})
    public Response updateGroup(GroupEntity groupEntity) {
        Optional<GroupEntity> updatedEntity = this.groupService.updateGroup(groupEntity);
        if (updatedEntity.isPresent()) {
            return Response.ok(updatedEntity.get()).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Delete a group from the database.
     */
    @DELETE
    @Path("/delete")
    @Consumes("application/json")
    @Produces("application/json")
    @Operation(summary = "Delete a group from the database.")
    @RolesAllowed({"app-admin"})
    public Response deleteGroup(@QueryParam("uuid") UUID uuid) {
        boolean deleted = this.groupService.deleteGroup(uuid);
        if (deleted) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }
}
