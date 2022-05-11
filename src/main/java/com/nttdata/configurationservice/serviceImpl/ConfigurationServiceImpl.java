package com.nttdata.configurationservice.serviceImpl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nttdata.configurationservice.FeignClient.TableIdFeignClient;
import com.nttdata.configurationservice.entity.Configuration;
import com.nttdata.configurationservice.repository.ConfigurationRepository;
import com.nttdata.configurationservice.service.ConfigurationService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
	Logger log = LoggerFactory.getLogger(ConfigurationService.class);
	@Autowired
	ConfigurationRepository configurationRepository;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	TableIdFeignClient tableIdFeignClient;
	
	@Value("${api.tableId-service.uri}")
	String tableIdService;

	@Override
	public Flux<Configuration> findAll() {
		// TODO Auto-generated method stub
		return configurationRepository.findAll()
				.sort((config1, config2) -> config1.getIdConfiguration().compareTo(config2.getIdConfiguration()));
	}

	@Override
	public Mono<Configuration> save(Configuration configuration) {
		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idConfiguration;
		if (count != null) {
			if (count <= 0) {
				idConfiguration = Long.valueOf(0);
			} else {
				idConfiguration = this.findAll().collect(Collectors.maxBy(Comparator.comparing(Configuration::getIdConfiguration)))
						.blockOptional().get().get().getIdConfiguration();
			}
		} else {
			idConfiguration = Long.valueOf(0);
		}
		configuration.setIdConfiguration(idConfiguration+1);
		configuration.setCreationDate(Calendar.getInstance().getTime());
		return configurationRepository.insert(configuration);
	}


	@Override
	public Mono<Configuration> findById(long idConfiguration) {
		// TODO Auto-generated method stub
		return configurationRepository.findById(idConfiguration);
	}

	@Override
	public Mono<Configuration> update(Configuration configuration) {
		// Verificar logica si aplica la busqueda del flatMap
		/*
		 * Mono<Configuration> mono =
		 * configurationRepository.findById(configuration.getIdConfiguration())
		 * .flatMap(objConfiguration -> { log.info("Update:[new]"+configuration
		 * +" [Old]:"+objConfiguration); return
		 * configurationRepository.save(configuration); });
		 */		
		return configurationRepository.save(configuration);
	}

	@Override
	public Mono<Void> delete(Long id) {
		/*
		 * Mono<Void> mono = configurationRepository.findById(id) .flatMap(configuration
		 * -> { return configurationRepository.delete(configuration); }); return mono;
		 */
		return configurationRepository.deleteById(id);
	}

	Long maxValue = Long.valueOf(0);// Buscar una solucion para el identity en mongodb

	@Override
	public Mono<Void> fillData() {
		Mono<Void> mono = configurationRepository.findAll().count().flatMap(x -> {
			log.info("Cantidad[X]:" + x);

			// if (x <= 0) {
			List<Configuration> listaConfigurations = new ArrayList<Configuration>();
			/*
			 * Long idConfiguration; double costMaintenance;// Costo de mantenimiento int
			 * quantityMovement;// Total de movimientos // TypeMovement TypeMovement;//tipo
			 * int quantityCredit;// Cantidad de movimientos permitidos, si solo permite un
			 * dia de moviento se // especifica fecha int specificDate;
			 */
			/*
			 * idConfiguration,costMaintenance,quantityMovement,quantityCredit,specificDate
			 */
			// Ahorro: libre de comisión por mantenimiento y con un límite máximo de
			// movimientos mensuales.
			listaConfigurations.add(new Configuration(Long.valueOf(1), null, 5, null, null, null, null,  null, null,  null));
			//  Cuenta corriente: posee comisión de mantenimiento y sin límite de
			// movimientos mensuales.
			listaConfigurations.add(new Configuration(Long.valueOf(2), 10.00, null, null, null, null, null,  null, null,  null));
			//  Plazo fijo: libre de comisión por mantenimiento, solo permite un movimiento
			// de retiro o depósito en un día específico del mes.
			listaConfigurations.add(new Configuration(Long.valueOf(3), null, Integer.valueOf(1), "12", null, null, null,  null, null,  null));
			//  Personal: solo se permite un solo crédito por persona.
			listaConfigurations.add(new Configuration(Long.valueOf(4),  null, null, null, 1, null, null, null, null, null));
			//  Empresarial: se permite más de un crédito por empresa.
			listaConfigurations.add(new Configuration(Long.valueOf(5), null, null, null, null, null, null,  null, null,  null));
			//  Tarjeta de Crédito empresarial.
			listaConfigurations.add(new Configuration(Long.valueOf(6), null, null, null, null, null, null,  null, null,  null));
			//  Tarjeta de Crédito empresarial.
			listaConfigurations.add(new Configuration(Long.valueOf(7), null, null, null, null, null, null,  null, null,  null));

			maxValue = Long.valueOf(x);
			return Flux.fromIterable(listaConfigurations).flatMap(configurations -> {
				maxValue = maxValue + 1;
				log.info("[product]:" + configurations + "c:" + x);
				configurations.setIdConfiguration(maxValue);
				return this.save(configurations);
			}).then();

			// } else {
			// log.info("There are data");
			// return Mono.just("There are data configurations");
			// }
		}).then();
		return mono;
	}

	@Override
	public Long generateKey(String nameTable) {
		log.info(tableIdService + "/generateKey/" + nameTable);
		/*ResponseEntity<Long> responseGet = restTemplate.exchange(tableIdService + "/generateKey/" + nameTable,
				HttpMethod.GET, null, new ParameterizedTypeReference<Long>() {
				});
		if (responseGet.getStatusCode() == HttpStatus.OK) {
			log.info("Body:" + responseGet.getBody());
			return responseGet.getBody();
		} else {
			return Long.valueOf(0);
		}*/
		return tableIdFeignClient.generateKey(nameTable);
	}
}
