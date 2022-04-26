package com.nttdata.configurationservice.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.configurationservice.FeignClient.FallBackImpl.TableIdFeignClientFallBack;

@FeignClient(name = "${api.tableId-service.uri}", fallback = TableIdFeignClientFallBack.class)
public interface TableIdFeignClient {

	@GetMapping("/generateKey/{nameTable}")
	public Long generateKey(@PathVariable(name = "nameTable") String nameTable);
}
