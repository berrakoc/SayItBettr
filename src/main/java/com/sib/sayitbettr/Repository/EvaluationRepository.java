package com.sib.sayitbettr.Repository;

import com.sib.sayitbettr.Model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByUserId(Long userId);
    List<Evaluation> findByWordId(Long wordId);
}
