package org.app.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.app.config.HibernateConfig;
import org.app.entities.Movie;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieDAO implements org.app.DAOS.IDAO<Movie> {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("sp1movie");
    @Override
    public Movie getBytId(Integer id) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Movie.class, id);
        }
    }

    @Override
    public Set<Movie> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery("SELECT u FROM Movie u", Movie.class);
            List<Movie> userList = query.getResultList();
            return userList.stream().collect(Collectors.toSet());
        }
    }


    public List<Movie> getAllWithGenres() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.genres", Movie.class);
            return query.getResultList();
        }
    }


    @Override
    public void create(Movie Movie) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(Movie);
            em.getTransaction().commit();
        }
    }

    @Override
    public void update(Movie Movie) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(Movie);
            em.getTransaction().commit();
        }
    }


    @Override
    public void delete(Movie Movie) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(Movie);
            em.getTransaction().commit();
        }
    }

}
