package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

//	@Query(nativeQuery = true, 
//			value="SELECT CASE WHEN EXISTS"
//			    + "(SELECT 1" //utile per exists query
//			    + "FROM users"
//			    + "WHERE email=:email)"
//			    + "THEN 'true'"
//			    + "ELSE 'false'"
//			    + "END")
	public boolean existsByEmail(String email);
}
