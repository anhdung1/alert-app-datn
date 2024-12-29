package com.example.alert.repository.impl;

import com.example.alert.dtos.AlertResponse;
import com.example.alert.repository.CustomAlertRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CustomAlertRepositoryImpl implements CustomAlertRepository {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<AlertResponse> findAlertByTimeAndType(Long usersId, LocalDate startDate, LocalDate endDate, LocalTime startHour, LocalTime endHour, Pageable pageable) {
        String jpql="select a.createdAt, a.message, a.type " +
                "from Alert a " +
                "join a.device d " +
                "join d.userDevices ud "+
                "join ud.user u on u.usersId =:usersId " +
                "where FUNCTION('DATE', a.createdAt) between :startDate and :endDate";
        if(startHour!=null & endHour !=null){
            jpql+=" and FUNCTION('TIME',a.createdAt) between :startHour and :endHour";
        }
        jpql+=" order by a.createdAt DESC";
        Query query = entityManager.createQuery(jpql,AlertResponse.class);
        query.setParameter("usersId",usersId);
        query.setParameter("startDate",startDate);
        query.setParameter("endDate",endDate);
        if(startHour!=null & endHour !=null){
            query.setParameter("startHour",startHour);
            query.setParameter("endHour",endHour);}
        query.setFirstResult((pageable.getPageNumber() * pageable.getPageSize()));
        query.setMaxResults(pageable.getPageSize());
        return (List<AlertResponse>) query.getResultList();
    }
}
