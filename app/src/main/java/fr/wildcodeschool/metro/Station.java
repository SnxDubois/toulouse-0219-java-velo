package fr.wildcodeschool.metro;

public class Station {

    int StationNumber;
    String StationName;
    String StaitonAdress;
    double StationLatitude;
    double StationLongitude;


    public Station(int stationNumber, String stationName, String staitonAdress, double stationLatitude, double stationLongitude) {
        StationNumber = stationNumber;
        StationName = stationName;
        StaitonAdress = staitonAdress;
        StationLatitude = stationLatitude;
        StationLongitude = stationLongitude;
    }

    public int getStationNumber() {
        return StationNumber;
    }

    public String getStationName() {
        return StationName;
    }

    public String getStaitonAdress() {
        return StaitonAdress;
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

    public void setStaitonAdress(String staitonAdress) {
        StaitonAdress = staitonAdress;
    }

    public void setStationLatitude(double stationLatitude) {
        StationLatitude = stationLatitude;
    }

    public void setStationLongitude(double stationLongitude) {
        StationLongitude = stationLongitude;
    }
}
