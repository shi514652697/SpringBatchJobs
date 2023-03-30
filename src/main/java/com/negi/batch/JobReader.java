package com.negi.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class JobReader implements ItemReader<String> {
	
	private String[] courses = {"Java","Spring","Angular"};
	private int count =0;

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		
		System.out.println("Inside Reader");
		if(count < courses.length)
		{
			return courses[count++];
		}
		else
		{
			count=0;
		}
		return null;
	}

}
