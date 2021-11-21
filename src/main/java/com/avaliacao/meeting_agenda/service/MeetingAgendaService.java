package com.avaliacao.meeting_agenda.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;

import com.avaliacao.meeting_agenda.exception.ExistingElementException;
import com.avaliacao.meeting_agenda.exception.NotFoundException;
import com.avaliacao.meeting_agenda.model.MeetingAgenda;
import com.avaliacao.meeting_agenda.repository.MeetingAgendaRepository;
import com.avaliacao.meeting_agenda.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MeetingAgendaService {

	@Value("${time.default}")
	private Long timeDefault;

	@Autowired
	private UserService userService;

	@Autowired
	private MeetingAgendaRepository meetingAgendaRepository;

	public MeetingAgenda insertMeetingAgenda(MeetingAgenda data) throws ExistingElementException, AccessException {

		log.info("started insertMeetingAgenda");

		if (!userService.validateUserCpf(data.getUserCpf())) {
			String info = String.format("user: [%s] does not have a valid CPF: [%s]", data.getUsername(),
					data.getUserCpf());
			log.error(info);
			throw new AccessException(info);
		}

		String meetingAgendaId = idGenerator(data.getMeetingAgendaName());
		MeetingAgenda meetingAgenda = findById(meetingAgendaId);

		if (!Util.isNull(meetingAgenda)) {
			String info = String.format("the MeetingAgenda element already exists, meetingAgendaName: [%s]",
					data.getMeetingAgendaName());
			log.error(info);
			throw new ExistingElementException(info);
		}

		LocalDateTime lastedUpdateDate = LocalDateTime.now();

		data.setLastedUpdateDate(lastedUpdateDate);
		data.setId(idGenerator(data.getMeetingAgendaName()));
		data.setSessionTimeMax(timeDefault);

		log.info(String.format("saving MeetingAgenda: [%s]", data.getId()));
		return save(data);
	}

	public MeetingAgenda startVotingSession(MeetingAgenda data) throws NotFoundException, AccessException {

		log.info("started startVotingSession");

		if (!userService.validateUserCpf(data.getUserCpf())) {
			String info = String.format("user: [%s] does not have a valid CPF: [%s]", data.getUsername(),
					data.getUserCpf());
			log.error(info);
			throw new AccessException(info);
		}

		String meetingAgendaId = idGenerator(data.getMeetingAgendaName());

		MeetingAgenda meetingAgenda = findById(meetingAgendaId);

		if (Util.isNull(meetingAgenda)) {
			String info = String.format("not found meetingAgenda: [%s]", data.getMeetingAgendaName());
			log.error(info);
			throw new NotFoundException(info);
		}

		if (!Util.isNull(data.getSessionTimeMax())) {
			meetingAgenda.setSessionTimeMax(data.getSessionTimeMax());
		}

		log.info(String.format("Session time max: [%s]", meetingAgenda.getSessionTimeMax()));

		LocalDateTime votingSessionStart = LocalDateTime.now();
		LocalDateTime votingSessionEnd = votingSessionStart.plusMinutes(meetingAgenda.getSessionTimeMax());

		log.info(String.format("voting session start: [%s] voting session end: [%s]", votingSessionStart,
				votingSessionEnd));

		meetingAgenda.setUsername(data.getUsername());
		meetingAgenda.setLastedUpdateDate(votingSessionStart);
		meetingAgenda.setVotingSessionStart(votingSessionStart);
		meetingAgenda.setVotingSessionEnd(votingSessionEnd);

		log.info(String.format("saving MeetingAgenda: [%s]", meetingAgenda.getId()));
		return save(meetingAgenda);
	}

	public MeetingAgenda findById(String id) {
		Optional<MeetingAgenda> op = meetingAgendaRepository.findById(id);
		return op.isPresent() ? op.get() : null;
	}

	public MeetingAgenda findById(String id, LocalDateTime current) {
		Optional<MeetingAgenda> op = meetingAgendaRepository.findById(id);
		return op.isPresent() ? op.get() : null;
	}

	public MeetingAgenda save(MeetingAgenda data) {
		return meetingAgendaRepository.save(data);
	}

	public String idGenerator(String meetingAgendaName) {
		return Util.toHex(meetingAgendaName);
	}

}
