package entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name="foobars")
@DynamicUpdate
public class Foobar implements Serializable {
	private static final long serialVersionUID = -1590584730789764160L;
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="ref_date")
	@Temporal(TemporalType.DATE)
	private Calendar refDate;
	
	@Column(name="taken_on")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar takenOn;
	
	public Calendar getTakenOn() {
		return takenOn;
	}

	public void setTakenOn(Calendar takenOn) {
		this.takenOn = takenOn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Calendar getRefDate() {
		return refDate;
	}

	public void setRefDate(Calendar refDate) {
		this.refDate = refDate;
	}
}
