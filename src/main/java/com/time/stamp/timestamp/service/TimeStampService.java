package com.time.stamp.timestamp.service;

import org.springframework.stereotype.Service;

import com.time.stamp.timestamp.dto.TimeStampDTO;

@Service
public interface TimeStampService {
	
	TimeStampDTO parseDate(String date);

}
