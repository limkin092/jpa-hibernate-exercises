package com.bobocode.dao;

import com.bobocode.exception.AccountDaoException;
import com.bobocode.model.Photo;
import com.bobocode.model.PhotoComment;
import com.bobocode.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Please note that you should not use auto-commit mode for your implementation.
 */
public class PhotoDaoImpl implements PhotoDao {
    private EntityManagerUtil emUtil;

    public PhotoDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.emUtil = new EntityManagerUtil(entityManagerFactory);
    }
    @Override
    public void save(Photo photo) {
        Objects.requireNonNull(photo);
        emUtil.performWithinTx(emf -> emf.persist(photo));
    }

    @Override
    public Photo findById(long id) {
        return emUtil.performReturningWithinTx(emUtil -> emUtil.find(Photo.class,id));
    }

    @Override
    public List<Photo> findAll() {
        return emUtil.performReturningWithinTx(
                emUtil -> emUtil.createQuery("SELECT p FROM Photo p").getResultList());
    }

    @Override
    public void remove(Photo photo) {
        Objects.requireNonNull(photo);
        emUtil.performWithinTx(entityManager -> {
            Photo managedPhoto = entityManager.merge(photo);
            entityManager.remove(managedPhoto);
        });
    }

    @Override
    public void addComment(long photoId, String comment) {
        emUtil.performWithinTx(emUtil -> {
            Photo photoReferance = emUtil.getReference(Photo.class,photoId);
            PhotoComment photoComment = new PhotoComment(comment,photoReferance);
            emUtil.persist(photoComment);
        });
    }

}
