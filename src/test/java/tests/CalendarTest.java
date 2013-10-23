package tests;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
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
 * DROP TABLE foobars;
 * 
 * CREATE TABLE foobars (
 *   id  INT(8) NOT NULL AUTO_INCREMENT,
 *   ref_date  DATE NOT NULL,
 *   taken_on  DATETIME NOT NULL,
 *   some_other_column VARCHAR(512) NULL,
 *   PRIMARY KEY (id)
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 * 
 * INSERT INTO foobars(id, ref_date, taken_on) values (1, '2013-10-15', '2013-10-15 05:29:21');
 * 
 * COMMIT;
 * 
 * @author Michael J. Simons
 */
public class CalendarTest {	
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	private final TimeZone europe_berlin;
	private final TimeZone utc;
	
	
	
	public CalendarTest() {
		TimeZone.setDefault(europe_berlin = TimeZone.getTimeZone("Europe/Berlin"));
		Locale.setDefault(Locale.GERMAN);
		
		this.utc = TimeZone.getTimeZone("UTC");
		
		this.entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-test");
		this.entityManager = this.entityManagerFactory.createEntityManager();			
	}
	
	private void stripTimeInfo(final Calendar valueFromJDBCDriver) {
		// Create an UTC instance. Here it's safe to set everyting to ZERO apart from the date.
		Calendar calUTC = Calendar.getInstance(utc);
		calUTC.clear();
		calUTC.set(valueFromJDBCDriver.get(Calendar.YEAR), valueFromJDBCDriver.get(Calendar.MONTH), valueFromJDBCDriver.get(Calendar.DATE), 0, 0, 0);
		
		// Represent this date in the users timezone
		valueFromJDBCDriver.setTimeInMillis(calUTC.getTimeInMillis());		
	}
	
	@Test
	public void possibleSolution() {		
		final DateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormatUTC.setTimeZone(utc);
		
		
		// This is what the MySQL driver returns...
		// The value from the UTC DATE column with 00:00 values converted to
		// the JVMs timezone
		Calendar cal = Calendar.getInstance(europe_berlin);
		cal.set(2013, 10-1, 15, 2, 0, 0);
				
		// Check if input is correct		
		Assert.assertEquals("Tue Oct 15 02:00:00 CEST 2013", cal.getTime().toString());	
		
		// Strip time information
		stripTimeInfo(cal);
		// Lets check the value in UTC
		Assert.assertEquals("2013-10-15 00:00", dateFormatUTC.format(cal.getTime()));
		// Date#toString() always uses default TZ which has been set to Europe/Berlin 
		Assert.assertEquals("Tue Oct 15 02:00:00 CEST 2013", cal.getTime().toString());
		
		// Regarding the other issue with the faulty Oracle 12c driver HHH-8517
		// this solution works also
		cal = Calendar.getInstance(europe_berlin);
		cal.set(2013, 10-1, 15, 21, 21, 21);
		
		stripTimeInfo(cal);		
		Assert.assertEquals("2013-10-15 00:00", dateFormatUTC.format(cal.getTime()));
		Assert.assertEquals("Tue Oct 15 02:00:00 CEST 2013", cal.getTime().toString());
		
		// If everything is configured to use utc, the solution also works		
		cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		// Asume we have the funny oracle driver again
		cal.set(2013, 10-1, 15, 21, 21, 21);
				
		stripTimeInfo(cal);		
		Assert.assertEquals("2013-10-15 00:00", dateFormatUTC.format(cal.getTime()));
		Assert.assertEquals("Tue Oct 15 02:00:00 CEST 2013", cal.getTime().toString());		
	}
	
	/**
	 * When the TZ and Time of a date column are not relevant, than explain to me,
	 * why the date ends up as 2013-10-14 in my database....
	 * @throws SQLException
	 */
	@Test
	public void update() throws SQLException {
		this.entityManager.getTransaction().begin();
		Foobar foobar = new Foobar();
		Calendar hlp = Calendar.getInstance();
		// So this is now 2013-10-15 at Midnight CEST…
		hlp.set(2013, 10-1, 15, 2, 0, 0);
		foobar.setRefDate(hlp);
		foobar.setTakenOn(Calendar.getInstance());
		this.entityManager.persist(foobar);		
		this.entityManager.getTransaction().commit();
		final int id = foobar.getId();

		final DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		
		Session session = entityManager.unwrap(Session.class);
		session.doWork(new Work() {			
			@Override
			public void execute(Connection connection) throws SQLException {
				ResultSet rs = connection.createStatement().executeQuery("Select ref_date from foobars where id = " + id);
				rs.next();
				Date date = rs.getDate(1);
				Assert.assertEquals("2013-10-15", sf.format(date));
				System.out.println(date);				
			}
		});		
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
		
		/* At least it seams to be consistent
		Date date = (Date) this.entityManager.createNativeQuery("Select max(ref_date) from foobars").getSingleResult();
		Assert.assertEquals(new LocalDate(date, ISOChronology.getInstanceUTC()), new LocalDate("2013-10-15", ISOChronology.getInstanceUTC()));
		*/
		
		// When used with JodaLocal Date to enforce a UTC Chronoly:
		Assert.assertEquals(new LocalDate(foobar.getRefDate(), ISOChronology.getInstanceUTC()), new LocalDate("2013-10-15", ISOChronology.getInstanceUTC()));
	}
}