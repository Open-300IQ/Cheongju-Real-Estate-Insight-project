package com.example.iq300.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "finaldata")
@NoArgsConstructor
@Getter
@Setter
public class FinalData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long Id;
	
	private String areaName;
	private String contractMonth;
	private String contractType;
	private Double price;
	private Long count;
	private String buildingType;
	
}
