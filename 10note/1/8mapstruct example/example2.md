@Configuration
public interface VehicleLocationToVehicleLocationResponseMapper extends BaseMapper<VehicleLocation, VehicleLocationResponse> {

    @Named("mapToResponse")
    @Bean
    public VehicleLocationResponse mapToResponse(VehicleLocation source) {
        if (source == null) return null;

        String distanceStr = source.getDistanceKm() == null
                ? null
                : (source.getDistanceKm() + " km");

        return VehicleLocationResponse.builder()
                .vehicleName(source.getVehicleName())
                .averageDistance(distanceStr)
                .point(source.getPoint())
                .hash(source.getHash())
                .build();
    }


    @Bean
    public VehicleLocationToVehicleLocationResponseMapper initialize() {
        return Mappers.getMapper(VehicleLocationToVehicleLocationResponseMapper.class);
    }
}
