package goinmul.sportsmanage.repository;


import goinmul.sportsmanage.domain.Authority;
import goinmul.sportsmanage.domain.CustomerSupport;
import goinmul.sportsmanage.domain.Gender;
import goinmul.sportsmanage.domain.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerSupportRepository {

    private final EntityManager em;

    public void save(CustomerSupport customerSupport) {
        em.persist(customerSupport);
    }

    public CustomerSupport findOne(Long id) {
        return em.find(CustomerSupport.class, id);
    }

    public List<CustomerSupport> findAll() {
        return em.createQuery("select c from CustomerSupport c", CustomerSupport.class).getResultList();
    }

}
