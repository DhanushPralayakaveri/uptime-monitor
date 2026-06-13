package com.proj.uptimemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UptimeMonitorRun {
	public static void main(String[] args) {
		SpringApplication.run(UptimeMonitorRun.class, args);
	}
}