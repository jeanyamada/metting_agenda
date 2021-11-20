package com.avaliacao.metting_agenda.service;

import java.rmi.AccessException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.avaliacao.metting_agenda.document.MettingAgenda;
import com.avaliacao.metting_agenda.document.VotingMettingAgenda;
import com.avaliacao.metting_agenda.exception.ExistingElementException;
import com.avaliacao.metting_agenda.exception.NotFoundException;
import com.avaliacao.metting_agenda.repository.VotingMettingAgendaRepository;
import com.avaliacao.metting_agenda.util.Util;
import com.avaliacao.metting_agenda.view.VotingMettringAgendaTotal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VotingMettingAgendaService {

	@Autowired
	private VotingMettingAgendaRepository votingMettingAgendaRepository;

	@Autowired
	private MettingAgendaService mettingAgendaService;

	@Autowired
	private MongoTemplate mongoTemplate;

	public VotingMettingAgenda insertVotingMettingAgenda(VotingMettingAgenda data)
			throws NotFoundException, ExistingElementException, AccessException {

		log.info("started insertVotingMettingAgenda");

		LocalDateTime current = LocalDateTime.now();

		if (!Util.validateUserCpf(data.getUserCpf())) {
			String info = String.format("user: [%s] does not have a valid CPF: [%s]", data.getUsername(),
					data.getUserCpf());
			log.error(info);
			throw new AccessException(info);
		}

		String votingMettingId = idGenerator(data.getUserCpf(), data.getMettingAgendaName());
		VotingMettingAgenda votingMettingAgenda = findById(votingMettingId);

		if (!Util.isNull(votingMettingAgenda)) {
			String info = String.format(
					"the VotingMettingAgenda element already exists, username: [%s]  mettingAgendaName: [%s]",
					data.getUsername(), data.getMettingAgendaName());
			log.error(info);
			throw new ExistingElementException(info);
		}

		String mettingAgendaId = mettingAgendaService.idGenerator(data.getMettingAgendaName());
		MettingAgenda mettingAgenda = mettingAgendaService.findById(mettingAgendaId);

		if (Util.isNull(mettingAgenda)) {
			String info = String.format("not found mettingAgenda: [%s]", data.getMettingAgendaName());
			log.error(info);
			throw new NotFoundException(info);
		}

		if (!(Util.isAfterOrEquals(current, mettingAgenda.getVotingSessionStart())
				&& Util.isBeforeOrEquals(current, mettingAgenda.getVotingSessionEnd()))) {
			String info = String.format("the meetingAgenda: [%s] is not open for voting", data.getMettingAgendaName());
			log.error(info);
			throw new NotFoundException(info);
		}

		LocalDateTime lastedUpdateDate = LocalDateTime.now();

		data.setLastedUpdateDate(lastedUpdateDate);
		data.setId(votingMettingId);

		log.info(String.format("saving VotingMettingAgenda: [%s]", data.getId()));
		return save(data);

	}

	public List<VotingMettringAgendaTotal> votingResult(String mettingAgendaName) {

		log.info("started votingResult");

		List<AggregationOperation> fetchOperations = new ArrayList<AggregationOperation>();

		fetchOperations.add(Aggregation.match(Criteria.where("mettingAgendaName").is(mettingAgendaName)));
		fetchOperations.add(Aggregation.group("mettingAgendaName", "votingType").count().as("total"));
		fetchOperations.add(Aggregation.project("mettingAgendaName", "votingType", "total"));
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
		Aggregation aggregation = Aggregation.newAggregation(fetchOperations).withOptions(options);

		log.info(String.format("votingResult aggregation : [%s]", aggregation.toString()));

		AggregationResults<VotingMettringAgendaTotal> data = mongoTemplate.aggregate(aggregation,
				VotingMettingAgenda.class, VotingMettringAgendaTotal.class);

		return data.getMappedResults();
	}

	public VotingMettingAgenda findById(String id) {
		Optional<VotingMettingAgenda> op = votingMettingAgendaRepository.findById(id);
		return op.isPresent() ? op.get() : null;
	}

	public VotingMettingAgenda save(VotingMettingAgenda votingMettingAgenda) {
		return votingMettingAgendaRepository.save(votingMettingAgenda);
	}

	public String idGenerator(String userCpf, String mettingAgendaName) {
		String id = Util.concat(userCpf, mettingAgendaName);
		return Util.toHex(id);
	}

}
