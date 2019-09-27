package com.gerardbradshaw.mater.helpers;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskScheduler {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private List<Runnable> runnableList = new ArrayList<>();
  private boolean taskRunning;

  private static String LOG_TAG = "GGG - AsyncTaskScheduler";


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public AsyncTaskScheduler() {
  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  public void addNewTask(Runnable runnable) {
    runnableList.add(runnable);
    executeNextTask();
  }

  public void addNewPriorityTask(Runnable runnable) {
    runnableList.add(0, runnable);
    executeNextTask();
  }

  private void executeNextTask() {
    if (!taskRunning && runnableList.size() > 0) {
      taskRunning = true;
      Log.d(LOG_TAG, "Running task");
      runnableList.get(0).run();
      runnableList.remove(0);
    }
  }

  public void setTaskFinished() {
    taskRunning = false;
    executeNextTask();
  }

}
