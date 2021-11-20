package com.avaliacao.metting_agenda.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Service;

import com.avaliacao.metting_agenda.document.MettingAgenda;
import com.avaliacao.metting_agenda.exception.ExistingElementException;
import com.avaliacao.metting_agenda.exception.NotFoundException;
import com.avaliacao.metting_agenda.repository.MettingAgendaRepository;
import com.avaliacao.metting_agenda.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MettingAgendaService {

	@Value("${time.default}")
	private Long timeDefault;

	@Autowired
	private MettingAgendaRepository mettingAgendaRepository;

	public MettingAgenda insertMettingAgenda(MettingAgenda data) throws ExistingElementException, AccessException {

		log.info("started insertMettingAgenda");

		if (!Util.validateUserCpf(data.getUserCpf())) {
			String info = String.format("user: [%s] does not have a valid CPF: [%s]", data.getUsername(),
					data.getUserCpf());
			log.error(info);
			throw new AccessException(info);
		}

		String mettingAgendaId = idGenerator(data.getMettingAgendaName());
		MettingAgenda mettingAgenda = findById(mettingAgendaId);

		if (!Util.isNull(mettingAgenda)) {
			String info = String.format("the MettingAgenda element already exists, mettingAgendaName: [%s]",
					data.getMettingAgendaName());
			log.error(info);
			throw new ExistingElementException(info);
		}

		LocalDateTime lastedUpdateDate = LocalDateTime.now();

		data.setLastedUpdateDate(lastedUpdateDate);
		data.setId(idGenerator(data.getMettingAgendaName()));
		data.setSessionTimeMax(timeDefault);

		log.info(String.format("saving MettingAgenda: [%s]", data.getId()));
		return save(data);
	}

	public MettingAgenda startVotingSession(MettingAgenda data) throws NotFoundException, AccessException {

		log.info("started startVotingSession");

		if (!Util.validateUserCpf(data.getUserCpf())) {
			String info = String.format("user: [%s] does not have a valid CPF: [%s]", data.getUsername(),
					data.getUserCpf());
			log.error(info);
			throw new AccessException(info);
		}

		String mettingAgendaId = idGenerator(data.getMettingAgendaName());

		MettingAgenda mettingAgenda = findById(mettingAgendaId);

		if (Util.isNull(mettingAgenda)) {
			String info = String.format("not found mettingAgenda: [%s]", data.getMettingAgendaName());
			log.error(info);
			throw new NotFoundException(info);
		}

		if (!Util.isNull(data.getSessionTimeMax())) {
			mettingAgenda.setSessionTimeMax(data.getSessionTimeMax());
		}

		log.info(String.format("Session time max: [%s]", mettingAgenda.getSessionTimeMax()));

		LocalDateTime votingSessionStart = LocalDateTime.now();
		LocalDateTime votingSessionEnd = votingSessionStart.plusMinutes(mettingAgenda.getSessionTimeMax());

		log.info(String.format("voting session start: [%s] voting session end: [%s]", votingSessionStart,
				votingSessionEnd));

		mettingAgenda.setUsername(data.getUsername());
		mettingAgenda.setLastedUpdateDate(votingSessionStart);
		mettingAgenda.setVotingSessionStart(votingSessionStart);
		mettingAgenda.setVotingSessionEnd(votingSessionEnd);

		log.info(String.format("saving MettingAgenda: [%s]", mettingAgenda.getId()));
		return save(mettingAgenda);
	}

	public MettingAgenda findById(String id) {
		Optional<MettingAgenda> op = mettingAgendaRepository.findById(id);
		return op.isPresent() ? op.get() : null;
	}

	public MettingAgenda findById(String id, LocalDateTime current) {
		Optional<MettingAgenda> op = mettingAgendaRepository.findById(id);
		return op.isPresent() ? op.get() : null;
	}

	public MettingAgenda save(MettingAgenda data) {
		return mettingAgendaRepository.save(data);
	}

	public String idGenerator(String mettingAgendaName) {
		return Util.toHex(mettingAgendaName);
	}

}
