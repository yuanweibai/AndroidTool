package rango.tool.androidtool.alive;

public class AliveManager {

    private AliveManager() {
    }

    private static class ClassHolder {
        private static final AliveManager INSTANCE = new AliveManager();
    }

    public static AliveManager getInstance() {
        return ClassHolder.INSTANCE;
    }


    private boolean isDownloadServiceAlive = false;

    public void setDownloadServiceAlive(boolean isAlive) {
        this.isDownloadServiceAlive = isAlive;
    }

    public boolean isDownloadServiceAlive() {
        return isDownloadServiceAlive;
    }

}
