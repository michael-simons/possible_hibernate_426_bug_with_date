package tests;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.junit.Test;

import entities.A1;
import entities.A2;
import entities.AB1;
import entities.AB2;

/**
 * CREATE TABLE a1 (
 *   foobar_id INT(8) NOT NULL, 
 *   id  INT(8) NOT NULL AUTO_INCREMENT,
 *   info VARCHAR(512) NULL,
 *   PRIMARY KEY (id)
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 * 
 * CREATE TABLE b1 (
 *   id  INT(8) NOT NULL AUTO_INCREMENT,
 *   info VARCHAR(512) NULL,
 *   PRIMARY KEY (id)
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 * 
 * CREATE TABLE ab1 (
 *   foobar_id INT(8) NOT NULL, 
 *   a1_id  INT(8) NOT NULL,
 *   b1_id  INT(8) NOT NULL,
 *   lfdnr INT(8),
 *   PRIMARY KEY (foobar_id, a1_id, b1_id )
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 * 
 * CREATE TABLE a2 (
 *   foobar_id INT(8) NOT NULL, 
 *   id  INT(8) NOT NULL AUTO_INCREMENT,
 *   info VARCHAR(512) NULL,
 *   PRIMARY KEY (id)
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 * 
 * CREATE TABLE b2 (
 *   id  INT(8) NOT NULL AUTO_INCREMENT,
 *   info VARCHAR(512) NULL,
 *   PRIMARY KEY (id)
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 * 
 * CREATE TABLE ab2 (
 *   foobar_id INT(8) NOT NULL, 
 *   a2_id  INT(8) NOT NULL,
 *   b2_id  INT(8) NOT NULL,
 *   lfdnr INT(8),
 *   PRIMARY KEY (foobar_id, a2_id, b2_id )
 * ) CHARACTER SET utf8mb4 collate utf8mb4_bin engine innodb;
 */
public class IdColumnNamesTest {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	
	public IdColumnNamesTest() {
		this.entityManagerFactory = Persistence.createEntityManagerFactory("hibernate-test");
		this.entityManager = this.entityManagerFactory.createEntityManager();			
	}
	
	@SuppressWarnings("unused")
	@Test
	public void fails() {
		final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<A1> query = criteriaBuilder.createQuery(A1.class);		
		final Root<A1> root = query.from(A1.class); 
		
		final Subquery<String> subquery = query.subquery(String.class);
				
		final Root<AB1> subqueryRoot = subquery.from(AB1.class);        		        		
		
		// Additional restrictions are missing and would be present in production code
		List<A1> a1s = entityManager.createQuery(
				query.select(root).where(criteriaBuilder.exists(        				
						subquery.select(criteriaBuilder.literal("")).where(
								criteriaBuilder.equal(subqueryRoot.get("a1"), root)       						
						)
				))
		).getResultList();		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void works() {
		final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		CriteriaQuery<A2> query = criteriaBuilder.createQuery(A2.class);		
		final Root<A2> root = query.from(A2.class); 
		
		final Subquery<String> subquery = query.subquery(String.class);
				
		final Root<AB2> subqueryRoot = subquery.from(AB2.class);        		        		
				
		// Additional restrictions are missing and would be present in production code
		List<A2> a2s = entityManager.createQuery(
				query.select(root).where(criteriaBuilder.exists(        				
						subquery.select(criteriaBuilder.literal("")).where(
								criteriaBuilder.equal(subqueryRoot.get("a2"), root)       						
						)
				))
		).getResultList();		
	}
}