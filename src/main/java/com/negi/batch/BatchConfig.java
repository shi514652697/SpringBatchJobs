package com.negi.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {
	
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

}
