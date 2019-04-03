package fr.wildcodeschool.metro;

public class Station {

    int StationNumber;
    String StationName;
    String StationAddress;
    double StationLatitude;
    double StationLongitude;


    public Station(int stationNumber, String stationName, String stationAddress, double stationLatitude, double stationLongitude) {
        this.StationNumber = stationNumber;
        this.StationName = stationName;
        this.StationAddress = stationAddress;
        this.StationLatitude = stationLatitude;
        this.StationLongitude = stationLongitude;
    }

    public int getStationNumber() {
        return StationNumber;
    }


    public String getStationName() {
        return StationName;
    }



    public double getStationLatitude() {
        return StationLatitude;
    }

    public double getStationLongitude() {
        return StationLongitude;
    }

    public void setStationNumber(int stationNumber) {
        StationNumber = stationNumber;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }



    public void setStationLatitude(double stationLatitude) {
        StationLatitude = stationLatitude;
    }

    public void setStationLongitude(double stationLongitude) {
        StationLongitude = stationLongitude;
    }

    public String getStationAddress() {
        return StationAddress;
    }

    public void setStationAddress(String stationAddress) {
        StationAddress = stationAddress;
    }
}
