package com.avaliacao.meeting_agenda.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class MeetingAgenda extends Base {

	@NotBlank(message = "The votingSessionName must contain at least one character")
	private String meetingAgendaName;
	private String meetingAgendaDescription;

	private LocalDateTime votingSessionStart;

	private LocalDateTime votingSessionEnd;

	// time in minutes
	private Long sessionTimeMax;

	public MeetingAgenda(String id,
			@NotBlank(message = "The username must contain at least one character") String username,
			@NotBlank(message = "The userCpf must contain at least one character") String userCpf,
			LocalDateTime lastedUpdateDate,
			@NotBlank(message = "The votingSessionName must contain at least one character") String meetingAgendaName,
			String meetingAgendaDescription, LocalDateTime votingSessionStart, LocalDateTime votingSessionEnd,
			Long sessionTimeMax) {
		super(id, username, userCpf, lastedUpdateDate);
		this.meetingAgendaName = meetingAgendaName;
		this.meetingAgendaDescription = meetingAgendaDescription;
		this.votingSessionStart = votingSessionStart;
		this.votingSessionEnd = votingSessionEnd;
		this.sessionTimeMax = sessionTimeMax;
	}

}
