package org.project.api;

import lombok.AllArgsConstructor;
import org.project.model.Review;
import org.project.service.ReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping()
    public void addReview(@RequestBody Review review) {
        reviewService.addReview(review);
    }
}
