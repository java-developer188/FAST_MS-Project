package com.fast.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fast.project.entity.Section;

@Repository
public interface SectionRepository extends CrudRepository<Section, Long> {

}
