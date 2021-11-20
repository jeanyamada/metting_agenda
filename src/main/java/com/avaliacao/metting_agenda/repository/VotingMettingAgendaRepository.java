package com.avaliacao.metting_agenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avaliacao.metting_agenda.document.VotingMettingAgenda;

public interface VotingMettingAgendaRepository extends MongoRepository<VotingMettingAgenda, String> {

}
