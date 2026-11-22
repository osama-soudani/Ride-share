package com.rideshare.sos.repository;

import com.rideshare.sos.entity.Mecanicien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MecanicienRepository extends JpaRepository<Mecanicien, Long> {
}