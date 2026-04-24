package com.smartcampus.infrastructure.persistence;

import com.smartcampus.core.domain.room.Room;
import com.smartcampus.core.domain.sensor.Sensor;
import com.smartcampus.core.domain.reading.SensorReading;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    private static final DataStore instance = new DataStore();

    private Map<String, Room> rooms = new ConcurrentHashMap<>();
    private Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    private DataStore() {}
    public static DataStore getInstance() { return instance; }

    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, Sensor> getSensors() { return sensors; }
    public Map<String, List<SensorReading>> getReadings() { return readings; }
}
