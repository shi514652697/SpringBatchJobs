package com.negi.batch;

import org.springframework.batch.item.ItemProcessor;

public class JobProcessor implements ItemProcessor<String, String> {

	@Override
	public String process(String item) throws Exception {
		System.out.println("Inside Processor");
		return "PROCESSED "+ item.toUpperCase();
	}

}
