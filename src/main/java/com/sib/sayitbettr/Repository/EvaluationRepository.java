package com.sib.sayitbettr.Repository;

import com.sib.sayitbettr.Model.Evaluation;
import com.sib.sayitbettr.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByUserId(Long userId);
    List<Evaluation> findByWordId(Long wordId);

    Long countByUserId(Long userId);
    Long countByUserIdAndWordLevel(Long userId, int level);

    List<Evaluation> findTop5ByUserIdOrderByDateDesc(Long userId);

    @Query("SELECT SUM(e.score) FROM Evaluation e WHERE e.user.id = :userId")
    Optional<Integer> getTotalScoreByUserId(Long userId);

    @Query("SELECT e.user.id, e.user.name, e.user.surname, SUM(e.score) as totalScore " +
            "FROM Evaluation e GROUP BY e.user.id, e.user.name, e.user.surname " +
            "ORDER BY totalScore DESC")
    List<Object[]> getUserRankings();


}
