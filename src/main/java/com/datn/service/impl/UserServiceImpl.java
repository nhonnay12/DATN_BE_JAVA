package com.datn.service.impl;

import com.datn.constant.ConstantRole;
import com.datn.dto.request.user_role.UserCreationRequest;
import com.datn.dto.request.user_role.UserUpdateRequest;
import com.datn.dto.response.UserPagingResponse;
import com.datn.dto.response.UserResponse;
import com.datn.entity.ImageData;
import com.datn.entity.Role;
import com.datn.entity.User;
import com.datn.exception.AppException;
import com.datn.exception.ErrorCode;
import com.datn.mapper.UserMapper;
import com.datn.repository.ImageRepo;
import com.datn.repository.RoleRepository;
import com.datn.repository.UserRepository;
import com.datn.service.ImageService;
import com.datn.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;
    private final ImageRepo imageRepo;

    @Override
    public UserResponse createUser(MultipartFile file, UserCreationRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        User user = userMapper.toUser(request);
        //  user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById(request.getRoles()).ifPresent(roles::add);
            user.setRoles(roles);
        } else {
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById(ConstantRole.USER_ROLE).ifPresent(roles::add);
            user.setRoles(roles);
        }

        // Xử lý ảnh nếu có
        if (file != null && !file.isEmpty()) {
            ImageData imageData = imageService.saveImage(file);
            imageData.setUser(user);
            user.setImages(Collections.singletonList(imageData));
        }
        // user.setEnabled(request.isEnabled());
        log.info("User ID: {}", user.getId());
        User savedUser = userRepository.save(user);
        log.info("Saved user ID: {}", savedUser.getId());
        return userMapper.toUserResponse(savedUser);
    }


//    @Override
//    public UserResponse getMyInfo(String id) {
//        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        return userMapper.toUserResponse(user);
//    }

    public UserResponse updateUser(MultipartFile file, UserUpdateRequest request) {
        // Tìm kiếm người dùng trong cơ sở dữ liệu
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        log.info(user.toString());
        // Cập nhật các trường dữ liệu người dùng chỉ nếu có thay đổi

        if (request.getStatus() != null && !request.getStatus().equals(user.getStatus())) {
            user.setStatus(request.getStatus());

        }
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())) {
            user.setPhone(request.getPhone());

        }if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            user.setEmail(request.getEmail());

        }
        // Cập nhật roles chỉ khi roles có thay đổi
        if (request.getRoles() != null && !request.getRoles().equals(user.getRoles())) {
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById(request.getRoles()).ifPresent(roles::add);
            user.setRoles(roles);
        }

        // Kiểm tra và xử lý hình ảnh mới
        if (file != null && !file.isEmpty()) {
            // Nếu có ảnh mới, ẩn tất cả ảnh cũ
            for (ImageData img : user.getImages()) {
                img.setHidden(true);
                imageRepo.save(img);  // Lưu lại trạng thái ẩn cho ảnh cũ
            }

            // Lưu ảnh mới
            ImageData newImage = imageService.saveImage(file);
            newImage.setUser(user);
            newImage.setHidden(false);  // Đảm bảo ảnh mới không bị ẩn
            List<ImageData> images = new ArrayList<>(user.getImages());
            images.add(newImage);  // Thêm ảnh mới vào danh sách ảnh của người dùng
            user.setImages(images);
        }

        // Lưu thông tin người dùng đã được cập nhật vào cơ sở dữ liệu
        user = userRepository.save(user);

        // Lọc lại danh sách ảnh chỉ gồm ảnh không bị ẩn
        List<ImageData> visibleImages = new ArrayList<>();
        for (ImageData img : user.getImages()) {
            if (!img.isHidden()) {
                visibleImages.add(img);
            }
        }

        // Cập nhật lại danh sách ảnh chỉ với ảnh không bị ẩn
        user.setImages(visibleImages);

        // Trả về response với thông tin người dùng đã cập nhật
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        user.setStatus("INACTIVE");
        userRepository.save(user);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserPagingResponse getAllUserWithPagingAndSort(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort); // vi pgaeNumber bat dau tu 1 nen khi lay page se lay pageNumber - 1 vì khi lấy nó lay từ 0
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> userList = userPage.getContent();

        List<UserResponse> userResponsesList = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse = userMapper.toUserResponse(user);
            userResponsesList.add(userResponse);
        }

        return new UserPagingResponse(userResponsesList,
                pageNumber, pageSize, userPage.getTotalElements(),
                userPage.getTotalPages(), userPage.isLast());
    }

    @Override
    public UserPagingResponse getAllUserwithPaging(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);// vi pgaeNumber bat dau tu 1 nen khi lay page se lay pageNumber - 1 vì khi lấy nó lay từ 0
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> userList = userPage.getContent();

        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList) {
            UserResponse userResponse = userMapper.toUserResponse(user);
            userResponseList.add(userResponse);
        }

        return new UserPagingResponse(userResponseList,
                pageNumber, pageSize, userPage.getTotalElements(),
                userPage.getTotalPages(), userPage.isLast());
    }
}
