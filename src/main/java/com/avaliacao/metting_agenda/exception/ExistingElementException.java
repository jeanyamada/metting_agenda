package com.avaliacao.metting_agenda.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ExistingElementException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExistingElementException(String string) {
		super(string);
	}

	public ExistingElementException(ExistingElementException e) {
		super(e.getMessage());
	}

}
