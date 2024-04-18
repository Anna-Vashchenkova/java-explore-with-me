package ru.practicum.ewm_main_service.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm_main_service.compilation.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("select c from Compilation as c where c.pinned = :pinned order by c.id")
    Page<Compilation> findAllByPinned(Boolean pinned, PageRequest of);
}

