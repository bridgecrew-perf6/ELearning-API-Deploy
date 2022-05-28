package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Avatar;
import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {
    public Avatar saveAvatar(MultipartFile file) throws Exception;
    public Avatar getAvatar(Long avatarId);
}
