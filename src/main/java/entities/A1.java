package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="a1")
public class A1 implements Serializable {	
	private static final long serialVersionUID = -3998987427422042724L;

	@Id
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="foobar_id")	
	@Fetch(FetchMode.JOIN)
	private Foobar foobar;
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="info")
	private String info;

	public Foobar getFoobar() {
		return foobar;
	}

	public void setFoobar(Foobar foobar) {
		this.foobar = foobar;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((foobar == null) ? 0 : foobar.hashCode());
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
		A1 other = (A1) obj;
		if (foobar == null) {
			if (other.foobar != null)
				return false;
		} else if (!foobar.equals(other.foobar))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}