package com.avaliacao.metting_agenda.document;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class MettingAgenda extends Base {

	@NotBlank(message = "The votingSessionName must contain at least one character")
	private String mettingAgendaName;
	private String mettingAgendaDescription;

	private LocalDateTime votingSessionStart;

	private LocalDateTime votingSessionEnd;

	// time in minutes
	private Long sessionTimeMax;

	public MettingAgenda(String id,
			@NotBlank(message = "The username must contain at least one character") String username,
			@NotBlank(message = "The userCpf must contain at least one character") String userCpf,
			LocalDateTime lastedUpdateDate,
			@NotBlank(message = "The votingSessionName must contain at least one character") String mettingAgendaName,
			String mettingAgendaDescription, LocalDateTime votingSessionStart, LocalDateTime votingSessionEnd,
			Long sessionTimeMax) {
		super(id, username, userCpf, lastedUpdateDate);
		this.mettingAgendaName = mettingAgendaName;
		this.mettingAgendaDescription = mettingAgendaDescription;
		this.votingSessionStart = votingSessionStart;
		this.votingSessionEnd = votingSessionEnd;
		this.sessionTimeMax = sessionTimeMax;
	}

}
