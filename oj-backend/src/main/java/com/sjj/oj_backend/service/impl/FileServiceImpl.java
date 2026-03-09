package com.sjj.oj_backend.service.impl;

import cn.hutool.core.io.FileUtil;
import com.sjj.oj_backend.common.ErrorCode;
import com.sjj.oj_backend.exception.BusinessException;
import com.sjj.oj_backend.service.FileService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * 本地文件服务实现
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private static final long MAX_AVATAR_SIZE = 2 * 1024 * 1024L;

    private static final List<String> ALLOWED_AVATAR_SUFFIX = Arrays.asList("jpg", "jpeg", "png", "webp");

    private static final Path AVATAR_UPLOAD_DIR = Paths.get("uploads", "avatar");

    @Override
    public String uploadAvatar(MultipartFile multipartFile) {
        validateAvatarFile(multipartFile);
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename()).toLowerCase();
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;

        try {
            Files.createDirectories(AVATAR_UPLOAD_DIR);
            Path targetPath = AVATAR_UPLOAD_DIR.resolve(fileName).normalize();
            Files.copy(multipartFile.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/file/avatar/")
                    .path(fileName)
                    .toUriString();
        } catch (IOException e) {
            log.error("upload avatar failed, fileName={}", fileName, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "头像上传失败");
        }
    }

    @Override
    public Resource loadAvatarAsResource(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "头像文件名不能为空");
        }
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "非法头像文件名");
        }

        try {
            Path filePath = AVATAR_UPLOAD_DIR.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "头像不存在");
            }
            return resource;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("load avatar failed, fileName={}", fileName, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "读取头像失败");
        }
    }

    private void validateAvatarFile(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }
        if (multipartFile.getSize() > MAX_AVATAR_SIZE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 2MB");
        }
        String suffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (StringUtils.isBlank(suffix) || !ALLOWED_AVATAR_SUFFIX.contains(suffix.toLowerCase())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "仅支持 jpg、jpeg、png、webp 图片格式");
        }
    }
}