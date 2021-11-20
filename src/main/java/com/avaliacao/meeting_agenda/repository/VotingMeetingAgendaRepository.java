package com.avaliacao.meeting_agenda.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avaliacao.meeting_agenda.model.VotingMeetingAgenda;

public interface VotingMeetingAgendaRepository extends MongoRepository<VotingMeetingAgenda, String> {

}
