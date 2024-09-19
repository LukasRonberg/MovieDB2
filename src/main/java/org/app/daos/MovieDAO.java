package org.app.daos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.app.config.HibernateConfig;
import org.app.entities.Movie;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieDAO implements org.app.daos.IDAO<Movie> {
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

    public List<Movie> getByTitle(String title) {
        try (EntityManager em = emf.createEntityManager()) {
            // Convert both the column and the parameter to lowercase and use LIKE for partial matching
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT u FROM Movie u WHERE LOWER(u.title) LIKE LOWER(CONCAT('%', :title, '%'))",
                    Movie.class);
            query.setParameter("title", title);
            return query.getResultList();
        }
    }

    public List<Movie> getByReleaseYear(String year) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT u FROM Movie u WHERE u.releaseDate = :year",
                    Movie.class);
            query.setParameter("year", year);
            return query.getResultList();
        }
    }

    public List<Movie> getByRating(double rating) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT u FROM Movie u WHERE u.voteAverage >= :rating",
                    Movie.class);
            query.setParameter("rating", rating);
            return query.getResultList();
        }
    }

    public List<Movie> getTopTenMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT u FROM Movie u ORDER BY u.voteAverage DESC",
                    Movie.class);
            query.setMaxResults(10); // Limit to top 10 results
            return query.getResultList();
        }
    }

    public List<Movie> getTopTenMostPopularMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT u FROM Movie u ORDER BY u.popularity DESC",
                    Movie.class);
            query.setMaxResults(10); // Limit to top 10 results
            return query.getResultList();
        }
    }
    public List<Movie> getLowestRatedTenMovies() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Movie> query = em.createQuery(
                    "SELECT u FROM Movie u ORDER BY u.voteAverage ASC",
                    Movie.class);
            query.setMaxResults(10); // Limit to lowest 10 results
            return query.getResultList();
        }
    }
    public double getAverageForAllMoviesInDB() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(u.voteAverage) FROM Movie u",
                    Double.class);
            return query.getSingleResult(); // Returns the average rating
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
