package com.nttdata.configurationservice.repository;

 
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.configurationservice.entity.*;

 
@Repository
public interface ConfigurationRepository extends ReactiveMongoRepository<Configuration, Long> {

 
	
}
