package rango.tool.androidtool.job;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import rango.tool.common.utils.FileUtils;

public class MainJobCreator implements JobCreator {
    private static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/JobSchedule/log.txt";

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case ToolEventJob.TAG:
                return new ToolEventJob();
            case TestJob.TAG:
                return new TestJob();
            default:
                return null;
        }
    }

    public static void writeMsg(String msg) {
        msg += "\n";
        FileUtils.writeFile(LOG_FILE_PATH, msg, true);
    }

    public static void clearMsg() {
        FileUtils.clearFile(LOG_FILE_PATH);
    }

    public static StringBuilder readMsg() {
        return FileUtils.readFile(LOG_FILE_PATH);
    }

}
