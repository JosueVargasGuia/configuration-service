package com.nttdata.configurationservice;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.nttdata.configurationservice.controller.ConfigurationController;
import com.nttdata.configurationservice.entity.Configuration;
import com.nttdata.configurationservice.service.ConfigurationService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ConfigurationController.class)
class ConfigurationServiceApplicationTests {

	
	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private ConfigurationService configurationService;
	
	
	@Test
	void addConfiguration() {
		
		Configuration configuration = new Configuration(10L,3.0,1,"20",1,20.0,100.0,2.5,Calendar.getInstance().getTime(),Calendar.getInstance().getTime());
		//Mono<ResponseEntity<Configuration>> 
		when(configurationService.save(configuration)).thenReturn(Mono.just(configuration));
		
		webTestClient
			.post().uri("/configuration")
			.body(Mono.just(configuration),Configuration.class)
			.exchange()
			.expectStatus().isOk();
	}
	
	
	@Test
	void getConfigurationsTest() {
		
		//var null = Calendar.getInstance().getTime();
		
		Flux<Configuration> conFlux = Flux.just(
				new Configuration(10L,3.0,1,"20",1,20.0,100.0,2.5,null,null),
				new Configuration(11L,4.0,1,"20",1,20.0,100.0,2.5,null,null));
	
		when(configurationService.findAll()).thenReturn(conFlux);
		
		Flux<Configuration> responseFluxConfiguration = webTestClient
		.get().uri("/configuration")
		.exchange()
		.expectStatus().isOk()
		.returnResult(Configuration.class)
		.getResponseBody();
	
		StepVerifier.create(responseFluxConfiguration)
		.expectSubscription()
		.expectNext(new Configuration(10L,3.0,1,"20",1,20.0,100.0,2.5,null,null))
		.expectNext(new Configuration(11L,4.0,1,"20",1,20.0,100.0,2.5,null,null))
		.verifyComplete();
	}
	
	
	@Test
	void getConfigurationTest() {
		
		Mono<Configuration> configuration = Mono.just(new Configuration(11L,3.0,1,"20",1,20.0,100.0,2.5,Calendar.getInstance().getTime(),Calendar.getInstance().getTime()));
		
		when(configurationService.findById(anyLong())).thenReturn(configuration);
		
		var configurationResponse = webTestClient
			.get().uri("/configuration/11")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Configuration.class)
			.getResponseBody();
		
		StepVerifier.create(configurationResponse)
		.expectSubscription()
		.expectNextMatches(c->c.getIdConfiguration()==(11L))
		.verifyComplete();
		
	}
	
	
	@Test
	void updateConfigurationTest() {
		
		Configuration configuration = new Configuration(11L,3.0,1,"20",1,20.0,100.0,2.5,Calendar.getInstance().getTime(),Calendar.getInstance().getTime());
	
		when(configurationService.update(configuration)).thenReturn(Mono.just(configuration));
		
		webTestClient.put().uri("/configuration")
		.body(Mono.just(configuration),Configuration.class)
		.exchange()
		.expectStatus().isOk();
	}
	
	
	
	

}
