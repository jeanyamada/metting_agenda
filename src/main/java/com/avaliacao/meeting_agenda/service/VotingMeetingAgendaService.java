package com.avaliacao.meeting_agenda.service;

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

import com.avaliacao.meeting_agenda.dto.VotingMeetingAgendaTotal;
import com.avaliacao.meeting_agenda.exception.ExistingElementException;
import com.avaliacao.meeting_agenda.exception.NotFoundException;
import com.avaliacao.meeting_agenda.model.MeetingAgenda;
import com.avaliacao.meeting_agenda.model.VotingMeetingAgenda;
import com.avaliacao.meeting_agenda.repository.VotingMeetingAgendaRepository;
import com.avaliacao.meeting_agenda.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VotingMeetingAgendaService {

	@Autowired
	private VotingMeetingAgendaRepository votingMeetingAgendaRepository;

	@Autowired
	private MeetingAgendaService meetingAgendaService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private MongoTemplate mongoTemplate;

	public VotingMeetingAgenda insertVotingMeetingAgenda(VotingMeetingAgenda data)
			throws NotFoundException, ExistingElementException, AccessException {

		log.info("started insertVotingMeetingAgenda");

		LocalDateTime current = LocalDateTime.now();

		if (!userService.validateUserCpf(data.getUserCpf())) {
			String info = String.format("user: [%s] does not have a valid CPF: [%s]", data.getUsername(),
					data.getUserCpf());
			log.error(info);
			throw new AccessException(info);
		}

		String votingMeetingId = idGenerator(data.getUserCpf(), data.getMeetingAgendaName());
		VotingMeetingAgenda votingMeetingAgenda = findById(votingMeetingId);

		if (!Util.isNull(votingMeetingAgenda)) {
			String info = String.format(
					"the VotingMeetingAgenda element already exists, username: [%s]  meetingAgendaName: [%s]",
					data.getUsername(), data.getMeetingAgendaName());
			log.error(info);
			throw new ExistingElementException(info);
		}

		String meetingAgendaId = meetingAgendaService.idGenerator(data.getMeetingAgendaName());
		MeetingAgenda meetingAgenda = meetingAgendaService.findById(meetingAgendaId);

		if (Util.isNull(meetingAgenda)) {
			String info = String.format("not found meetingAgenda: [%s]", data.getMeetingAgendaName());
			log.error(info);
			throw new NotFoundException(info);
		}

		if (!(Util.isAfterOrEquals(current, meetingAgenda.getVotingSessionStart())
				&& Util.isBeforeOrEquals(current, meetingAgenda.getVotingSessionEnd()))) {
			String info = String.format("the meetingAgenda: [%s] is not open for voting", data.getMeetingAgendaName());
			log.error(info);
			throw new NotFoundException(info);
		}

		LocalDateTime lastedUpdateDate = LocalDateTime.now();

		data.setLastedUpdateDate(lastedUpdateDate);
		data.setId(votingMeetingId);

		log.info(String.format("saving VotingMeetingAgenda: [%s]", data.getId()));
		return save(data);

	}

	public List<VotingMeetingAgendaTotal> votingResult(String meetingAgendaName) {

		log.info("started votingResult");

		List<AggregationOperation> fetchOperations = new ArrayList<AggregationOperation>();

		fetchOperations.add(Aggregation.match(Criteria.where("meetingAgendaName").is(meetingAgendaName)));
		fetchOperations.add(Aggregation.group("meetingAgendaName", "votingType").count().as("total"));
		fetchOperations.add(Aggregation.project("meetingAgendaName", "votingType", "total"));
		AggregationOptions options = AggregationOptions.builder().allowDiskUse(true).build();
		Aggregation aggregation = Aggregation.newAggregation(fetchOperations).withOptions(options);

		log.info(String.format("votingResult aggregation : [%s]", aggregation.toString()));

		AggregationResults<VotingMeetingAgendaTotal> data = mongoTemplate.aggregate(aggregation,
				VotingMeetingAgenda.class, VotingMeetingAgendaTotal.class);

		return data.getMappedResults();
	}

	public VotingMeetingAgenda findById(String id) {
		Optional<VotingMeetingAgenda> op = votingMeetingAgendaRepository.findById(id);
		return op.isPresent() ? op.get() : null;
	}

	public VotingMeetingAgenda save(VotingMeetingAgenda votingMeetingAgenda) {
		return votingMeetingAgendaRepository.save(votingMeetingAgenda);
	}
	public void remove(VotingMeetingAgenda votingMeetingAgenda) {
		votingMeetingAgendaRepository.delete(votingMeetingAgenda);
	}

	public String idGenerator(String userCpf, String meetingAgendaName) {
		String id = Util.concat(userCpf, meetingAgendaName);
		return Util.toHex(id);
	}

}
