package Walter;

//currently unused
public class UserInfo {
    private long totalTime;
    private long lastTime;

    public UserInfo() {
        totalTime = 0;
        lastTime = 0;
    }

    public UserInfo(long time) {
        totalTime = 0;
        lastTime = time;
    }

    //returns true if lastTime was 0, meaning no hole in the logs
    boolean connect(long time) {
        boolean valid = true;
        if (lastTime != 0) valid = false;
        lastTime = time;
        return valid;
    }

    //returns true if there has been a connection time to this disconnection time
    boolean disconnect(long time) {
        if (lastTime != 0) {
            long tempTime = time - lastTime;
            if (tempTime > 0) {
                totalTime += tempTime;
            } else {
                lastTime = 0;
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    long getTotalTime() {
        return totalTime;
    }
}
