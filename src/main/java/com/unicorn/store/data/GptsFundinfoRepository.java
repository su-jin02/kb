package com.unicorn.store.data;

import com.unicorn.store.model.GptsFundinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface GptsFundinfoRepository extends JpaRepository<GptsFundinfo, Long> {
}
