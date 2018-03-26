package com.suyang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.suyang.domain.User;

public interface UserRepository extends JpaRepository<User, Integer>  {
	Page<User> findAll(Pageable pageable);
	int countByLoginName(String loginName);
}
