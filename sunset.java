import static java.lang.Math.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/*
Today is Novemeber 21st, 2026,
The sun will set today around 4:49 PM EST

Created on: November 21st, 2026
Last edited: November 21st, 2026
*/

public class Sunset {

	public static double solarDeclinationCalc(int dayOfYear) {
		double solarDeclination = 23.44 * sin((toRadians(360.0)/365.0) * (dayOfYear - 81));
		
		return solarDeclination;
	}
	
	public static double eqauationOfTimeCalc(LocalDate date) {
		
		double eotConstant = (toRadians(360.0)/date.lengthOfYear()) * ((date.getDayOfYear() - 81));
		
		double equationOfTime = (9.87 * sin(2 * eotConstant)) - (7.53 * cos(eotConstant)) - (1.5 * sin(eotConstant));
		
		return equationOfTime;
	}
	
	public static double hourAngleHoursCalc(double latitude, double solarDeclination) {
		double cosHourAngle = ((sin(toRadians(-0.833))) - (sin(toRadians(latitude))) * (sin(toRadians(solarDeclination)))) / 
				((cos(toRadians(latitude))) * (cos(toRadians(solarDeclination))));
		
		double hourAngleHours = toDegrees(acos(cosHourAngle)) / 15.0;
		
		return hourAngleHours;
	}
	
	public static LocalTime sunsetTimeCalc(LocalDate date, double latitude, double longitude, ZoneId timeZone) {
		
		int dayOfYear = date.getDayOfYear();
		
		double solarDeclination = solarDeclinationCalc(dayOfYear);
		double equationOfTime = eqauationOfTimeCalc(date);
		double hourAngleHours = hourAngleHoursCalc(latitude, solarDeclination);
		
		ZoneOffset offSet = timeZone.getRules().getStandardOffset(date.atStartOfDay(timeZone).toInstant());
		double standardMeridian = (offSet.getTotalSeconds() / 3600.0) * 15.0;
		
		double sunsetHours  = 12.0 + ((standardMeridian - longitude) / 15.0) - (equationOfTime / 60.0) + hourAngleHours;
		
		ZonedDateTime standardMidnight = ZonedDateTime.of(date, LocalTime.MIDNIGHT, offSet);
		
		long sunsetSeconds = round(sunsetHours * 3600.0);
		
		ZonedDateTime sunsetTime = standardMidnight.plusSeconds(sunsetSeconds).withZoneSameInstant(timeZone);
		
		return sunsetTime.toLocalTime();
	}

	public static void main(String[] args) {
		double latitude = 38.91;
		double longitude = -77.04;
		
		LocalDate todayDate = LocalDate.now();
	
		ZoneId timeZone = ZoneId.of("America/New_York");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss a");
		
		LocalTime sunsetTimeToday = sunsetTimeCalc(todayDate, latitude, longitude, timeZone);
		
		System.out.println(todayDate);
		
		System.out.println("The sun will set today at: \n" + sunsetTimeToday.format(formatter) + " ~ " + timeZone + " Timezone");

	}

}
