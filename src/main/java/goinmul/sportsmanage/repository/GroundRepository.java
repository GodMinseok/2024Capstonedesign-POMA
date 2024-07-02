package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Ground;
import goinmul.sportsmanage.domain.form.GroundForm;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GroundRepository {

    private final EntityManager em;

    public void save(Ground ground) {
        em.persist(ground);
    }

    public void update(Long groundId, String location, String name, Integer price) {
        em.createQuery("update Ground g set g.name = :name, g.location = :location, g.price =:price" +
                        " where g.id = :id")
                .setParameter("name", name)
                .setParameter("location", location)
                .setParameter("price", price)
                .setParameter("id", groundId)
                .executeUpdate();
    }

    public Ground findOne(Long id) {
        return em.find(Ground.class, id);
    }

    public List<Ground> findAll() {
        return em.createQuery("select g from Ground g", Ground.class).getResultList();
    }

    public Ground validateGround(String location, String name){
        List<Ground> grounds = em.createQuery("select g from Ground g " +
                        "where g.location = :location and g.name = :name", Ground.class)
                .setParameter("location", location)
                .setParameter("name", name)
                .getResultList();

        return grounds.isEmpty() ?  null : grounds.get(0);
    }


}
