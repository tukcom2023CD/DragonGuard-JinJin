package com.dragonguard.backend.global.repository;

import com.dragonguard.backend.global.audit.Auditable;
import com.dragonguard.backend.global.exception.EntityNotFoundException;
import com.dragonguard.backend.global.exception.UnsupportedDeletionException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승진
 * @description soft delete 를 위한 repository 인터페이스
 */

public interface EntityRepository<T extends Auditable, ID> extends JpaRepository<T, ID> {

    @Modifying
    @Transactional
    default void softDelete(final T entity) {
        entity.delete();
    }

    @Modifying
    @Transactional
    default void softDeleteById(final ID id) {
        final T entity = findById(id).orElseThrow(EntityNotFoundException::new);
        entity.delete();
    }

    @Override
    default void deleteById(final ID id) {
        throw new UnsupportedDeletionException();
    }

    @Override
    default void delete(final T entity) {
        throw new UnsupportedDeletionException();
    }

    @Override
    default void deleteAll(final Iterable<? extends T> entities) {
        throw new UnsupportedDeletionException();
    }

    @Override
    default void deleteAll() {
        throw new UnsupportedDeletionException();
    }
}
