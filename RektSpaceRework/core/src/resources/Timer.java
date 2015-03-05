package resources;

/**
 * Created by Jarbsku on 11.2.2015.
 */
public class Timer {

    private boolean running;
    private long time;


    public Timer(){
        running = false;
    }

    public void start(){
        time = System.currentTimeMillis();
        running = true;
    }

    public void pause(){
        running = false;
    }

    public long getElapsed(){
        return System.currentTimeMillis() - time;
    }

    public boolean isPassed(float millis){
        return getElapsed() > millis;
    }

    public boolean isRunning(){
        return running;
    }

}