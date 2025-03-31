package com.sib.sayitbettr.Repository;

import com.sib.sayitbettr.Model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByLevel(int level);

    @Query("SELECT COUNT(w) FROM Word w")
    Long countTotalWords();

    @Query("SELECT COUNT(w) FROM Word w WHERE w.level = :level")
    Long countWordsByLevel(int level);
}
