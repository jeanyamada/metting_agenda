package com.avaliacao.metting_agenda.controller;

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

import com.avaliacao.metting_agenda.document.VotingMettingAgenda;
import com.avaliacao.metting_agenda.exception.ExistingElementException;
import com.avaliacao.metting_agenda.exception.NotFoundException;
import com.avaliacao.metting_agenda.service.VotingMettingAgendaService;
import com.avaliacao.metting_agenda.view.VotingMettringAgendaTotal;


@RestController
public class VotingMettingAgendaController {

	@Autowired
	private VotingMettingAgendaService votingMettingAgendaService;

	@PostMapping(value = "/votingMettingAgenda/v1")
	public ResponseEntity<VotingMettingAgenda> votingMettingAgenda(
			@Valid @RequestBody(required = true) VotingMettingAgenda payload) {
		try {
			return new ResponseEntity<VotingMettingAgenda>(
					votingMettingAgendaService.insertVotingMettingAgenda(payload), HttpStatus.OK);
		} catch (ExistingElementException e) {
			return new ResponseEntity<VotingMettingAgenda>(HttpStatus.CONFLICT);
		} catch (NotFoundException e) {
			return new ResponseEntity<VotingMettingAgenda>(HttpStatus.NOT_FOUND);
		} catch (AccessException e) {
			return new ResponseEntity<VotingMettingAgenda>(HttpStatus.UNAUTHORIZED);
		}
	}

	@GetMapping(value = "/votingMettingAgenda/v1")
	public ResponseEntity<List<VotingMettringAgendaTotal>> votingResult(
			@RequestParam(required = true) String mettingAgendaName) {
		return new ResponseEntity<List<VotingMettringAgendaTotal>>(
				votingMettingAgendaService.votingResult(mettingAgendaName), HttpStatus.OK);
	}

}
