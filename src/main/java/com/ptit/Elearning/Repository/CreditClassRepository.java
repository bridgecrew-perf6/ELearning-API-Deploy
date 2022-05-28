package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface CreditClassRepository extends JpaRepository<CreditClass,Long> {
    public Page<CreditClass> findByStatusAndEndTimeGreaterThan(int status,Date today, Pageable pageable);
    public Optional<CreditClass> findByCreditClassId(Long id);
    public Page<CreditClass> findBySchoolYearAndDepartment(String schoolYear, Department department,Pageable pageable);

    @Query(value = "select * from credit_class where school_year= :schoolYear and extract( month from end_time) < :endMonth and extract(month from start_time)>= :startMonth " +
            "and department_id = :departmentId ", countQuery = "select * from credit_class where school_year= :schoolYear and extract( month from end_time) < :endMonth and extract(month from start_time)>= :startMonth " +
            " and department_id = :departmentId", nativeQuery = true)
    public Page<CreditClass> findBySchoolYearAndDepartmentAndTime(@Param("schoolYear") String schoolYear,@Param("departmentId") int departmentId,
                                                                  @Param("startMonth") int startMonth, @Param("endMonth") int endMonth, Pageable pageable);

    @Query(value = "select credit_class.* from credit_class,subject where school_year= :schoolYear and extract( month from end_time) < :endMonth and extract(month from start_time)>= :startMonth " +
            "and department_id = :departmentId and credit_class.subject_id = subject.subject_id and LOWER(subject_name) like LOWER(CONCAT('%',:name,'%'))", countQuery = "select credit_class.* from credit_class,subject where school_year= :schoolYear and extract( month from end_time) < :endMonth and extract(month from start_time)>= :startMonth " +
            " and department_id = :departmentId and credit_class.subject_id = subject.subject_id and LOWER(subject_name) like LOWER(CONCAT('%',:name,'%'))", nativeQuery = true)
    public Page<CreditClass> findBySchoolYearAndDepartmentAndTimeAndName(@Param("schoolYear") String schoolYear,@Param("departmentId") int departmentId,
                                                                  @Param("startMonth") int startMonth, @Param("endMonth") int endMonth, @Param("name") String name, Pageable pageable);

    @Query(value = "select credit_class.* from credit_class,subject " +
            "where credit_class.subject_id = subject.subject_id and LOWER(subject_name) like LOWER(CONCAT('%',:name,'%'))", countQuery = "select credit_class.* from credit_class,subject " +
            " where credit_class.subject_id = subject.subject_id and LOWER(subject_name) like LOWER(CONCAT('%',:name,'%'))", nativeQuery = true)
    public Page<CreditClass> findByName( @Param("name") String name, Pageable pageable);
}
