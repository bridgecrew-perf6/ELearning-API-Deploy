package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.AutogeneateNotification.NotificationForJoinClass;
import com.ptit.Elearning.DTO.ListStudentIDDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.ClassRegistrationRepository;
import com.ptit.Elearning.Service.ClassRegistrationService;
import com.ptit.Elearning.Service.NotificationService;
import com.ptit.Elearning.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassRegistrationServiceImpl implements ClassRegistrationService {
    @Autowired
    ClassRegistrationRepository classRegistrationRepository;

    @Autowired
    StudentService studentService;

    @Autowired
    NotificationService notificationService;


    @Override
    public List<ClassRegistration> getListClassByUserInfo(UserInfo userInfo) {
        return classRegistrationRepository.findByUserInfo(userInfo);
    }

    @Override
    public List<UserInfo> getListUserInfosByCreditClass(CreditClass creditClass) {
         return classRegistrationRepository.findByCreditClass(creditClass).stream().map(r->r.getUserInfo()).collect(Collectors.toList());
    }

    @Override
    public List<UserInfo> getListUserInfosByCreditClassActive(CreditClass creditClass) {
        return classRegistrationRepository.findByCreditClassAndStatus(creditClass,1).stream().map(r->r.getUserInfo()).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class})
    public boolean addStudentToCreditClass(CreditClass creditClass, ListStudentIDDTO list, Teacher teacher) {
        for (String s : list.getStudentCode()) {
            Student student = studentService.getByStudentCode(s);
            ClassRegistration classRegistration = new ClassRegistration();

            classRegistration.setCreditClass(creditClass);
            classRegistration.setUserInfo(student.getUserInfo());
            classRegistration.setClassRegistrationId(new ClassRegistrationId(student.getUserInfo().getUserId(),creditClass.getCreditClassId()));
            classRegistration.setStatus(1);
            classRegistration.setJoinedTime( new Timestamp(System.currentTimeMillis()));
            classRegistrationRepository.save(classRegistration);


            //create notification for users
            String notificationContent = NotificationForJoinClass.addToClass(creditClass.getSubject().getSubjectName(),classRegistration.getJoinedTime(),teacher.getUserInfo().getFullname());
            Notification notification = new Notification();
            notification.setNotificationContent(notificationContent);
            notification.setStatus(false);
            notification.setOwner(student.getUserInfo());
            notification.setTime(new Timestamp(System.currentTimeMillis()));
            notificationService.createNewNotification(notification);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {NotFoundException.class})
    public boolean removeStudentFromCreditClass(CreditClass creditClass, ListStudentIDDTO list,Teacher teacher) {
        for (String s : list.getStudentCode()) {
            Student student = studentService.getByStudentCode(s);
            ClassRegistration classRegistration = classRegistrationRepository.findByClassRegistrationIdUserIdAndClassRegistrationIdCreditClassId(student.getUserInfo().getUserId(),creditClass.getCreditClassId())
                    .orElseThrow(()->new NotFoundException("Could not found class registration with student and class"));
            classRegistration.setStatus(3);
            classRegistrationRepository.save(classRegistration);

            //create notification for user
            String notificationContent = NotificationForJoinClass.removeFromClass(creditClass.getSubject().getSubjectName(),classRegistration.getJoinedTime(),teacher.getUserInfo().getFullname());
            Notification notification = new Notification();
            notification.setNotificationContent(notificationContent);
            notification.setStatus(false);
            notification.setOwner(student.getUserInfo());
            notification.setTime(new Timestamp(System.currentTimeMillis()));
            notificationService.createNewNotification(notification);
        }
        return true;
    }

    @Override
    public boolean checkStudentJoinedClass(Long userId, Long creditClassId) {
        try{
            ClassRegistration classRegistration = classRegistrationRepository.findByClassRegistrationIdUserIdAndClassRegistrationIdCreditClassId(userId,creditClassId)
                    .orElseThrow(()->new NotFoundException("Student has not joined this class before"));
        }catch(NotFoundException e){
            return false;
        }
        return true;
    }

    @Override
    public boolean joinToCreditClass(CreditClass creditClass, UserInfo userInfo, String joinCode) {
        BCryptPasswordEncoder passwordEncoder  = new BCryptPasswordEncoder();
        if(!passwordEncoder.matches(joinCode,creditClass.getJoinedPassword().trim())){
            return false;
        }

        ClassRegistration classRegistration = new ClassRegistration();

        classRegistration.setCreditClass(creditClass);
        classRegistration.setUserInfo(userInfo);
        classRegistration.setClassRegistrationId(new ClassRegistrationId(userInfo.getUserId(),creditClass.getCreditClassId()));
        classRegistration.setStatus(1);
        classRegistration.setJoinedTime( new Timestamp(System.currentTimeMillis()));
        classRegistrationRepository.save(classRegistration);

        //create notification for user:
        String notificationContent = NotificationForJoinClass.joinClass(creditClass.getSubject().getSubjectName(),classRegistration.getJoinedTime());
        Notification notification = new Notification();
        notification.setNotificationContent(notificationContent);
        notification.setStatus(false);
        notification.setOwner(userInfo);
        notification.setTime(new Timestamp(System.currentTimeMillis()));
        notificationService.createNewNotification(notification);

        return true;
    }
}
