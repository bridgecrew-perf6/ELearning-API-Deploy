package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Department;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CreditClassService {
    public Page<CreditClass>  pageOfTopTenActive(int pageNo, int pageSize, String sortField, String sortDirection);
    public CreditClass getCreditClassById(Long id);
    public List<CreditClass> pageOfCreditClassViaSchoolYearAndSemesterAndDepartment(int pageNo, int pageSize, String sortField, String sortDirection, String schoolYear, int semester, Department department) throws IllegalAccessException;
    public List<CreditClass> getAll();
    public CreditClass createNewCreditClassOrUpdate(CreditClass creditClass);
    public Page<CreditClass> pageOfCreditClassByBasicInfo(int pageNo, int pageSize, String sortField, String sortDirection, String schoolYear, int semester, int departmentId) throws IllegalAccessException;
    public Page<CreditClass> pageOfCreditClassByBasicInfoAndName(int pageNo, int pageSize, String sortField, String sortDirection, String schoolYear, int semester, int departmentId,String name) throws IllegalAccessException;
    public Page<CreditClass> pageOfCreditClassByName(int pageNo, int pageSize, String sortField, String sortDirection, String name);

}
