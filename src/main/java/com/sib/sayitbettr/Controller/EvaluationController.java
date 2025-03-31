package com.sib.sayitbettr.Controller;

import com.sib.sayitbettr.Model.Evaluation;
import com.sib.sayitbettr.Model.EvaluationRequest;
import com.sib.sayitbettr.Service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping("/save")
    public ResponseEntity<Evaluation> saveEvaluation(@RequestBody EvaluationRequest request) {
        Evaluation evaluation = evaluationService.saveEvaluation(
                request.getUserId(),
                request.getWordId(),
                request.getPronunciation(),
                request.getAccuracy(),
                request.getScore(),
                request.getDate()
        );
        return ResponseEntity.ok(evaluation);
    }

    @GetMapping("/user/{userId}") //kullanıcının çözdüğü kelimeler
    public ResponseEntity<List<Evaluation>> getEvaluationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByUserId(userId));
    }

    @GetMapping("/word/{wordId}") //kelimeyi çözen kullanıcılar
    public ResponseEntity<List<Evaluation>> getEvaluationsByWord(@PathVariable Long wordId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByWordId(wordId));
    }

    @GetMapping("/user/{userId}/count") //kullanıcının çözdüğü toplam soru sayısını verir
    public ResponseEntity<Long> getTotalEvaluationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(evaluationService.countEvaluationsByUserId(userId));
    }

    @GetMapping("/user/{userId}/level/{level}/count") //kullanıcının levela göre çözdüğü toplam soru sayısını verir
    public ResponseEntity<Long> getEvaluationsByLevel(@PathVariable Long userId, @PathVariable int level) {
        return ResponseEntity.ok(evaluationService.countEvaluationsByUserIdAndLevel(userId, level));
    }

    @GetMapping("/user/{userId}/last5") //history
    public ResponseEntity<List<Evaluation>> getLast5Evaluations(@PathVariable Long userId) {
        return ResponseEntity.ok(evaluationService.getLast5EvaluationsByUser(userId));
    }

    @GetMapping("/user/{userId}/score") // Kullanıcının toplam skorunu getirir
    public ResponseEntity<Integer> getUserTotalScore(@PathVariable Long userId) {
        return ResponseEntity.ok(evaluationService.getUserTotalScore(userId));
    }

    @GetMapping("/ranking") // Kullanıcıları skorlarına göre sıralıyor, belki lazım olur
    public ResponseEntity<List<Map<String, Object>>> getUserRanking() {
        return ResponseEntity.ok(evaluationService.getUserRanking());
    }

    @GetMapping("/user/{userId}/rank") // Kullanıcının sıralamadaki yerini getirir
    public ResponseEntity<Integer> getUserRank(@PathVariable Long userId) {
        int rank = evaluationService.getUserRank(userId);
        return ResponseEntity.ok(rank);
    }
}

