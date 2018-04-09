package com.cheuks.bin.spring.boot.data.jpa.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "demo")
@AllArgsConstructor
@NoArgsConstructor
public class DemoModel implements Serializable {

	private static final long serialVersionUID = -3250747959972966370L;

	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Setter
	@Getter
	private String name;

	public DemoModel(String name) {
		super();
		this.name = name;
	}

}
