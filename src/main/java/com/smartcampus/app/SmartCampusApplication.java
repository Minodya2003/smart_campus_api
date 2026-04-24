package com.smartcampus.app;

import org.glassfish.jersey.server.ResourceConfig;

public class SmartCampusApplication extends ResourceConfig {
    public SmartCampusApplication() {
        packages("com.smartcampus.api.endpoints", "com.smartcampus.infrastructure.web");
    }
}
