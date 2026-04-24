package com.smartcampus.api.endpoints;
import com.smartcampus.api.response.ErrorResponse;
import com.smartcampus.core.domain.room.Room;
import com.smartcampus.infrastructure.persistence.DataStore;
import com.smartcampus.core.errors.RoomNotEmptyException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/rooms")
public class RoomResource {
    private DataStore store = DataStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getRooms() { return new ArrayList<>(store.getRooms().values()); }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRoom(Room room) {
        if (store.getRooms().containsKey(room.getId())) {
            return Response.status(409)
                    .entity(new ErrorResponse(409, "Room with ID '" + room.getId() + "' already exists."))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        store.getRooms().put(room.getId(), room);
        return Response.status(201).entity(room).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoom(@PathParam("id") String id) {
        Room room = store.getRooms().get(id);
        if (room == null) {
            return Response.status(404)
                    .entity(new ErrorResponse(404, "Room not found: " + id))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("id") String id) {
        Room r = store.getRooms().get(id);
        if (r == null) {
            return Response.status(404)
                    .entity(new ErrorResponse(404, "Room not found: " + id))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (!r.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Room cannot be deleted because it still contains sensors: " + r.getSensorIds());
        }
        store.getRooms().remove(id);
        return Response.ok(new ErrorResponse(200, "Room '" + id + "' deleted successfully."))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
