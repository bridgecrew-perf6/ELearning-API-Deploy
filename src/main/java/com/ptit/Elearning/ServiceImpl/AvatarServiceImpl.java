package com.ptit.Elearning.ServiceImpl;

import com.google.common.io.Files;
import com.ptit.Elearning.Entity.Avatar;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.AvatarRepository;
import com.ptit.Elearning.Service.AvatarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class AvatarServiceImpl implements AvatarService {
    @Autowired
    AvatarRepository avatarRepository;

    @Override
    public Avatar saveAvatar(MultipartFile file) throws Exception {
        Avatar avatar = null;
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {//any invalid character: throw exception!
                throw new Exception("File name contains invalid path sequence " + fileName);
            }
            if (isSupportedContentType(getFileExtension(fileName))==false) {
                throw new IllegalStateException("File must be an Image with type .jpg, .jpeg, .png");
            }
            avatar = new Avatar(fileName, file.getContentType(), file.getBytes());
            return avatarRepository.save(avatar);
        } catch (Exception e) {
            System.out.println("message"+e.getMessage());
            throw new Exception("Could not save file");
        }
    }

    @Override
    public Avatar getAvatar(Long avatarId) {
        Avatar avatar = avatarRepository.findByAvatarId(avatarId)
                .orElseThrow(() -> new NotFoundException("Could not found with id: " + avatarId));

        return avatar;
    }

    private boolean isSupportedContentType(String contentType) {
        return contentType.equals("png")
                || contentType.equals("jpg")
                || contentType.equals("jpeg");
    }
    private static String getFileExtension(String fullName) {

        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
