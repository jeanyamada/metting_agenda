package com.avaliacao.meeting_agenda.document;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class VotingMeetingAgenda extends Base {

	@NotBlank(message = "The mettingAgendaName must contain at least one character")
	private String meetingAgendaName;
	private String meetingAgendaDescription;

	@NotBlank(message = "The votingType must contain at least one character")
	private String votingType;

	public VotingMeetingAgenda(String id,
			@NotBlank(message = "The username must contain at least one character") String username,
			@NotBlank(message = "The userCpf must contain at least one character") String userCpf,
			LocalDateTime lastedUpdateDate,
			@NotBlank(message = "The mettingAgendaName must contain at least one character") String meetingAgendaName,
			String meetingAgendaDescription,
			@NotBlank(message = "The votingType must contain at least one character") String votingType) {
		super(id, username, userCpf, lastedUpdateDate);
		this.meetingAgendaName = meetingAgendaName;
		this.meetingAgendaDescription = meetingAgendaDescription;
		this.votingType = votingType;
	}

}
