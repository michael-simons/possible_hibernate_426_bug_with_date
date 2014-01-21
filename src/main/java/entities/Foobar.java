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

@Entity
@Table(name="foobars")
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
	
	@Column(name="some_other_column")
	private String someOtherColumn;
	
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

	public String getSomeOtherColumn() {
		return someOtherColumn;
	}

	public void setSomeOtherColumn(String someOtherColumn) {
		this.someOtherColumn = someOtherColumn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Foobar other = (Foobar) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
