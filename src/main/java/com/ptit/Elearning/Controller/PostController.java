package com.ptit.Elearning.Controller;

import com.ptit.Elearning.Constant.ERole;
import com.ptit.Elearning.DTO.PostCommentDTO;
import com.ptit.Elearning.DTO.PostCommentDTOpk.PostCommentRequest;
import com.ptit.Elearning.DTO.PostDTO.PostRequestDTO;
import com.ptit.Elearning.DTO.PostDTO.PostResponseDTO;
import com.ptit.Elearning.Entity.*;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Payloads.Request.Security.Jwt.JwtUtils;
import com.ptit.Elearning.Repository.RoleRepository;
import com.ptit.Elearning.Service.*;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {

    @Autowired
    PostService postService;

    @Autowired
    PostCommentService postCommentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    StudentService studentService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AccountService accountService;

    @Autowired
    CreditClassService creditClassService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ClassRegistrationService classRegistrationService;

    @ApiOperation(value = "Lấy tất cả các bình luận trong một bài post #32")
    @GetMapping("/all-comment")
    public ResponseEntity<?> allReplies(@RequestParam(name = "post-id") long postId) {
        Post post;
        try {
            post = postService.getPostById(postId);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToCommentDTO(postCommentService.getAllCommentsByPost(post)));
    }

    @ApiOperation(value = "Tạo bài post mới #39")
    @PostMapping("/create-new-post")
    public ResponseEntity<?> createPost(@RequestParam(name = "credit-class-id") long creditClassId, @RequestBody PostRequestDTO postDTO, HttpServletRequest request) {
        CreditClass creditClass;
        try {
            creditClass = creditClassService.getCreditClassById(creditClassId);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Post post = new Post();

        post.setPostContent(postDTO.getPostContent());
        post.setCreditClass(creditClass);
        post.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        post.setStatus(1);
        post.setUserInfo(account.getUserInfo());

        return ResponseEntity.ok(modelMapper.map(postService.createNewPostOrUpdate(post), PostResponseDTO.class));
    }

    @ApiOperation(value = "Cập nhật bài post #40")
    @PutMapping("/update-post")
    public ResponseEntity<?> postContent(@RequestParam(name = "post-id") long postId, @RequestBody PostRequestDTO postDTO, HttpServletRequest request) {
        Post post;
        try {
            post = postService.getPostById(postId);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        if (!post.getUserInfo().getUserId().equals(account.getUserInfo().getUserId())) {
            return ResponseEntity.notFound().build();
        }
        post.setPostContent(postDTO.getPostContent());
        return ResponseEntity.ok(modelMapper.map(postService.createNewPostOrUpdate(post), PostResponseDTO.class));
    }

    @ApiOperation(value = "Xóa bài post #41")
    @PutMapping("/delete-post")
    public ResponseEntity<?> deletePost(@RequestParam(name = "post-id") long postId, HttpServletRequest request) {
        Post post;
        try {
            post = postService.getPostById(postId);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        if (isCanDeletePermition(post, request)) {
            post.setStatus(0);
        } else {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(modelMapper.map(postService.createNewPostOrUpdate(post), PostResponseDTO.class));
    }
    @ApiOperation(value = "Bình luận bài viết #73")
    @PostMapping("/comment")
    public ResponseEntity<?> comment(@Valid  @RequestBody PostCommentRequest comment,  HttpServletRequest request) {

        try {
            String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
            Account account = accountService.getAccountByUsername(username);
            Post post = postService.getPostById(comment.getPostId());

            if(isCanComment(post.getCreditClass(),account)){
                //được quyền bình luận :
                UserInfo userInfo = account.getUserInfo();
                PostComment postComment = new PostComment();
                postComment.setPost(post);
                postComment.setContent(comment.getContent());
                postComment.setStatus(1);
                postComment.setUserInfo(userInfo);
                postComment.setCreatedAt(new Timestamp(System.currentTimeMillis()));

                return ResponseEntity.ok(convertToCommentDTO(Arrays.asList(postCommentService.addComment(postComment))).get(0));
            }else{
                return ResponseEntity.unprocessableEntity().body("You don't have permission");
            }

        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @ApiOperation(value = "Xóa bình luận của bài viết #61")
    @DeleteMapping("/delete-comment")
    public ResponseEntity<?> deleteComment(@RequestParam(name = "post-comment-id") long commentId, HttpServletRequest request) {
        PostComment postComment;
        CreditClass creditClass;
        try {
            postComment = postCommentService.getById(commentId);
            creditClass  = postComment.getPost().getCreditClass();

        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        //kiểm tra thử xem có phải admin hay không:
            //nếu là admin thì luôn delete được
            //nếu là giảng viên thì phải dạy lớp đó
            //nếu là sinh viên thì phải là người bình luận
        if (isCanDeleteCommentPermition(postComment,creditClass, request)) {
            boolean checkDelete = postCommentService.deleteComment(postComment);
            if(checkDelete)
                return ResponseEntity.ok("Deleted successfuly!");
            else
                return ResponseEntity.unprocessableEntity().body("Something went wrong!");
        } else {
            return ResponseEntity.unprocessableEntity().body("You do not have permition");
        }
    }

    private boolean isCanDeletePermition(Post post, HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Set<Role> roles = account.getRoles();
        Role roleTeacher = roleRepository.findByRoleName(ERole.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        if(roles.contains(roleMod)) return true;
        if (!roles.contains(roleTeacher) &&  !post.getUserInfo().getUserId().equals(account.getUserInfo().getUserId()))
            return false;

        return true;
    }

    private boolean isCanDeleteCommentPermition(PostComment postComment, CreditClass creditClass, HttpServletRequest request) {
        String username = jwtUtils.getUserNameFromJwtToken(request.getHeader("Authorization").substring(7));
        Account account = accountService.getAccountByUsername(username);
        Set<Role> roles = account.getRoles();
        Role roleTeacher = roleRepository.findByRoleName(ERole.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        //nếu là admin: được quyền xóa
        if(roles.contains(roleMod)) return true;

        //nếu là giảng viên: phải là giảng viên dạy lớp đó
        if(roles.contains(roleTeacher)){
            Teacher teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(teacher.getCreditClasses().contains(creditClass)) return true;
        }

        //nếu là sinh viên: thì phải là sinh viên đăng bình luận đó.
        if (postComment.getUserInfo().getUserId().equals(account.getUserInfo().getUserId()))
            return true;

        return false;
    }
    private boolean isCanComment(CreditClass creditClass, Account account) {
        Set<Role> roles = account.getRoles();
        Role roleTeacher = roleRepository.findByRoleName(ERole.ROLE_TEACHER).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        Role roleMod = roleRepository.findByRoleName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        //nếu là admin: được quyền bình luận
        if(roles.contains(roleMod)) return true;

        //nếu là giảng viên: phải là giảng viên dạy lớp đó
        if(roles.contains(roleTeacher)){
            Teacher teacher = teacherService.getByUserInfo(account.getUserInfo());
            if(teacher.getCreditClasses().contains(creditClass)) return true;
        }

        //nếu là sinh viên: thì phải học lớp này !
        if (classRegistrationService.checkStudentJoinedClass(account.getUserInfo().getUserId(),creditClass.getCreditClassId()))
            return true;

        return false;
    }
    private List<PostCommentDTO> convertToCommentDTO(List<PostComment> postComments) {
        List<PostCommentDTO> dtos = new ArrayList<>();

        postComments.forEach(p -> {
            PostCommentDTO dto = modelMapper.map(p, PostCommentDTO.class);
            if(p.getUserInfo().getAvatar()!=null){
                dto.setAvatar(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/avatar/dowload/")
                        .path(p.getUserInfo().getAvatar().getAvatarId().toString())
                        .toUriString());
            }

            dto.setUserId(p.getUserInfo().getUserId());
            dto.setUserName(p.getUserInfo().getFullname());

            try{
                Teacher teacher = teacherService.getByUserInfo(p.getUserInfo());
                dto.setTeacher(true);
                dto.setCode(teacher.getTeacherId());
            }catch (NotFoundException e){
                Student student = studentService.getByUserInfo(p.getUserInfo());
                dto.setTeacher(false);
                dto.setCode(student.getStudentCode());
            }

            dtos.add(dto);
        });
        return dtos;
    }
}
