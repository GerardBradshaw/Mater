package com.gerardbradshaw.tomatoes.helpers;

import android.os.AsyncTask;

public class AsyncTaskHolder {

  private AsyncTask task;
  private Object[] params;
  private Object[] progress;
  private Object[] result;

  public AsyncTaskHolder(AsyncTask task, Object[] params, Object [] progress, Object[] result) {
    this.task = task;
    this.params = params;
    this.progress = progress;
    this.result = result;
  }

  public AsyncTask getTask() {
    return task;
  }

  public Object[] getParams() {
    return params;
  }

  public Object[] getProgress() {
    return progress;
  }

  public Object[] getResult() {
    return result;
  }
}
