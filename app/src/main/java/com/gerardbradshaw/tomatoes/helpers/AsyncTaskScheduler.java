package com.gerardbradshaw.tomatoes.helpers;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.gerardbradshaw.tomatoes.pojos.AsyncTaskHolder;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskScheduler {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private List<AsyncTaskHolder> taskList = new ArrayList<>();
  private boolean taskRunning;


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public AsyncTaskScheduler() {
  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  public void addNewTask(AsyncTask task, Object[] params) {
    AsyncTaskHolder taskHolder = new AsyncTaskHolder(task, params);
    taskList.add(taskHolder);
    executeNextTask();
  }

  public void addNewTask(AsyncTask task) {
    AsyncTaskHolder taskHolder = new AsyncTaskHolder(task);
    taskList.add(taskHolder);
    executeNextTask();
  }

  public void addNewTask(AsyncTaskHolder taskHolder) {
    taskList.add(taskHolder);
    executeNextTask();
  }

  public void addNewPriorityTask(AsyncTask task, Object[] params) {
    AsyncTaskHolder taskHolder = new AsyncTaskHolder(task, params);
    taskList.add(0, taskHolder);
    executeNextTask();
  }

  public void addNewPriorityTask(AsyncTaskHolder taskHolder) {
    taskList.add(0, taskHolder);
    executeNextTask();
  }

  private void executeNextTask() {
    if (!taskRunning && taskList.size() > 0) {
      taskRunning = true;
      AsyncTaskHolder taskHolder = taskList.get(0);
      AsyncTask task = taskHolder.getTask();
      Object[] params = taskHolder.getParams();
      taskList.remove(0);

      if (params == null) {
        task.execute();
      } else {
        task.execute(params);
      }
    }
  }

  public void setTaskFinished() {
    taskRunning = false;
    executeNextTask();
  }

}
