package com.sib.sayitbettr.Service;

import com.sib.sayitbettr.Model.Evaluation;
import com.sib.sayitbettr.Model.User;
import com.sib.sayitbettr.Model.Word;
import com.sib.sayitbettr.Repository.EvaluationRepository;
import com.sib.sayitbettr.Repository.UserRepository;
import com.sib.sayitbettr.Repository.WordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public Evaluation saveEvaluation(Long userId, Long wordId, String pronunciation, double accuracy, int score, LocalDateTime date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Word word = wordRepository.findById(wordId)
                .orElseThrow(() -> new RuntimeException("Word not found"));

        Evaluation evaluation = new Evaluation();
        evaluation.setUser(user);
        evaluation.setWord(word);
        evaluation.setPronunciation(pronunciation);
        evaluation.setAccuracy(accuracy);
        evaluation.setScore(score);
        evaluation.setDate(date);

        return evaluationRepository.save(evaluation);
    }

    public List<Evaluation> getEvaluationsByUserId(Long userId) {
        return evaluationRepository.findByUserId(userId);
    }

    public List<Evaluation> getEvaluationsByWordId(Long wordId) {
        return evaluationRepository.findByWordId(wordId);
    }

    public Long countEvaluationsByUserId(Long userId) {
        return evaluationRepository.countByUserId(userId);
    }

    public Long countEvaluationsByUserIdAndLevel(Long userId, int level) {
        return evaluationRepository.countByUserIdAndWordLevel(userId, level);
    }

    public List<Evaluation> getLast5EvaluationsByUser(Long userId) {
        return evaluationRepository.findTop5ByUserIdOrderByDateDesc(userId);
    }
/*
    public int getUserTotalScore(Long userId) {
        List<Evaluation> evaluations = evaluationRepository.findByUserId(userId);
        return evaluations.stream()
                .mapToInt(Evaluation::getScore)
                .sum();
    }

    public List<User> getUserRanking() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .sorted((u1, u2) -> Integer.compare(getUserTotalScore(u2.getId()), getUserTotalScore(u1.getId())))
                .collect(Collectors.toList());
    }

 */
public Integer getUserTotalScore(Long userId) {
    return evaluationRepository.getTotalScoreByUserId(userId).orElse(0);
}

    public List<Map<String, Object>> getUserRanking() {
        List<Object[]> results = evaluationRepository.getUserRankings();
        List<Map<String, Object>> rankings = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", result[0]);
            map.put("name", result[1]);
            map.put("surname", result[2]);
            map.put("totalScore", result[3]);
            rankings.add(map);
        }

        return rankings;
    }

    public Integer getUserRank(Long userId) {
        List<Map<String, Object>> rankings = getUserRanking();

        for (int rank = 0; rank < rankings.size(); rank++) {
            Map<String, Object> ranking = rankings.get(rank);
            Long currentUserId = (Long) ranking.get("userId");
            if (currentUserId.equals(userId)) {
                return rank + 1;  // Rank starts from 1
            }
        }
        return -1;  // If user not found in rankings
    }
}
