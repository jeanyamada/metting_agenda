package com.avaliacao.metting_agenda;

import java.rmi.AccessException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.avaliacao.meeting_agenda.MeetingAgendaApplication;
import com.avaliacao.meeting_agenda.dto.VotingMeetingAgendaTotal;
import com.avaliacao.meeting_agenda.exception.ExistingElementException;
import com.avaliacao.meeting_agenda.exception.NotFoundException;
import com.avaliacao.meeting_agenda.model.MeetingAgenda;
import com.avaliacao.meeting_agenda.model.VotingMeetingAgenda;
import com.avaliacao.meeting_agenda.service.MeetingAgendaService;
import com.avaliacao.meeting_agenda.service.UserService;
import com.avaliacao.meeting_agenda.service.VotingMeetingAgendaService;
import com.avaliacao.meeting_agenda.type.VotingType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MeetingAgendaApplication.class)
@ActiveProfiles("localhost")
class VotingMeetingAgendaApplicationTests {

	@Value("${server.port}")
	private Long port;

	@Value("${time.default}")
	private Long timeDefault;

	@TestConfiguration
	static class VotingMeetingAgendaServiceTestConfiguration {
		@Bean
		public VotingMeetingAgendaService votingMeetingAgendaService() {
			return new VotingMeetingAgendaService();
		}
	}

	@Autowired
	private VotingMeetingAgendaService votingMeetingAgendaService;

	@MockBean
	private MeetingAgendaService meetingAgendaService;

	@MockBean
	private UserService userService;

	@Test
	public void insertVotingMeetingAgenda() {
		String username = "admin@teste.com";
		String userCpf = "437.800.900-58";
		String meetingAgendaName = "PAUTA_100";

		VotingMeetingAgenda votingMeetingAgenda = new VotingMeetingAgenda(username, userCpf, null, meetingAgendaName,
				null, "Sim");

		try {
			VotingMeetingAgenda result = votingMeetingAgendaService.insertVotingMeetingAgenda(votingMeetingAgenda);

			Assertions.assertEquals(result.getMeetingAgendaName(), result.getMeetingAgendaName());

			List<VotingMeetingAgendaTotal> total = votingMeetingAgendaService
					.votingResult(result.getMeetingAgendaName());

			Assertions.assertEquals(1, total.size());
			Assertions.assertEquals(VotingType.SIM, total.get(0).getVotingType());

		} catch (AccessException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ExistingElementException e) {
			e.printStackTrace();
		}
	}

	@BeforeEach
	public void init() {
		String username = "admin@teste.com";
		String userCpf = "437.800.900-58";
		String meetingAgendaName = "PAUTA_100";
		String meetingAgendaDescription = "Teste PAUTA_100";
		LocalDateTime lastedUpdateDate = LocalDateTime.now();
		Long sessionTimeMax = timeDefault;
		LocalDateTime votingSessionStart = LocalDateTime.now();
		LocalDateTime votingSessionEnd = votingSessionStart.plusMinutes(sessionTimeMax);

		String meetingAgendaId = meetingAgendaService.idGenerator(meetingAgendaName);

		MeetingAgenda meetingAgenda = new MeetingAgenda(meetingAgendaId, username, userCpf, lastedUpdateDate,
				meetingAgendaName, meetingAgendaDescription, votingSessionStart, votingSessionEnd, sessionTimeMax);

		Mockito.when(meetingAgendaService.findById(meetingAgenda.getId())).thenReturn(meetingAgenda);
		Mockito.when(userService.validateUserCpf(userCpf)).thenReturn(Boolean.TRUE);

	}

	@AfterEach
	public void finish() {
		String username = "admin@teste.com";
		String userCpf = "437.800.900-58";
		String meetingAgendaName = "PAUTA_100";
		String meetingAgendaDescription = "Teste PAUTA_100";
		VotingType votingType = VotingType.getEnum("Não");
		LocalDateTime lastedUpdateDate = LocalDateTime.now();

		String votinMeetingAgendaId = votingMeetingAgendaService.idGenerator(userCpf, meetingAgendaName);
		VotingMeetingAgenda votingMeetingAgenda = new VotingMeetingAgenda(votinMeetingAgendaId, username, userCpf,
				lastedUpdateDate, meetingAgendaName, meetingAgendaDescription, votingType);

		votingMeetingAgendaService.remove(votingMeetingAgenda);
	}

}
