package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.MentorReview;
import goinmul.sportsmanage.domain.dto.MentorReviewDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorReviewRepository {

    private final EntityManager em;

    @Transactional
    public void save(MentorReview mentorMentee) {
        em.persist(mentorMentee);
    }

    public MentorReview findOne(Long id) {
        return em.find(MentorReview.class, id);
    }

    public List<MentorReview> findAll() {
        return em.createQuery("select m from MentorReview m", MentorReview.class).getResultList();
    }
    public List<MentorReviewDto> findAllGroupByMentorId() {
        return em.createQuery("select new goinmul.sportsmanage.domain.dto.MentorReviewDto(m, avg(m.score), count(m.id)) from MentorReview m group by m.mentor.id",
                        MentorReviewDto.class)
                .getResultList();
    }
    public List<MentorReview> findByMentorId(Long mentorId) {
        return em.createQuery("select m from MentorReview m where m.mentor.id = :mentorId", MentorReview.class)
                .setParameter("mentorId", mentorId)
                .getResultList();
    }


}
