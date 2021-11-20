package com.avaliacao.meeting_agenda.document;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class Base {
	@Id
	private String id;
	@NotBlank(message = "The username must contain at least one character")
	private String username;
	@NotBlank(message = "The userCpf must contain at least one character")
	private String userCpf;
	private LocalDateTime lastedUpdateDate;

	public Base(String id, @NotBlank(message = "The username must contain at least one character") String username,
			@NotBlank(message = "The userCpf must contain at least one character") String userCpf,
			LocalDateTime lastedUpdateDate) {
		super();
		this.id = id;
		this.username = username;
		this.userCpf = userCpf;
		this.lastedUpdateDate = lastedUpdateDate;
	}

}
