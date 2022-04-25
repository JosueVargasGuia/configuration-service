package com.nttdata.configurationservice.service;

import com.nttdata.configurationservice.entity.Configuration;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConfigurationService {

	Flux<Configuration> findAll();

	Mono<Configuration> findById(long idConfiguration);

	Mono<Configuration> save(Configuration configuration);

	Mono<Configuration> update(Configuration configuration);

	Mono<Void> delete(Long id);

	Mono<Void> fillData();

	Long generateKey(String nameTable);

}
