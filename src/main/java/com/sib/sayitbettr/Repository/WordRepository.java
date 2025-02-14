package com.sib.sayitbettr.Repository;

import com.sib.sayitbettr.Model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByLevel(int level);
}

