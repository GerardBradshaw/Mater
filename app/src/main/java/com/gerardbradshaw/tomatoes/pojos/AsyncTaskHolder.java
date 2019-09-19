package com.gerardbradshaw.tomatoes.pojos;

import android.os.AsyncTask;

public class AsyncTaskHolder {

  private AsyncTask task;
  private Object[] params;

  public AsyncTaskHolder(AsyncTask task, Object[] params) {
    this.task = task;
    this.params = params;
  }

  public AsyncTaskHolder(AsyncTask task) {
    this.task = task;
    this.params = null;
  }

  public AsyncTask getTask() {
    return task;
  }

  public Object[] getParams() {
    return params;
  }

}
