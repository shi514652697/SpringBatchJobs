package com.negi.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class MyJobListner implements JobExecutionListener {

	@Override
	public void afterJob(JobExecution arg0) {
      System.out.println("Job Started");
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Job Ended with " +jobExecution.getStatus());
	}

}
