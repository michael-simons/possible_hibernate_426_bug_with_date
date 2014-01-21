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
@Table(name="a2")
public class A2 implements Serializable {	
	private static final long serialVersionUID = 8412021922850670530L;

	@Id
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name="foobar_id")	
	@Fetch(FetchMode.JOIN)
	private Foobar foobar;
	
	@Id	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long a2id;
	
	@Column(name="info")
	private String info;

	public Foobar getFoobar() {
		return foobar;
	}

	public void setFoobar(Foobar foobar) {
		this.foobar = foobar;
	}

	public Long getA2id() {
		return a2id;
	}

	public void setA2id(Long a2id) {
		this.a2id = a2id;
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
		result = prime * result + ((a2id == null) ? 0 : a2id.hashCode());
		result = prime * result + ((foobar == null) ? 0 : foobar.hashCode());
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
		A2 other = (A2) obj;
		if (a2id == null) {
			if (other.a2id != null)
				return false;
		} else if (!a2id.equals(other.a2id))
			return false;
		if (foobar == null) {
			if (other.foobar != null)
				return false;
		} else if (!foobar.equals(other.foobar))
			return false;
		return true;
	}
}
