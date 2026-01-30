package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Paste;

@Repository
public interface PasteRepository extends JpaRepository<Paste, Long> {

}
