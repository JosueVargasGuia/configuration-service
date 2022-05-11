package com.nttdata.configurationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.configurationservice.entity.Configuration;
import com.nttdata.configurationservice.service.ConfigurationService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/configuration")
public class ConfigurationController {


	@Autowired
	ConfigurationService configurationService;

	@GetMapping
	public Flux<Configuration> findAll() {
		return configurationService.findAll();
	}

	@PostMapping
	public Mono<ResponseEntity<Configuration>> save(@RequestBody Configuration configuration) {
		return configurationService.save(configuration)
		.map(_configuration -> ResponseEntity.ok().body(_configuration))
		.onErrorResume(e -> {
			log.info("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		});
	}
	

	@GetMapping("/{idConfiguration}")
	public Mono<ResponseEntity<Configuration>> findById(@PathVariable(name = "idConfiguration") long idConfiguration) {
		return configurationService.findById(idConfiguration)
				.map(configuration -> ResponseEntity.ok().body(configuration)).onErrorResume(e -> {
					log.info(e.getMessage());
					return Mono.just(ResponseEntity.badRequest().build());
				}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	
	@PutMapping
	public Mono<ResponseEntity<Configuration>> update(@RequestBody Configuration configuration) {

		Mono<Configuration> mono = configurationService.findById(configuration.getIdConfiguration())
				.flatMap(objConfiguration -> {
					log.info("Update:[new]" + configuration + " [Old]:" + objConfiguration);
					return configurationService.update(configuration);
				});

		return mono.map(_configuration -> {
			log.info("Status:" + HttpStatus.OK);
			return ResponseEntity.ok().body(_configuration);
		}).onErrorResume(e -> {
			log.info("Status:" + HttpStatus.BAD_REQUEST + " menssage" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());

	}

	@DeleteMapping("/{idConfiguration}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable(name = "idConfiguration") long idConfiguration) {
		return configurationService.findById(idConfiguration).flatMap(configuration -> {
			return configurationService.delete(configuration.getIdConfiguration())
					.then(Mono.just(ResponseEntity.ok().build()));
		});
	}

	@GetMapping("/fillData")
	public Mono<Void> fillData() {
		return configurationService.fillData();
	}
}
