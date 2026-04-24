package dev.jgregorio.demo.data.infrastructure.api.center;

import dev.jgregorio.demo.data.domain.center.*;
import dev.jgregorio.demo.data.domain.location.Location;
import dev.jgregorio.demo.data.infrastructure.api.center.create.CenterCreationRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.delete.CenterDeleteRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.read.CenterReadRequest;
import dev.jgregorio.demo.data.infrastructure.api.center.update.CenterUpdateRequest;

public class CenterControllerTestDataFactory {

    public static final Long CENTER_ID = 1L;
    public static final Long CLIENT_ID = 2L;
    public static final String CENTER_NAME = "Center A";
    public static final String CENTER_ADDRESS = "123 Main St";
    public static final String CENTER_POSTAL_CODE = "City";
    public static final Long LOCATION_ID = 3L;

    public static CenterReadRequest createCenterReadRequest() {
        return CenterReadRequest.builder().id(CENTER_ID).clientId(CLIENT_ID).build();
    }

    public static CenterRead createCenterRead() {
        return CenterRead.builder().id(CENTER_ID).clientId(CLIENT_ID).build();
    }

    public static Center createCenter() {
        return Center.builder()
                .id(CENTER_ID)
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .location(Location.from(LOCATION_ID))
                .build();
    }

    public static CenterResponse createCenterResponse() {
        return CenterResponse.builder()
                .id(CENTER_ID)
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .location(Location.from(LOCATION_ID))
                .build();
    }

    public static CenterCreationRequest createCenterCreationRequest() {
        return CenterCreationRequest.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();
    }

    public static CenterCreation createCenterCreation() {
        return CenterCreation.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();
    }

    public static CenterUpdateRequest createCenterUpdateRequest() {
        return CenterUpdateRequest.builder()
                .id(CENTER_ID)
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();
    }

    public static CenterUpdate createCenterUpdate() {
        return CenterUpdate.builder()
                .id(CENTER_ID)
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();
    }

    public static CenterDeleteRequest createCenterDeleteRequest() {
        return CenterDeleteRequest.builder().id(CENTER_ID).clientId(CLIENT_ID).build();
    }

    public static CenterDelete createCenterDelete() {
        return CenterDelete.builder().id(CENTER_ID).clientId(CLIENT_ID).build();
    }
}
