package com.negi.batch.file;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.negi.batch.model.Product;

@Configuration
public class BatchFileConfig {
	
	
	@Autowired
	private StepBuilderFactory sbf;
	
	@Autowired
	private JobBuilderFactory jbf;
	
	@Bean
	public Job job1()
	{
		return jbf.get("myJob").incrementer(new RunIdIncrementer()).start(step1()).build();	
	}
	
	
	
	@Bean
	public Step step1()
	{
		return sbf.get("Step1")
				.<Product,Product>chunk(1)
				.reader(reader1())
				.processor(processor1())
				.writer(writer1())
				.build();
	}
	
	
	
	@Bean
	public ItemReader<Product> reader1()
	{
		FlatFileItemReader<Product> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new ClassPathResource("products.csv"));
		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
		
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames("id","name","description","price");
		BeanWrapperFieldSetMapper<Product> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Product.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		flatFileItemReader.setLineMapper(lineMapper);
		return flatFileItemReader;
	}
	
	@Bean
	public ItemProcessor<Product, Product> processor1()
	{
		return ( p) ->{ p.setPrice(p.getPrice() - p.getPrice()*10/100);
		return p;};
		
	}
	
	@Bean
	public ItemWriter<Product> writer1()
	{
		JdbcBatchItemWriter<Product> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		jdbcBatchItemWriter.setDataSource(dataSource());
		jdbcBatchItemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Product>());
		jdbcBatchItemWriter.setSql("Insert into product (id, name,description,proce) values (:id,:name,:description,:price) ");
		return null;
		
	}
	
	@Bean
	public DataSource dataSource()
	{
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.driver");
		dataSource.setUrl("jdbc:mysql://localhost:8306/mydb");
		dataSource.setUsername("username");
		dataSource.setPassword("negi");
		return dataSource;
	}

}
