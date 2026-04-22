package com.smartcampus.resource;
import com.smartcampus.dto.ErrorResponse;
import com.smartcampus.model.*;
import com.smartcampus.store.DataStore;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

@Path("/sensors")
public class SensorResource {
    private DataStore store = DataStore.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSensor(Sensor s) {
        if (!store.rooms.containsKey(s.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room '" + s.getRoomId() + "' does not exist.");
        }
        store.sensors.put(s.getId(), s);
        store.rooms.get(s.getRoomId()).getSensorIds().add(s.getId());
        return Response.status(201).entity(s).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type == null) return new ArrayList<>(store.sensors.values());
        return store.sensors.values().stream()
                .filter(s -> s.getType().equalsIgnoreCase(type)).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensor(@PathParam("id") String id) {
        Sensor sensor = store.sensors.get(id);
        if (sensor == null) {
            return Response.status(404)
                    .entity(new ErrorResponse(404, "Sensor not found: " + id))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        return Response.ok(sensor).build();
    }

    @Path("/{id}/readings")
    public SensorReadingResource getReadings(@PathParam("id") String id) {
        Sensor sensor = store.sensors.get(id);
        if (sensor == null) {
            throw new NotFoundException(
                    Response.status(404)
                            .entity(new ErrorResponse(404, "Sensor not found: " + id))
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
        return new SensorReadingResource(id);
    }
}
