package tests;

import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.junit.Assert;
import org.junit.Test;

import entities.Foobar;

/**
 * create database hibernate_test;
 * 
 * use hibernate_test;
 * 
 * CREATE TABLE foobars (
 *   id  INT(8) NOT NULL AUTO_INCREMENT,
 *   ref_date  DATE NOT NULL,
 *   taken_on  DATETIME NOT NULL,
 *   PRIMARY KEY (id)
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 * 
 * INSERT INTO foobars(id, ref_date, taken_on) values (1, '2013-10-15', '2013-10-15 05:29:21 ');
 * 
 * @author Michael J. Simons
 */
public class CalendarTest {	
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	public CalendarTest() {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
		
		this.entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-test");
		this.entityManager = this.entityManagerFactory.createEntityManager();	
	}
	
	@Test
	public void testCalendarMapping() {
		Foobar foobar = this.entityManager.find(Foobar.class, 1);
	
		// In both cases the @Temporal(TemporalType.TIMESTAMP) column
		// takenOn is correctly converted from UTC to Europe/Berlin.
		
		// Prior to 4.2.6 the behaviour for @Temporal(TemporalType.DATE)
		// seems to be "Assume the date at midnight AND use the zone from the driver"
		// (Driver is configured to use UTC at all times, see persistence.xml)
		// than convert this date to server time zone (Europe/Berlin).
		
		// Commit https://github.com/hibernate/hibernate-orm/commit/24a36b9cbb4b586aea3401374b991c68492cd08a
		// changed this: By setting the time attributes to 0 the resulting calendar changes.
		// Midnight (00:00:00) in the calendar that has been already converted to another timezone 
		// isn't the same.
		
		// Technically it seems to be more correct at first sight, but 
		// looking at both DATE and TIMESTAMP temporal types it has fare more suprises,
		// especally if used with LocalDate, see below
				
		// Hibernate 4.2.5
		// Tue Oct 15 02:00:00 CEST 2013, Tue Oct 15 07:29:21 CEST 2013
		// 2013-10-15T00:00:00.000+02:00
		// Hibernate 4.2.6
		// Tue Oct 15 00:00:00 CEST 2013, Tue Oct 15 07:29:21 CEST 2013
		// 2013-10-15T00:00:00.000+02:00
		System.out.println(foobar.getRefDate().getTime() + ", " + foobar.getTakenOn().getTime());		
		System.out.println(new LocalDate(foobar.getRefDate()).toDateTimeAtStartOfDay());
		
		// When used with JodaLocal Date to enforce a UTC Chronoly:
		Assert.assertEquals(new LocalDate(foobar.getRefDate(), ISOChronology.getInstanceUTC()), new LocalDate("2013-10-15", ISOChronology.getInstanceUTC()));
	}
}