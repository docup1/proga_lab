package Managers;

import Model.FuelType;
import Model.VehicleType;
public class CastManager {
    public static VehicleType castToVehicleType(String str){
        for (VehicleType value: VehicleType.values()){
            if (value.name().equalsIgnoreCase(str)){
                return value;
            }
        }
        return VehicleType.DRONE;
    }
    public static FuelType castToFuelType(String str){
        for (FuelType value: FuelType.values()){
            if (value.name().equalsIgnoreCase(str)){
                return value;
            }
        }
        return FuelType.PLASMA;
    }
}
