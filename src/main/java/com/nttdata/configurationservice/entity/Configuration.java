package com.nttdata.configurationservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "configuration")
public class Configuration {
	@Id
	Long idConfiguration;
	Double costMaintenance;// Costo de mantenimiento
	Integer quantityMovement;// Total de movimientos
	// TypeMovement TypeMovement;//tipo
	Integer quantityCredit;// Cantidad de movimientos permitidos, si solo permite un dia de moviento se
						// especifica fecha
	String specificDate;
	
	@Override
	public String toString() {
		return "Configuration [idConfiguration=" + idConfiguration + ", costMaintenance=" + costMaintenance
				+ ", quantityMovement=" + quantityMovement + ", quantityCredit=" + quantityCredit + ", specificDate="
				+ specificDate + "]";
	}
	
	
}
