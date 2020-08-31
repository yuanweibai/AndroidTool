package rango.tool.androidtool.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class LifecycleFragment extends BaseFragment {

    public static String TAG;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.e(TAG,"onAttach()");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e(TAG,"onCreate()");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG,"onViewCreated()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.e(TAG,"onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG,"onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.e(TAG,"onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.e(TAG,"onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG,"onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e(TAG,"onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.e(TAG,"onDetach()");
    }
}
