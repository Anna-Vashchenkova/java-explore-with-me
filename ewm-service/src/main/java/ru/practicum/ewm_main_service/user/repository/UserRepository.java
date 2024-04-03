package ru.practicum.ewm_main_service.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_main_service.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User as u order by u.id")
    Page<User> findAll(Pageable pageable);

    List<User> findAllByIdIn(List<Long> ids, Pageable pageable);

    @Query("select u from User as u where u.email = :email and u.name = :name order by u.id")
    User findUser(String email, String name);

    User findUserByEmail(String emailSearch);

}
