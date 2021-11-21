package com.avaliacao.meeting_agenda.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.avaliacao.meeting_agenda.dto.CpfApiResponse;
import com.avaliacao.meeting_agenda.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	@Value("${cpf.validation}")
	public String cpfValidationAPI;

	public Boolean validateUserCpf(String cpf) {

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
