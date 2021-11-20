package com.avaliacao.metting_agenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avaliacao.metting_agenda.document.MettingAgenda;

public interface MettingAgendaRepository extends MongoRepository<MettingAgenda, String> {

}
