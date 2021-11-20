package com.avaliacao.metting_agenda.document;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class VotingMettingAgenda extends Base {

	@NotBlank(message = "The mettingAgendaName must contain at least one character")
	private String mettingAgendaName;
	private String mettingAgendaDescription;

	@NotBlank(message = "The votingType must contain at least one character")
	private String votingType;

	public VotingMettingAgenda(String id,
			@NotBlank(message = "The username must contain at least one character") String username,
			@NotBlank(message = "The userCpf must contain at least one character") String userCpf,
			LocalDateTime lastedUpdateDate,
			@NotBlank(message = "The mettingAgendaName must contain at least one character") String mettingAgendaName,
			String mettingAgendaDescription,
			@NotBlank(message = "The votingType must contain at least one character") String votingType) {
		super(id, username, userCpf, lastedUpdateDate);
		this.mettingAgendaName = mettingAgendaName;
		this.mettingAgendaDescription = mettingAgendaDescription;
		this.votingType = votingType;
	}

}
