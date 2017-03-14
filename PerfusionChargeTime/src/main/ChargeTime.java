package main;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * ChargeTime maintains the total times for Perfusion Charges and the start and
 * stop days and times. Calculates the total number of days, hours and minutes
 * of charges. Also calculates daily totals for multiple days.
 * 
 * @author Kimberle McGill
 * @version 1.1.170314
 */
public class ChargeTime {

	/* Start date and time */
	private String start;
	/* End date and time */
	private String stop;
	/* Is this a multi-day charge? */
	private Boolean multi;
	/* Charges for the given start and stop days and times */
	private String charges;
	/* Format of date/time entry */
	private final static String ENTRY_FORMAT = "MMdd HHmm";
	/* Format of time only */
	private final static String TIME_FORMAT = "HHmm";
	/* Number of minutes in an hour */
	private final static int MIN_PER_HOUR = 60;
	/* Number of hours in a day */
	private final static int HOUR_PER_DAY = 24;
	/* Milliseconds in a second */
	private final static int MILLS_PER_SEC = 1000;
	/* Just before midnight */
	private final static String LAST_MIN_OF_DAY = "2359";
	/* Midnight */
	private final static String MIDNIGHT = "0000";
	/* Index of start of month substring */
	private final static int MONTH_START = 0;
	/* Index of end of month substring */
	private final static int MONTH_END = 2;
	/* Index of start of day substring */
	private final static int DAY_START = 2;
	/* Index of end of day substring */
	private final static int DAY_END = 4;
	/* Index for the start of the time substring */
	private final static int TIME_START = 5;
	/* Length of proper entry */
	private final static int PROPER_ENTRY = 9;
	/* Month of December */
	private final static String DEC = "12";
	/* Month of January */
	private final static String JAN = "01";
	/* Milliseconds in a year */
	private final static long MILLS_IN_YEAR = (long) 1000 * 60 * 60 * 24 * 365;
	/* Milliseconds in a leap year */
	private final static long MILLS_IN_LEAP_YEAR = (long) 1000 * 60 * 60 * 24 * 366;
	/* Hundreds divisible by 4 */
	private final static int HUNDREDS_BY_FOUR = 400;
	/* Ones divisible by 4 */
	private final static int ONES_BY_FOUR = 4;
	/* Hundreds */
	private final static int HUNDREDS = 100;
	/* Maximum number of minutes for charge entry */
	private final static int MAX_MINS = 999;

	/**
	 * Constructs a new ChargeTime with the given start time and stop time, and if
	 * a multi charge event (multi = true), then includes the start date and 
	 * stop date. The date is in the format of MMdd and the time is in the format
	 * of HHmm. Proper start format is "MMdd HHmm". Proper stop format is 
	 * "MMdd HHmm".
	 * 
	 * @param start
	 *            the starting date and time in the format of "MMdd HHmm"
	 * @param stop
	 *            the stopping date and time in the format of "MMdd HHmm"
	 * @param multi
	 *            boolean for whether this is a multi-day event
	 * @throws IllegalArgumentException
	 *             if the parameters given are not in the proper format
	 */
	public ChargeTime(String start, String stop, Boolean multi) {
		this.start = start;
		this.stop = stop;
		this.multi = multi;
		if (isProperFormat(this.start) && isProperFormat(this.stop)) {
			this.charges = calculateCharges();
		} else {
			throw new IllegalArgumentException("Invalid entry! Entries must be in the form of MMdd or HHmm");
		}
	}

	/**
	 * Gets the calculated charges.
	 * 
	 * @return the calculated charges as a string
	 */
	public String getCharges() {
		return this.charges;
	}

	/*
	 * Calculates charges based on the given start and stop dates and times.
	 */
	private String calculateCharges() {

		String timeTotal = "";

		// converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat(ENTRY_FORMAT);

		Date d1 = null;
		Date d2 = null;

		try

		{
			d1 = format.parse(this.start);
			d2 = format.parse(this.stop);

			// difference between start date/time and stop date/time in milliseconds
			long diff = d2.getTime() - d1.getTime();

			// if multi days and crosses to a new year (ex., dec 28 - jan 10)
			if (this.multi) {
				if (isNewYear()) { 
					if (wasLeapYear()) {
						diff = (d2.getTime() + MILLS_IN_LEAP_YEAR) - d1.getTime();
					} else {
						diff = (d2.getTime() + MILLS_IN_YEAR) - d1.getTime();
					}
				}
			}

			// days, hours, minutes, total mins, total hrs
			long diffMinutes = diff / (MIN_PER_HOUR * MILLS_PER_SEC) % MIN_PER_HOUR;
			long diffHours = diff / (MIN_PER_HOUR * MIN_PER_HOUR * MILLS_PER_SEC) % HOUR_PER_DAY;
			long diffDays = diff / (HOUR_PER_DAY * MIN_PER_HOUR * MIN_PER_HOUR * MILLS_PER_SEC);
			long totalMins = (diffDays * (HOUR_PER_DAY * MIN_PER_HOUR)) + (diffHours * MIN_PER_HOUR) + diffMinutes;
			double totalHours = (diffDays * HOUR_PER_DAY) + (diffHours) + (diffMinutes / (double) MIN_PER_HOUR);

			// multiple days?
			if (this.multi) {
				
				int numberOfDays = numDaysToCalculate(this.start, this.stop);
				int thisDay = 1;
				while (thisDay <= numberOfDays) {
                    
					long minutes;
					
					// get first day
					if (thisDay == 1) {
						minutes = numberOfMinutes(this.start.substring(TIME_START), LAST_MIN_OF_DAY) + 1;
					} else if (thisDay == numberOfDays) {
						minutes = numberOfMinutes(MIDNIGHT, this.stop.substring(TIME_START));
					} else {
						minutes = numberOfMinutes(MIDNIGHT, LAST_MIN_OF_DAY) + 1;
					}
					
					timeTotal += ("Day " + thisDay + " minutes: " + splitMins(minutes) + "\n");
					
					// move to next day
					thisDay++;
				}
				
				timeTotal += (diffDays + " days, ");
				timeTotal += (diffHours + " hours, ");
				timeTotal += (diffMinutes + " minutes\n");
				timeTotal += ("Total hours: " + round(totalHours, 2) + " hours\n");
				timeTotal += ("Total minutes: " + totalMins + " minutes\n");
				
			// single day, total minutes only to be displayed
			} else {
				timeTotal = "Total minutes: " + splitMins(totalMins) + " minutes\n";
			}

			return timeTotal;

		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid calculation in date/times");
		}
	}

	/*
	 * Rounds to the nearest given digits after the decimal
	 */
	private double round(double value, int numberOfDigitsAfterDecimalPoint) {
		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_HALF_UP);
		return bigDecimal.doubleValue();
	}

	/*
	 * Returns the number of days to calculate
	 */
	private int numDaysToCalculate(String start, String stop) {

		int thisYear = LocalDate.now().getYear();

		String startDay = thisYear + "-" + start.substring(MONTH_START, MONTH_END);
		startDay += "-" + start.substring(DAY_START, DAY_END);

		String stopDay = thisYear + "-" + stop.substring(MONTH_START, MONTH_END);
		stopDay += "-" + stop.substring(DAY_START, DAY_END);

		// consider December - January (different years)
		if (isNewYear()) {
			stopDay = (thisYear + 1) + "-" + stop.substring(MONTH_START, MONTH_END);
			stopDay += "-" + stop.substring(DAY_START, DAY_END);
		}

		int days = 0;

		LocalDate day1 = LocalDate.parse(startDay);
		LocalDate day2 = LocalDate.parse(stopDay);

		while (day1.compareTo(day2) <= 0) {
			days++;
			day1 = day1.plusDays(1);
		}

		return days;
	}

	/*
	 * Returns the number of minutes between the start and stop times
	 */
	private long numberOfMinutes(String start, String stop) {

		SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);

		Date d1;
		Date d2;

		try {
			d1 = format.parse(start);
			d2 = format.parse(stop);

			long diffMs = d2.getTime() - d1.getTime();
			long diffSec = diffMs / MILLS_PER_SEC;
			long diffMin = diffSec / MIN_PER_HOUR;

			return diffMin;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/*
	 * Is the entry formatted properly?
	 */
	private boolean isProperFormat(String entry) {
		if (!entry.isEmpty() && entry.length() == PROPER_ENTRY) {
			for (int i = 0; i < DAY_END; i++) {
				if (!Character.isDigit(entry.charAt(i))) {
					return false;
				}
			}

			for (int i = TIME_START; i < entry.length(); i++) {
				if (!Character.isDigit(entry.charAt(i))) {
					return false;
				}
			}

			return true;
		}
		return false;
	}

	/*
	 * Is this an overlapping year (Dec - Jan)?
	 */
	private boolean isNewYear() {
		if (this.start.substring(MONTH_START, MONTH_END).equals(DEC) && 
				this.stop.substring(MONTH_START, MONTH_END).equals(JAN)) {
			return true;
		}
		return false;
	}

	/*
	 * Was last year a leap year?
	 */
	private boolean wasLeapYear() {
		int lastYear = LocalDate.now().getYear() - 1;
		if ((lastYear % HUNDREDS_BY_FOUR == 0) 
				|| ((lastYear % ONES_BY_FOUR == 0) && (lastYear % HUNDREDS != 0))) {
			return true;
		}
		return false;
	}
	
	/*
	 * Split the minutes that are > 999 to minutes plus 999
	 * for entering charges into ChargeMaster.
	 */
	private String splitMins(long totalMins) {
		String properMins;
		if (totalMins > MAX_MINS) {
			properMins = "" + MAX_MINS + " + " + (totalMins - MAX_MINS) + " (" + totalMins + ")";
		} else {
			properMins = "" + totalMins;
		}
		return properMins;
	}
	
	/*
	 * Returns the month name for the given int.
	 * int must be between 0 and 11.
	 */
	String getMonthForInt(int month) {
		String monthName = "INVALID INT for MONTH";
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		if (month >= 0 && month <= 11) {
			monthName = months[month];
		} 
		return monthName;
	}
}
