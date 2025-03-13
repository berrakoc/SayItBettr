package com.sib.sayitbettr.Controller;

import com.sib.sayitbettr.Model.Evaluation;
import com.sib.sayitbettr.Model.EvaluationRequest;
import com.sib.sayitbettr.Service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByUserId(userId));
    }

    @GetMapping("/word/{wordId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByWord(@PathVariable Long wordId) {
        return ResponseEntity.ok(evaluationService.getEvaluationsByWordId(wordId));
    }
}
