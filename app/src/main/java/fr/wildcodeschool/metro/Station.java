package fr.wildcodeschool.metro;

public class Station {

    private int number;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private int availableStands;
    private int availableBike;
    private String status;

    public Station(int number, String name, String address, double latitude, double longitude, int availableStands, int availableBike, String  status) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.availableStands = availableStands;
        this.availableBike = availableBike;
        this.status = status;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        longitude = longitude;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        address = address;
    }

    public int getAvailableStands() {
        return availableStands;
    }

    public int getAvailableBike() {
        return availableBike;
    }



    public void setAvailableStands(int availableStands) {
        this.availableStands = availableStands;
    }

    public void setAvailableBike(int availableBike) {
        this.availableBike = availableBike;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
