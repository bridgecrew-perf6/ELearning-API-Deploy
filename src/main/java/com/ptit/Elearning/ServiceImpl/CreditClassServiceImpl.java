package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Department;
import com.ptit.Elearning.Repository.CreditClassRepository;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Service.CreditClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditClassServiceImpl implements CreditClassService {

    @Autowired
    CreditClassRepository creditClassRepository;

    @Override
    public Page<CreditClass> pageOfTopTenActive(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending() : Sort.by(sortField).descending() ;

        Pageable pageable = PageRequest.of(pageNo -1, pageSize,sort);
        Date today = new Date(System.currentTimeMillis());
        return creditClassRepository.findByStatusAndEndTimeGreaterThan(1,today,pageable);
    }

    @Override
    public CreditClass getCreditClassById(Long id) {
        return creditClassRepository.findByCreditClassId(id).orElseThrow(()->new NotFoundException("Credit class not found with id: "+id));
    }

    @Override
    public List<CreditClass> pageOfCreditClassViaSchoolYearAndSemesterAndDepartment(int pageNo, int pageSize, String sortField, String sortDirection, String schoolYear, int semester, Department department) throws IllegalAccessException {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable  = PageRequest.of(pageNo-1,pageSize,sort);
        Page<CreditClass> page = creditClassRepository.findBySchoolYearAndDepartment(schoolYear,department,pageable);

        List<CreditClass> list = page.getContent();

        if(semester==2){
            return list.stream().filter(c-> c.getStartTime().getMonth()>=0 && c.getEndTime().getMonth()<5).collect(Collectors.toList());
        }
        else if(semester==3){
            return list.stream().filter(c-> c.getStartTime().getMonth()>=5 && c.getEndTime().getMonth()<8).collect(Collectors.toList());
        }
        else if(semester==1){
            return list.stream().filter(c-> c.getStartTime().getMonth()>=8).collect(Collectors.toList());
        }
        else{
            throw  new IllegalAccessException("The semester is not valid. Must be from 1 to 3");
        }


    }

    @Override
    public List<CreditClass> getAll() {
        return creditClassRepository.findAll();
    }

    @Override
    public CreditClass createNewCreditClassOrUpdate(CreditClass creditClass) {
        return creditClassRepository.save(creditClass);
    }

    @Override
    public Page<CreditClass> pageOfCreditClassByBasicInfo(int pageNo, int pageSize, String sortField, String sortDirection, String schoolYear, int semester,int departmentId) throws IllegalAccessException {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable  = PageRequest.of(pageNo-1,pageSize,sort);

        int startMonth = 1;
        int endMonth = 13;
        if(semester==1){
            startMonth = 9;
            endMonth = 13;
        }else if(semester==2){
            startMonth = 1;
            endMonth = 6;
        }else if(semester==3){
            startMonth = 6;
            endMonth = 9;
        }else{
            throw new IllegalAccessException("Semester is not valid");
        }
        return creditClassRepository.findBySchoolYearAndDepartmentAndTime(schoolYear,departmentId,startMonth,endMonth,pageable);
    }
    @Override
    public Page<CreditClass> pageOfCreditClassByBasicInfoAndName(int pageNo, int pageSize, String sortField, String sortDirection, String schoolYear, int semester,int departmentId,String name) throws IllegalAccessException {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable  = PageRequest.of(pageNo-1,pageSize,sort);

        int startMonth = 1;
        int endMonth = 13;
        if(semester==1){
            startMonth = 9;
            endMonth = 13;
        }else if(semester==2){
            startMonth = 1;
            endMonth = 6;
        }else if(semester==3){
            startMonth = 6;
            endMonth = 9;
        }else{
            throw new IllegalAccessException("Semester is not valid");
        }
        return creditClassRepository.findBySchoolYearAndDepartmentAndTimeAndName(schoolYear,departmentId,startMonth,endMonth,name,pageable);
    }

    @Override
    public Page<CreditClass> pageOfCreditClassByName(int pageNo, int pageSize, String sortField, String sortDirection, String name)  {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable  = PageRequest.of(pageNo-1,pageSize,sort);


        return creditClassRepository.findByName(name,pageable);
    }
}
