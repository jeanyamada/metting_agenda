package com.avaliacao.meeting_agenda.view;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VotingMeetingAgendaTotal {
	private String meetingAgendaName;
	private String votingType;
	private Long total;
}
