package fr.wildcodeschool.metro;

import android.location.Location;

public class Singleton {

    private static Singleton instance;
    private Settings settings;

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
    private Singleton(){
    }

    public void initiateSettings(int zoom, boolean dropOff, Location lastKnownLocation, boolean init, boolean changeActivity, boolean theme) {
        settings = new Settings(zoom, dropOff, lastKnownLocation, init, changeActivity, theme);
    }

    public Settings getSettings() {
        return settings;
    }

    public void setZoom(int zoom) {
        settings.setZoom(zoom);
    }


    public void setTheme(boolean theme) {
        settings.setTheme(theme);
    }

    public void setDropOff(boolean dropOff){
        settings.setDropOff(dropOff);
    }

    public void setLocation(Location location) {
        settings.setLocation(location);
    }

    public void setInit(boolean init) {
        settings.setInit(init);
    }

}
