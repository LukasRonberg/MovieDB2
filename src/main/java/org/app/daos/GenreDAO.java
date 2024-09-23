package org.app.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.app.config.HibernateConfig;
import org.app.entities.Genre;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenreDAO implements org.app.daos.IDAO<Genre> {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("sp1movie");
    @Override
    public Genre getBytId(Integer id) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Genre.class, id);
        }
    }

    @Override
    public Set<Genre> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Genre> query = em.createQuery("SELECT u FROM Genre u", Genre.class);
            List<Genre> userList = query.getResultList();
            return userList.stream().collect(Collectors.toSet());
        }
    }


    public ArrayList<Genre> getAllAsList() {
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Genre> query = em.createQuery("SELECT u FROM Genre u", Genre.class);
            List<Genre> userList = query.getResultList();
            return userList.stream().collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public void create(Genre genre) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(genre);
            em.getTransaction().commit();
        }
    }

    @Override
    public void update(Genre genre) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(genre);
            em.getTransaction().commit();
        }
    }


    @Override
    public void delete(Genre genre) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(genre);
            em.getTransaction().commit();
        }
    }
}
