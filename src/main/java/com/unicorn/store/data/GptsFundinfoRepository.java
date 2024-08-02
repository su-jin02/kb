package com.unicorn.store.data;

import com.unicorn.store.model.GptsFundinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GptsFundinfoRepository extends JpaRepository<GptsFundinfo, Long> {
}
