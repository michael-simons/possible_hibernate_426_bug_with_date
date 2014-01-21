package entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name="ab2")
public class AB2 implements Serializable {
	private static final long serialVersionUID = -8298479745114824826L;

	@Id
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumns({			
			@JoinColumn(name="foobar_id", referencedColumnName="foobar_id"),
			@JoinColumn(name="a2_id",     referencedColumnName="id")
	})	
	
	private A2 a2;
	
	@Id
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="b2_id")	
	@Fetch(FetchMode.JOIN)		
	private B2 b2;
	
	@Column(name="lfdnr", nullable=true)	
	private Integer lfdnr;

	public A2 getA2() {
		return a2;
	}

	public void setA2(A2 a2) {
		this.a2 = a2;
	}

	public B2 getB2() {
		return b2;
	}

	public void setB2(B2 b2) {
		this.b2 = b2;
	}

	public Integer getLfdnr() {
		return lfdnr;
	}

	public void setLfdnr(Integer lfdnr) {
		this.lfdnr = lfdnr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a2 == null) ? 0 : a2.hashCode());
		result = prime * result + ((b2 == null) ? 0 : b2.hashCode());
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
		AB2 other = (AB2) obj;
		if (a2 == null) {
			if (other.a2 != null)
				return false;
		} else if (!a2.equals(other.a2))
			return false;
		if (b2 == null) {
			if (other.b2 != null)
				return false;
		} else if (!b2.equals(other.b2))
			return false;
		return true;
	}
}