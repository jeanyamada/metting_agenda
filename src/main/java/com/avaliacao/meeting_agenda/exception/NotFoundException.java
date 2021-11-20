package com.avaliacao.meeting_agenda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotFoundException(String string) {
		super(string);
	}

	public NotFoundException(NotFoundException e) {
		super(e.getMessage());
	}

}
