package dev.jgregorio.demo.data.domain.center;

import dev.jgregorio.demo.data.domain.location.Location;

public class CenterServiceTestDataFactory {

    public static final Long CENTER_ID = 1L;
    public static final Long CLIENT_ID = 2L;
    public static final String CENTER_NAME = "Test Center";
    public static final String CENTER_ADDRESS = "123 Test St";
    public static final String CENTER_POSTAL_CODE = "12345";
    public static final Long LOCATION_ID = 3L;

    public static CenterCreation createCenterCreation() {
        return CenterCreation.builder()
                .clientId(CLIENT_ID)
                .name(CENTER_NAME)
                .address(CENTER_ADDRESS)
                .postalCode(CENTER_POSTAL_CODE)
                .locationId(LOCATION_ID)
                .build();
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

    public static CenterRead createCenterRead() {
        return CenterRead.builder().id(CENTER_ID).clientId(CLIENT_ID).build();
    }

    public static CenterUpdate createCenterUpdate() {
        return CenterUpdate.builder()
                .id(CENTER_ID)
                .clientId(CLIENT_ID)
                .name("Updated Name")
                .address("Updated Address")
                .postalCode("54321")
                .locationId(LOCATION_ID)
                .build();
    }

    public static CenterDelete createCenterDelete() {
        return CenterDelete.builder().id(CENTER_ID).clientId(CLIENT_ID).build();
    }

    public static CenterSearch createCenterSearch() {
        return CenterSearch.builder().name("Test").build();
    }
}
