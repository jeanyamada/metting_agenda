package com.avaliacao.metting_agenda.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VotingMettringAgendaTotal {
	private String mettingAgendaName;
	private String votingType;
	private Long total;
}
