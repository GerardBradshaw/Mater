package com.gerardbradshaw.tomatoes.helpers;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class AsyncTaskScheduler {

  // - - - - - - - - - - - - - - - Member variables - - - - - - - - - - - - - - -

  private List<AsyncTaskHolder> taskList = new ArrayList<>();


  // - - - - - - - - - - - - - - - Constructor - - - - - - - - - - - - - - -

  public AsyncTaskScheduler() {

  }


  // - - - - - - - - - - - - - - - Public methods - - - - - - - - - - - - - - -

  public void addNewTask(AsyncTask task, Object[] params, Object [] progress, Object[] result) {
    AsyncTaskHolder taskHolder = new AsyncTaskHolder(task, params, progress, result);
    taskList.add(taskHolder);
    executeNextTask();
  }

  public void addNewTask(AsyncTaskHolder taskHolder) {
    taskList.add(taskHolder);
    executeNextTask();
  }

  public void addNewPriorityTask(AsyncTask task, Object[] params, Object [] progress, Object[] result) {
    AsyncTaskHolder taskHolder = new AsyncTaskHolder(task, params, progress, result);
    taskList.add(0, taskHolder);
    executeNextTask();
  }

  public void addNewPriorityTask(AsyncTaskHolder taskHolder) {
    taskList.add(0, taskHolder);
    executeNextTask();
  }

  public void executeNextTask() {
    if (taskList.size() > 0) {
      AsyncTaskHolder taskHolder = taskList.get(0);
      AsyncTask task = taskHolder.getTask();
      Object[] params = taskHolder.getParams();
      Object[] progress = taskHolder.getProgress();
      Object[] result = taskHolder.getProgress();
      taskList.remove(0);
      task.execute(params, progress, result);
    }
  }

}
