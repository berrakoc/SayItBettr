package com.sib.sayitbettr.Service;

import com.sib.sayitbettr.Model.Evaluation;
import com.sib.sayitbettr.Model.User;
import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Repository.EvaluationRepository;
import com.sib.sayitbettr.Repository.UserRepository;
import com.sib.sayitbettr.Repository.WordRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;

    public EvaluationService(EvaluationRepository evaluationRepository, UserRepository userRepository, WordRepository wordRepository) {
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
        this.wordRepository = wordRepository;
    }

    public Evaluation saveEvaluation(Long userId, Long wordId, String pronunciation, double accuracy, int score) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Word> wordOpt = wordRepository.findById(wordId);

        if (userOpt.isEmpty() || wordOpt.isEmpty()) {
            throw new RuntimeException("User veya Word bulunamadı.");
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setUser(userOpt.get());
        evaluation.setWord(wordOpt.get());
        evaluation.setPronunciation(pronunciation);
        evaluation.setAccuracy(accuracy);
        evaluation.setDate(LocalDateTime.now());
        evaluation.setScore(score);

        return evaluationRepository.save(evaluation);
    }

    public List<Evaluation> getEvaluationsByUserId(Long userId) {
        return evaluationRepository.findByUserId(userId);
    }

    public List<Evaluation> getEvaluationsByWordId(Long wordId) {
        return evaluationRepository.findByWordId(wordId);
    }
}
