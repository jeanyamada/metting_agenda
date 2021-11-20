package com.avaliacao.meeting_agenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avaliacao.meeting_agenda.document.MeetingAgenda;

public interface MeetingAgendaRepository extends MongoRepository<MeetingAgenda, String> {

}
