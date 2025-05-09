package com.time.stamp.timestamp.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class TimeStampHelper {

    public boolean isValid(String date) {
        return isUnixTimestamp(date) || isDateString(date);
    }

    public boolean isUnixTimestamp(String str) {
        // Check if it's numeric and within expected length (10 or 13 digits)
        if (!str.matches("\\d{10}|\\d{13}")) {
            return false;
        }
        try {
            long timestamp = Long.parseLong(str);
            // Optionally check range: timestamps from 1970 to 2100
            long minValid = 0L;
            long maxValid = 4102444800L; // Around 2100 in seconds
            if (str.length() == 13) {
                timestamp /= 1000; // Convert milliseconds to seconds
            }
            return timestamp >= minValid && timestamp <= maxValid;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isDateString(String str) {
       Date date =  getDateFromInputString(str);
        return Objects.nonNull(date)?true:false;
    }

	public Date getDateFromInputString(String str) {
		Date date=null;
		String[] formats = {
            "yyyy-MM-dd",
            "yyyy/MM/dd",
            "dd-MM-yyyy",
            "MM-dd-yyyy",
            "yyyy-dd-MM"
        };
        for (String format : formats) {
            try {
            	DateFormat formatter= new SimpleDateFormat(format);
            	formatter.setLenient(false);
            	date = formatter.parse(str);
            	if(date!=null) {
            		return date;
            	}
            } catch (ParseException ignored) {
           
            }
        }
        return null;
	}
	
}

