package org.app.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.app.config.HibernateConfig;
import org.app.entities.Crew;
import org.app.entities.Movie;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrewDAO implements org.app.daos.IDAO<Crew> {
    private EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("sp1movie");
    @Override
    public Crew getBytId(Integer id) {
        try(EntityManager em = emf.createEntityManager()) {
            return em.find(Crew.class, id);
        }
    }

    @Override
    public Set<Crew> getAll() {
        try(EntityManager em = emf.createEntityManager()) {
            TypedQuery<Crew> query = em.createQuery("SELECT u FROM Crew u", Crew.class);
            List<Crew> userList = query.getResultList();
            return userList.stream().collect(Collectors.toSet());
        }
    }

    public Set<Crew> getActorsOrDirectors(Movie movie, boolean showDirectors) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Crew> query;
            if (showDirectors) {
                query = em.createQuery("SELECT c FROM Crew c WHERE c.movie = :movie AND c.job = 'Director'", Crew.class);
            } else {
                query = em.createQuery("SELECT c FROM Crew c WHERE c.movie = :movie AND c.job IS NULL", Crew.class);
            }

            query.setParameter("movie", movie);
            List<Crew> crewList = query.getResultList();
            return crewList.stream().collect(Collectors.toSet());
        }
    }


    public Set<Crew> getCrewByMovieById(Long movieId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Crew> query = em.createQuery(
                    "SELECT c FROM Crew c WHERE c.movie.id = :movieId", Crew.class);
            query.setParameter("movieId", movieId);
            List<Crew> crewList = query.getResultList();
            return crewList.stream().collect(Collectors.toSet());
        }
    }


    @Override
    public void create(Crew crew) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(crew);
            em.getTransaction().commit();
        }
    }

    @Override
    public void update(Crew crew) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(crew);
            em.getTransaction().commit();
        }
    }


    @Override
    public void delete(Crew crew) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(crew);
            em.getTransaction().commit();
        }
    }
}
