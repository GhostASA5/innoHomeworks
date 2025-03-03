package org.project.service;

import lombok.AllArgsConstructor;
import org.project.model.Review;
import org.project.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReviewService {

    private ReviewRepository reviewRepository;

    public void addReview(Review reviewDto) {
        Review review = new Review();
        review.setStudentId(reviewDto.getStudentId());
        review.setCourseId(reviewDto.getCourseId());
        review.setMessage(reviewDto.getMessage());
        reviewRepository.save(review);
    }
}
