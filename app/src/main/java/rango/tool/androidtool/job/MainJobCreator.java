package rango.tool.androidtool.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class MainJobCreator implements JobCreator {
    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case ToolEventJob.TAG:
                return new ToolEventJob();
            default:
                return null;
        }
    }
}
