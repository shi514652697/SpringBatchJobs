package com.negi.batch.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Configuration;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;

@Configuration
public class Configurations {
	
	public LockProvider lockProvider(DataSource datasource)
	{
		return new JdbcTemplateLockProvider(datasource, "LOCK_TABLE");
	}

}
