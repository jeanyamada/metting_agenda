package com.avaliacao.meeting_agenda.controller;

import java.rmi.AccessException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacao.meeting_agenda.document.VotingMeetingAgenda;
import com.avaliacao.meeting_agenda.exception.ExistingElementException;
import com.avaliacao.meeting_agenda.exception.NotFoundException;
import com.avaliacao.meeting_agenda.service.VotingMeetingAgendaService;
import com.avaliacao.meeting_agenda.view.VotingMeetingAgendaTotal;


@RestController
public class VotingMettingAgendaController {

	@Autowired
	private VotingMeetingAgendaService votingMettingAgendaService;

	@PostMapping(value = "/votingMettingAgenda/v1")
	public ResponseEntity<VotingMeetingAgenda> votingMettingAgenda(
			@Valid @RequestBody(required = true) VotingMeetingAgenda payload) {
		try {
			return new ResponseEntity<VotingMeetingAgenda>(
					votingMettingAgendaService.insertVotingMeetingAgenda(payload), HttpStatus.OK);
		} catch (ExistingElementException e) {
			return new ResponseEntity<VotingMeetingAgenda>(HttpStatus.CONFLICT);
		} catch (NotFoundException e) {
			return new ResponseEntity<VotingMeetingAgenda>(HttpStatus.NOT_FOUND);
		} catch (AccessException e) {
			return new ResponseEntity<VotingMeetingAgenda>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping(value = "/votingMettingAgenda/v1")
	public ResponseEntity<List<VotingMeetingAgendaTotal>> votingResult(
			@RequestParam(required = true) String mettingAgendaName) {
		return new ResponseEntity<List<VotingMeetingAgendaTotal>>(
				votingMettingAgendaService.votingResult(mettingAgendaName), HttpStatus.OK);
	}

}
