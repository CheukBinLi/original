package com.cheuks.bin.spring.boot.data.jpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.CrudRepository;

import com.cheuks.bin.spring.boot.data.jpa.model.DemoModel;

public interface DemoDao extends JpaRepository<DemoModel, Long>/* , CrudRepository<DemoModel, Long> */ {

}
