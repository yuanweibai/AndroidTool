package rango.tool.androidtool.workmanager;

import androidx.work.Worker;

public abstract class BaseWorker extends Worker {

    public String getTag() {
        return null;
    }
}
