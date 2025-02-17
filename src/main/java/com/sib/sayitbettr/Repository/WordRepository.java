package com.sib.sayitbettr.Repository;

import com.sib.sayitbettr.Model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByLevel(int level);
    Optional<Word> findById(Long id);


}
