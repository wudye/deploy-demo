@Mapper(componentModel = "spring")
public interface VehicleLocationToVehicleLocationResponseMapper extends BaseMapper<VehicleLocation, VehicleLocationResponse> {

    @Named("mapToResponse")
    default VehicleLocationResponse mapToResponse(VehicleLocation source) {
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

 
}