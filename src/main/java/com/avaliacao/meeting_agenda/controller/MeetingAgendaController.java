package com.avaliacao.meeting_agenda.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacao.meeting_agenda.document.MeetingAgenda;
import com.avaliacao.meeting_agenda.exception.ExistingElementException;
import com.avaliacao.meeting_agenda.exception.NotFoundException;
import com.avaliacao.meeting_agenda.service.MeetingAgendaService;

@RestController
public class MeetingAgendaController {

	@Autowired
	private MeetingAgendaService mettingAgendaService;

	@PostMapping(value = "/meetingAgenda/v1")
	public ResponseEntity<MeetingAgenda> insertMettingAgenda(
			@Valid @RequestBody(required = true) MeetingAgenda payload) {
		try {
			return new ResponseEntity<MeetingAgenda>(mettingAgendaService.insertMeetingAgenda(payload), HttpStatus.OK);
		} catch (ExistingElementException e) {
			return new ResponseEntity<MeetingAgenda>(HttpStatus.CONFLICT);
		} catch (AccessException e) {
			return new ResponseEntity<MeetingAgenda>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping(value = "/meetingAgenda/v1")
	public ResponseEntity<MeetingAgenda> votingSessionStart(
			@Valid @RequestBody(required = true) MeetingAgenda payload) {
		try {
			return new ResponseEntity<MeetingAgenda>(mettingAgendaService.startVotingSession(payload), HttpStatus.OK);
		} catch (NotFoundException e) {
			return new ResponseEntity<MeetingAgenda>(HttpStatus.NOT_FOUND);
		} catch (AccessException e) {
			return new ResponseEntity<MeetingAgenda>(HttpStatus.UNAUTHORIZED);
		}

	}
}
