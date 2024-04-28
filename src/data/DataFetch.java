package data;

public class DataFetch implements Runnable {

    WeatherStation weatherStation;

    public DataFetch(WeatherStation weatherStation) {
        this.weatherStation = weatherStation;
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException e) {
                System.out.println(e);
            }
            weatherStation.getSME280Data();
        }
    }
}
