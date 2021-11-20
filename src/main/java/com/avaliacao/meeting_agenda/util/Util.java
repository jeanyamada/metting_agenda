package com.avaliacao.meeting_agenda.util;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.avaliacao.meeting_agenda.view.CpfApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Util {

	@Value("${cpf.validation}")
	public static String cpfValidationAPI;

	public static String toHex(String arg) {
		if (Util.isNull(arg)) {
			return null;
		}

		return String.format("%040x", new BigInteger(1, arg.getBytes(Charset.defaultCharset())));
	}

	public static <T> boolean isNull(final T e) {
		return e == null;
	}

	public static <T> boolean isNullOrEmpty(final List<T> c) {
		return isNull(c) || c.isEmpty();
	}

	public static String concat(Object... objects) {

		if (Util.isNull(objects)) {
			return null;
		}

		Iterator<Object> iterator = Arrays.stream(objects).iterator();

		StringBuilder concatened = new StringBuilder();

		while (iterator.hasNext()) {

			concatened.append(iterator.next());

			if (iterator.hasNext()) {
				concatened.append("|");
			}
		}

		return concatened.toString();
	}

	public static <T> List<T> getSingletonList(T o) {
		if (!Util.isNull(o)) {
			return Collections.singletonList(o);
		} else {
			return null;
		}
	}

	public static <T> T ifNull(T object, T value) {
		if (isNull(object)) {
			return value;
		}
		return object;
	}

	public static Boolean isAfterOrEquals(LocalDateTime a, LocalDateTime b) {

		if (a.equals(b) || a.isAfter(b)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static Boolean isBeforeOrEquals(LocalDateTime a, LocalDateTime b) {
		if (a.equals(b) || a.isBefore(b)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static Boolean validateUserCpf(String cpf) {

		log.info("started validateUserCpf");

		RestTemplate restTemplate = new RestTemplate();

		String userCpfFormated = Util.isNull(cpf) ? "" : cpf.replaceAll("[^0-9]", "");

		log.info(String.format("cpf: [%s]", userCpfFormated));

		ResponseEntity<CpfApiResponse> responseEntity = restTemplate
				.getForEntity(String.format("%s/%s", cpfValidationAPI, userCpfFormated), CpfApiResponse.class);

		if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
			CpfApiResponse cpfApiResponse = responseEntity.getBody();

			log.info(String.format("cpf: [%s] -> [%s]", cpf, cpfApiResponse.getStatus()));

			return cpfApiResponse.getStatus().equals("ABLE_TO_VOTE");
		}

		return Boolean.FALSE;
	}

}