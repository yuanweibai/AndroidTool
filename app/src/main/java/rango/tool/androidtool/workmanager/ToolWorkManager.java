package rango.tool.androidtool.workmanager;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import androidx.work.Configuration;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import rango.tool.androidtool.ToolApplication;
import rango.tool.common.utils.FileUtils;
import rango.tool.common.utils.TimeUtils;

public class ToolWorkManager {

    public static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WorkManager/log.txt";
    private WorkManager workManager;
    private static ToolWorkManager instance;

    private ToolWorkManager() {
        workManager = WorkManager.getInstance();
    }

    public static ToolWorkManager getInstance() {
        if (instance == null) {
            synchronized (ToolWorkManager.class) {
                if (instance == null) {
                    instance = new ToolWorkManager();
                }
            }
        }
        return instance;
    }

    public static void init() {
        WorkManager.initialize(ToolApplication.getContext(), new Configuration.Builder().build());
    }

    public void testDelay() {
        String msg = "start: type = work_delay, start_time = " + TimeUtils.getCurrentTime() + ", delay_time = 10s;\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_DELAY,
                OneTimeWorker.class,
                10,
                TimeUnit.SECONDS).build();
        workManager.enqueue(request);
    }

    public void testPeriodic() {
        String msg = "start: type = work_periodic, start_time = " + TimeUtils.getCurrentTime() + ", periodic_time = 15m;\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_PERIODIC,
                PeriodicWorker.class,
                15,
                TimeUnit.MINUTES).build();
        workManager.enqueue(request);
    }

    public void testPeriodicNoFirst() {
        String msg = "start: type = work_periodic, start_time = " + TimeUtils.getCurrentTime() + ", periodic_time = 15m, but first not do!!!\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_PERIODIC,
                FirstNoWorkPeriodicWorker.class,
                15,
                TimeUnit.MINUTES).build();
        workManager.enqueue(request);
    }

    public void cancelWorkByTag(String tag) {
        workManager.cancelAllWorkByTag(tag);
    }

    public void writeMsg(String msg) {
        msg += "\n";
        FileUtils.writeFile(LOG_FILE_PATH, msg, true);
    }

    public void clearMsg() {
        FileUtils.clearFile(LOG_FILE_PATH);
    }

    public StringBuilder readMsg() {
        return FileUtils.readFile(LOG_FILE_PATH);
    }

    public final static class Builder {
        private long duration;
        private TimeUnit timeUnit;
        private @NonNull Class<? extends Worker> workerClass;
        private WorkType currentType;
        private String tag;

        public enum WorkType {
            WORK_DELAY,
            WORK_PERIODIC
        }

        public Builder(@NonNull WorkType type, @NonNull Class<? extends Worker> workerClass, long duration, @NonNull TimeUnit timeUnit) {
            this.currentType = type;
            this.workerClass = workerClass;
            this.duration = duration;
            this.timeUnit = timeUnit;
        }

        public Builder addTag(String tag) {
            this.tag = tag;
            return this;
        }

        public WorkRequest build() {
            if (currentType == WorkType.WORK_PERIODIC) {
                PeriodicWorkRequest.Builder periodicBuilder = new PeriodicWorkRequest.Builder(workerClass, duration, timeUnit);
                if (!TextUtils.isEmpty(tag)) {
                    periodicBuilder.addTag(tag);
                }
                return periodicBuilder.build();
            } else {
                OneTimeWorkRequest.Builder oneTimeBuilder = new OneTimeWorkRequest.Builder(workerClass);
                oneTimeBuilder.setInitialDelay(duration, timeUnit);
                if (!TextUtils.isEmpty(tag)) {
                    oneTimeBuilder.addTag(tag);
                }
                return oneTimeBuilder.build();
            }
        }
    }
}
