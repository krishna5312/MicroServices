package com.time.stamp.timestamp.service;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.time.stamp.timestamp.dto.TimeStampDTO;
import com.time.stamp.timestamp.validator.TimeStampHelper;

import io.micrometer.common.util.StringUtils;

@Component
public class TimeStampServiceImpl implements TimeStampService {

	private TimeStampHelper helper;

	private static final ZoneOffset UTC = ZoneOffset.UTC;
	private static final String GMT = "GMT";
	private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";

	
	public TimeStampServiceImpl(TimeStampHelper helper) {
		this.helper = helper;
	}


	@Override
	public TimeStampDTO parseDate(String date) {
		LocalDateTime currentDate = null;
		String formattedDate = null;
		DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT, Locale.ENGLISH);
		long unixTime = 0;
		if (StringUtils.isBlank(date)) {
			currentDate = LocalDate.now(ZoneId.of(GMT)).atStartOfDay();
			unixTime = currentDate.toEpochSecond(UTC) * 1000;
		} else {

			if (helper.isUnixTimestamp(date)) {
				long parsedTime = Long.valueOf(date);
				Instant inst = Instant.ofEpochMilli(parsedTime);
				currentDate = LocalDate.ofInstant(inst, ZoneId.of(GMT)).atStartOfDay();
				unixTime = parsedTime;
			} else {
				try {
					Date parsedDate = helper.getDateFromInputString(date);
					System.out.println("Parsed Date is ::::: "+parsedDate);
					currentDate = LocalDate.ofInstant(parsedDate.toInstant(),ZoneId.of(GMT)).atStartOfDay();
					System.out.println("Current Date is :::::::: "+currentDate );
					unixTime = parsedDate.getTime();
				} catch (DateTimeParseException dtpe) {
					throw new DateTimeException("Can't format date");
				}
			}
		}

		formattedDate = currentDate.atOffset(UTC).format(df);
		return new TimeStampDTO(unixTime + "", formattedDate);
	}

}
