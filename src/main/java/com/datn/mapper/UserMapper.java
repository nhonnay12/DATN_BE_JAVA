package com.datn.mapper;

import com.datn.dto.request.user_role.UserUpdateRequest;
import com.datn.dto.response.UserResponse;
import com.datn.entity.ImageData;
import com.datn.entity.Role;
import com.datn.entity.User;
import com.datn.dto.request.user_role.UserCreationRequest;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Component
public class UserMapper {


    public User toUser(UserCreationRequest request) {
        if ( request == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( request.getUsername() );
        user.password( request.getPassword() );
        user.firstName( request.getFirstName() );
        user.lastName( request.getLastName() );
        user.email( request.getEmail() );
        user.phone( request.getPhone() );
        user.status( request.getStatus() );

        return user.build();
    }


    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        List<ImageData> list = user.getImages();
        if ( list != null ) {
            userResponse.images( new ArrayList<ImageData>( list ) );
        }
        userResponse.status( user.getStatus() );
        Set<Role> set = user.getRoles();
        if ( set != null ) {
            userResponse.roles( new LinkedHashSet<Role>( set ) );
        }
        userResponse.id( user.getId() );
        userResponse.username( user.getUsername() );
        userResponse.password( user.getPassword() );
        userResponse.firstName( user.getFirstName() );
        userResponse.lastName( user.getLastName() );
        userResponse.email( user.getEmail() );
        userResponse.phone( user.getPhone() );

        return userResponse.build();
    }


    public void updateUser(User user, UserUpdateRequest request) {
        if ( request == null ) {
            return;
        }

        user.setUsername( request.getUsername() );
        user.setId( request.getId() );
        user.setPassword( request.getPassword() );
        user.setFirstName( request.getFirstName() );
        user.setLastName( request.getLastName() );
        user.setEmail( request.getEmail() );
        user.setPhone( request.getPhone() );
        user.setStatus( request.getStatus() );
    }
}