package com.smartcampus.api.endpoints;
import com.smartcampus.api.response.ErrorResponse;
import com.smartcampus.core.domain.room.Room;
import com.smartcampus.core.domain.sensor.Sensor;
import com.smartcampus.core.domain.reading.SensorReading;
import com.smartcampus.infrastructure.persistence.DataStore;
import com.smartcampus.core.errors.LinkedResourceNotFoundException;
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
        if (!store.getRooms().containsKey(s.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room '" + s.getRoomId() + "' does not exist.");
        }
        if (store.getSensors().containsKey(s.getId())) {
            return Response.status(409)
                    .entity(new ErrorResponse(409, "Sensor with ID '" + s.getId() + "' already exists."))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        store.getSensors().put(s.getId(), s);
        store.getRooms().get(s.getRoomId()).getSensorIds().add(s.getId());
        return Response.status(201).entity(s).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type == null) return new ArrayList<>(store.getSensors().values());
        return store.getSensors().values().stream()
                .filter(s -> s.getType().equalsIgnoreCase(type)).collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensor(@PathParam("id") String id) {
        Sensor sensor = store.getSensors().get(id);
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
        Sensor sensor = store.getSensors().get(id);
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
