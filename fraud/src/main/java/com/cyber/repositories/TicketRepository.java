package com.cyber.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyber.model.Ticket;


public interface TicketRepository extends JpaRepository<Ticket,Long>
{
	List<Ticket> findByUser_Id(Long userId);

	List<Ticket> findAllByOrderByCreatedAtDesc();
	
	Optional<Ticket> findByIdAndUser_Id(Long id, Long userId);

}
