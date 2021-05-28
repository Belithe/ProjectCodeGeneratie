package io.swagger.repository;

import io.swagger.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Transactional
    @Modifying
    @Query("select user from User user where user.emailAddress =:emailAddress and user = :password")
    User findUserByLogin(@Param("emailAddress") String emailAddress, @Param("password") String password);

    @Query("select user from User user where user.lastName =:lastName")
    User findByLastname(@Param("lastName") String lastName);

    @Query("select user from User user where user.emailAddress =:emailAddress")
    User findByEmailAddress(@Param("emailAddress") String emailAddress);

    @Query("select user from User user where user.id =:id")
    User findById(@Param("id") Integer id);
}

