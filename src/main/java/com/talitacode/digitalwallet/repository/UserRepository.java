package com.talitacode.digitalwallet.repository;

import com.talitacode.digitalwallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByLogin(String login);

    boolean existsByCpf(String cpf);
}
