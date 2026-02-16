package com.cyber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cyber.model.TicketMedia;

@Repository
public interface TicketMediaRepository extends JpaRepository<TicketMedia, Long> {

}
