package rango.tool.androidtool.workmanager;

import android.os.Environment;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
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

    public void testDelay() {
        String msg = "start: type = work_delay_1, start_time = " + TimeUtils.getCurrentTime() + ", delay_time = 120s;\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_DELAY,
                OneTimeWorker.class,
                120,
                TimeUnit.SECONDS)
                .build();
        workManager.enqueue(request);
    }

    public void testDelay2() {
        String msg = "start: type = work_delay_2, start_time = " + TimeUtils.getCurrentTime() + ", delay_time = 14s;\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_DELAY,
                OneTimeWorker2.class,
                14,
                TimeUnit.SECONDS)
                .build();
        workManager.enqueue(request);
    }

    public void testDelay3() {
        String msg = "start: type = work_delay_3, start_time = " + TimeUtils.getCurrentTime() + ", delay_time = 1day;\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_DELAY,
                OneTimeWorker3.class,
                1,
                TimeUnit.DAYS)
                .build();
        workManager.enqueue(request);
    }

    public void testPeriodic() {
        String msg = "start: type = work_periodic_1, start_time = " + TimeUtils.getCurrentTime() + ", periodic_time = 15m;\n";
        Log.d(PeriodicWorker.TAG,msg);
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_PERIODIC,
                PeriodicWorker.class,
                15,
                TimeUnit.MINUTES)
                .addTag(PeriodicWorker.WORKER_TAG)
                .build();
        workManager.enqueue(request);
    }

    public void testPeriodic2() {
        String msg = "start: type = work_periodic_2, start_time = " + TimeUtils.getCurrentTime() + ", periodic_time = 12h!!!\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_PERIODIC,
                PeriodicWorker2.class,
                12,
                TimeUnit.HOURS)
                .addTag(PeriodicWorker2.WORKER_TAG)
                .build();
        workManager.enqueue(request);
    }

    public void testPeriodic3() {
        String msg = "start: type = work_periodic_3, start_time = " + TimeUtils.getCurrentTime() + ", periodic_time = 1day!!!\n";
        writeMsg(msg);
        WorkRequest request = new Builder(Builder.WorkType.WORK_PERIODIC,
                PeriodicWorker3.class,
                1,
                TimeUnit.DAYS)
                .addTag(PeriodicWorker3.WORKER_TAG)
                .build();
        workManager.enqueue(request);
    }

    public void cancelWorkByTag(String tag) {
        workManager.cancelAllWorkByTag(tag);
    }

    public void cancelAllWork() {
        workManager.cancelAllWork();
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
        private Data data;

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

        public Builder setInputData(Data data) {
            this.data = data;
            return this;
        }

        public WorkRequest build() {
            if (currentType == WorkType.WORK_PERIODIC) {
                PeriodicWorkRequest.Builder periodicBuilder = new PeriodicWorkRequest.Builder(workerClass, duration, timeUnit, 6, TimeUnit.MINUTES);
                if (!TextUtils.isEmpty(tag)) {
                    periodicBuilder.addTag(tag);
                }
                if (data != null) {
                    periodicBuilder.setInputData(data);
                }
                return periodicBuilder.build();
            } else {
                OneTimeWorkRequest.Builder oneTimeBuilder = new OneTimeWorkRequest.Builder(workerClass);
                oneTimeBuilder.setInitialDelay(duration, timeUnit);
                if (!TextUtils.isEmpty(tag)) {
                    oneTimeBuilder.addTag(tag);
                }
                if (data != null) {
                    oneTimeBuilder.setInputData(data);
                }
                return oneTimeBuilder.build();
            }
        }
    }
}
