package fr.wildcodeschool.metro;

public class Station {

    int stationNumber;
    String stationName;
    String stationAddress;
    double stationLatitude;
    double stationLongitude;


    public Station(int stationNumber, String stationName, String stationAddress, double stationLatitude, double stationLongitude) {
        this.stationNumber = stationNumber;
        this.stationName = stationName;
        this.stationAddress = stationAddress;
        this.stationLatitude = stationLatitude;
        this.stationLongitude = stationLongitude;
    }


    public int getStationNumber() {
        return stationNumber;
    }


    public String getStationName() {
        return stationName;
    }


    public double getStationLatitude() {
        return stationLatitude;
    }


    public double getStationLongitude() {
        return stationLongitude;
    }


    public void setStationNumber(int stationNumber) {
        stationNumber = stationNumber;
    }


    public void setStationName(String stationName) {
        stationName = stationName;
    }


    public void setStationLatitude(double stationLatitude) {
        stationLatitude = stationLatitude;
    }


    public void setStationLongitude(double stationLongitude) {
        stationLongitude = stationLongitude;
    }


    public String getStationAddress() {
        return stationAddress;
    }


    public void setStationAddress(String stationAddress) {
        stationAddress = stationAddress;
    }
}
