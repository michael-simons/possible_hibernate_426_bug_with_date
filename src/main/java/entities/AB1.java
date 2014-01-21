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
@Table(name="ab1")
public class AB1 implements Serializable {	
	private static final long serialVersionUID = 4572746570117233628L;

	@Id
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumns({			
			@JoinColumn(name="foobar_id", referencedColumnName="foobar_id"),
			@JoinColumn(name="a1_id",     referencedColumnName="id")
	})	
	
	private A1 a1;
	
	@Id
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="b1_id")	
	@Fetch(FetchMode.JOIN)		
	private B1 b1;
	
	@Column(name="lfdnr", nullable=true)	
	private Integer lfdnr;

	public A1 getA1() {
		return a1;
	}

	public void setA1(A1 a1) {
		this.a1 = a1;
	}

	public B1 getB1() {
		return b1;
	}

	public void setB1(B1 b1) {
		this.b1 = b1;
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
		result = prime * result + ((a1 == null) ? 0 : a1.hashCode());
		result = prime * result + ((b1 == null) ? 0 : b1.hashCode());
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
		AB1 other = (AB1) obj;
		if (a1 == null) {
			if (other.a1 != null)
				return false;
		} else if (!a1.equals(other.a1))
			return false;
		if (b1 == null) {
			if (other.b1 != null)
				return false;
		} else if (!b1.equals(other.b1))
			return false;
		return true;
	}
}