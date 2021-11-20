package com.avaliacao.metting_agenda.controller;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.avaliacao.metting_agenda.document.MettingAgenda;
import com.avaliacao.metting_agenda.exception.ExistingElementException;
import com.avaliacao.metting_agenda.exception.NotFoundException;
import com.avaliacao.metting_agenda.service.MettingAgendaService;

@RestController
public class MettingAgendaController {

	@Autowired
	private MettingAgendaService mettingAgendaService;

	@PostMapping(value = "/mettingAgenda/v1")
	public ResponseEntity<MettingAgenda> insertMettingAgenda(
			@Valid @RequestBody(required = true) MettingAgenda payload) {
		try {
			return new ResponseEntity<MettingAgenda>(mettingAgendaService.insertMettingAgenda(payload), HttpStatus.OK);
		} catch (ExistingElementException e) {
			return new ResponseEntity<MettingAgenda>(HttpStatus.CONFLICT);
		} catch (AccessException e) {
			return new ResponseEntity<MettingAgenda>(HttpStatus.UNAUTHORIZED);
		}
	}

	@PutMapping(value = "/mettingAgenda/v1")
	public ResponseEntity<MettingAgenda> votingSessionStart(
			@Valid @RequestBody(required = true) MettingAgenda payload) {
		try {
			return new ResponseEntity<MettingAgenda>(mettingAgendaService.startVotingSession(payload), HttpStatus.OK);
		} catch (NotFoundException e) {
			return new ResponseEntity<MettingAgenda>(HttpStatus.NOT_FOUND);
		} catch (AccessException e) {
			return new ResponseEntity<MettingAgenda>(HttpStatus.UNAUTHORIZED);
		}

	}
}
