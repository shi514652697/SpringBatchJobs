package com.negi.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class JobWriter implements ItemWriter<String> {

	@Override
	public void write(List<? extends String> items) throws Exception {
	
		System.out.println("Writing Data"+items);
		
	}

}
