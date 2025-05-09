package com.time.stamp.timestamp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.time.stamp.timestamp.dto.ErrorDto;
import com.time.stamp.timestamp.dto.TimeStampDTO;
import com.time.stamp.timestamp.service.TimeStampService;
import com.time.stamp.timestamp.validator.TimeStampHelper;

import io.micrometer.common.util.StringUtils;

@RestController
public class TimeStampController {
	
	private TimeStampHelper validator;
	
	private TimeStampService timeStampService;
	
	@Autowired
	public TimeStampController(TimeStampHelper validator,TimeStampService timeStampService) {
		this.validator = validator;
		this.timeStampService = timeStampService;
	}
	
	@GetMapping(value ={"/api/{date}","/api"})
	public ResponseEntity<Object> getTimeStampValue(@PathVariable(required = false) String date){
		TimeStampDTO result = null;
		if(StringUtils.isNotBlank(date)) {
			if(validator.isValid(date)) {
				result=timeStampService.parseDate(date);
			}else {
				return new ResponseEntity<Object>(new ErrorDto("Invalid Date"),HttpStatus.BAD_REQUEST);
			}
		}else {
			result=timeStampService.parseDate(null);
		}
		return new ResponseEntity<Object>(result, HttpStatus.OK);
		
	}

}
