// src/main/java/com/example/urlshortener/repository/UrlMappingRepository.java

package com.example.urlshortener.repository;

import com.example.urlshortener.model.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

// NEW: Import the LocalDateTime class for our method parameter.
import java.time.LocalDateTime;
import java.util.Optional;

public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {

    Optional<UrlMapping> findByShortCode(String shortCode);

    // --- NEWLY ADDED METHOD START ---

    /**
     * Defines a derived delete query for bulk deletion of expired URLs.
     *
     * Spring Data JPA will parse this method name and generate the corresponding
     * JPQL/SQL 'DELETE' statement: "DELETE FROM UrlMapping u WHERE u.expirationDate < :now"
     *
     * This is a highly efficient way to perform a bulk delete, as it executes a single
     * command in the database without fetching the entities into the application's memory first.
     *
     * @param now The timestamp to compare against. All URLs with an expirationDate
     *            before this time will be deleted.
     * @return The number of entities that were deleted. This is very useful for logging.
     */
    long deleteByExpirationDateBefore(LocalDateTime now);

    // --- NEWLY ADDED METHOD END ---
}