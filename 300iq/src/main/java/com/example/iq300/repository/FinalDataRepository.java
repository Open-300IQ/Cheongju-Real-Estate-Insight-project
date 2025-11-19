package com.example.iq300.repository;

import com.example.iq300.domain.FinalData;
import com.example.iq300.domain.IFinalDataAgg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface FinalDataRepository extends JpaRepository<FinalData, Long> {

    @Query("SELECT f FROM FinalData f WHERE f.areaName = :areaName AND f.contractType = :contractType AND f.buildingType = :buildingType AND f.contractMonth IN :months ORDER BY f.contractMonth ASC")
    List<FinalData> findByAreaNameAndContractTypeAndBuildingTypeAndContractMonthIn(
            @Param("areaName") String areaName,
            @Param("contractType") String contractType,
            @Param("buildingType") String buildingType,
            @Param("months") List<String> months
    );

    @Query("SELECT " +
            "    f.contractMonth AS contractMonth, " +
            "    (CASE WHEN SUM(f.count) = 0 THEN 0 ELSE SUM(f.price * f.count) / SUM(f.count) END) AS weightedAvgPrice, " +
            "    SUM(f.count) AS totalCount " +
            "FROM FinalData f " +
            "WHERE f.areaName = :areaName AND f.contractType = :contractType AND f.contractMonth IN :months " +
            "GROUP BY f.contractMonth " +
            "ORDER BY f.contractMonth ASC")
    List<IFinalDataAgg> findAggregatedBy(
            @Param("areaName") String areaName,
            @Param("contractType") String contractType,
            @Param("months") List<String> months
    );
    
    @Query("SELECT " +
            "    f.contractMonth AS contractMonth, " +
            "    (CASE WHEN SUM(f.count) = 0 THEN 0 ELSE SUM(f.price * f.count) / SUM(f.count) END) AS weightedAvgPrice, " +
            "    SUM(f.count) AS totalCount " +
            "FROM FinalData f " +
            "WHERE f.contractType = :contractType AND f.contractMonth IN :months " +
            "GROUP BY f.contractMonth " +
            "ORDER BY f.contractMonth ASC")
    List<IFinalDataAgg> findAggregatedTotalCity(
            @Param("contractType") String contractType,
            @Param("months") List<String> months
    );

    @Query("SELECT " +
            "    (CASE WHEN SUM(f.count) = 0 THEN 0 ELSE SUM(f.price * f.count) / SUM(f.count) END) AS weightedAvgPrice, " +
            "    SUM(f.count) AS totalCount " +
            "FROM FinalData f " +
            "WHERE f.areaName = :areaName AND f.contractType = :contractType AND f.contractMonth = :month")
    IFinalDataAgg findStatByAreaAndMonth(
            @Param("areaName") String areaName,
            @Param("contractType") String contractType,
            @Param("month") String month
    );
    
    @Query("SELECT " +
            "    (CASE WHEN SUM(f.count) = 0 THEN 0 ELSE SUM(f.price * f.count) / SUM(f.count) END) AS weightedAvgPrice, " +
            "    SUM(f.count) AS totalCount " +
            "FROM FinalData f " +
            "WHERE f.contractType = :contractType AND f.contractMonth = :month")
    IFinalDataAgg findStatTotalCityByMonth(
            @Param("contractType") String contractType,
            @Param("month") String month
    );
    
    @Query("SELECT SUM(f.count) FROM FinalData f WHERE f.areaName = :areaName AND f.contractType = :contractType AND f.contractMonth IN :months")
    Long sumCountByArea(
            @Param("areaName") String areaName,
            @Param("contractType") String contractType,
            @Param("months") List<String> months
    );

    @Query("SELECT SUM(f.count) FROM FinalData f WHERE f.contractType = :contractType AND f.contractMonth IN :months")
    Long sumCountTotalCity(
            @Param("contractType") String contractType,
            @Param("months") List<String> months
    );
}