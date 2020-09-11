package com.negi.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import net.javacrumbs.shedlock.core.SchedulerLock;

@Configuration
public class BatchConfig {
	
	@Autowired
	JobLauncher jobLauncher;
	
	
	@Autowired
	private StepBuilderFactory sbf;
	
	@Autowired
	private JobBuilderFactory jbf;
	
	@Bean
	public Reader reader()
	{
		return new Reader();
	}
	
	@Bean
	public Writer writer()
	{
		return new Writer();
	}
	@Bean
	public Processor processor()
	{
		return new Processor();
	}
	
	@Bean
	public MyJobListner listener()
	{
		return new MyJobListner();
	}
	
	@Bean
	public Step step()
	{
		return sbf.get("Step1")
				.<String,String>chunk(1)
				.reader(reader())
				.processor(processor())
				.writer(writer()).build();
	}
	
	@Bean
	public Job job()
	{
		return jbf.get("job")
				.incrementer(new RunIdIncrementer())
				.listener(listener())
				.start(step())
				.build();
	}
	
	@Scheduled(cron="0 0/2 * * * ?", zone="CST")
	//@SchedulerLock(name="jobname", lockAtLeastFor= "PT5M", lockAtMostFor="PT5M" )
	public void executeJob() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException
	
	{
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		
		JobExecution execution = jobLauncher.run(job(), jobParameters);
	}
	
	

}
