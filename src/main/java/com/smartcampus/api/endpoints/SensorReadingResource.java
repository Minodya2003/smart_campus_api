package com.smartcampus.api.endpoints;
import com.smartcampus.api.response.ErrorResponse;
import com.smartcampus.core.domain.room.Room;
import com.smartcampus.core.domain.sensor.Sensor;
import com.smartcampus.core.domain.reading.SensorReading;
import com.smartcampus.infrastructure.persistence.DataStore;
import com.smartcampus.core.errors.SensorUnavailableException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;

public class SensorReadingResource {
    private String sensorId;
    private DataStore store = DataStore.getInstance();

    public SensorReadingResource(String sensorId) { this.sensorId = sensorId; }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postReading(SensorReading reading) {
        Sensor s = store.getSensors().get(sensorId);
        if (s == null) {
            return Response.status(404)
                    .entity(new ErrorResponse(404, "Sensor not found: " + sensorId))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if ("MAINTENANCE".equals(s.getStatus()) || "OFFLINE".equals(s.getStatus())) {
            throw new SensorUnavailableException(
                    "Sensor '" + sensorId + "' is under maintenance and cannot accept new readings.");
        }

        s.setCurrentValue(reading.getValue());
        store.getReadings().computeIfAbsent(sensorId, k -> Collections.synchronizedList(new ArrayList<>())).add(reading);
        return Response.status(201).entity(reading).type(MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHistory() {
        List<SensorReading> history = store.getReadings().getOrDefault(sensorId, new ArrayList<>());
        return Response.ok(new ArrayList<>(history)).build();
    }
}
