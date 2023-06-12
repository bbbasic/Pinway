package com.example.userservice.services;


import com.example.userservice.dto.*;
import com.example.userservice.exception.PinwayError;
import com.example.userservice.infrastructure.CollectionService;
import com.example.userservice.models.User;
import com.example.userservice.models.UserCollection;
import com.example.userservice.repositories.UserCollectionRepository;
import com.example.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserCollectionServiceImpl implements UserCollectionService{

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @Autowired
    private UserCollectionRepository userCollectionRepository;

    @Autowired
    private CollectionService collectionService;

    @Override
    public UserDTO AddCollection(Integer id, UserCollectionCreateDTO userCollectionCreateDTO) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent())
            throw new PinwayError("Not found User with id = " + id);
        // prvjera da li ima post sa IDem ovim datim

        User user = userOpt.get();
        Boolean doesExist = collectionService.DoesExist(userCollectionCreateDTO.getCollectionId());
        if (!doesExist)
            throw new PinwayError("Not found Collection with id = " + userCollectionCreateDTO.getCollectionId());

        UserCollection userCollection = new UserCollection(userCollectionCreateDTO.getCollectionId(), user);

        userCollectionRepository.save(userCollection);

        collectionService.UpdateCollectionVisibility(userCollection.getCollectionId(), "SHARED");

        UserVisibilityTypeDTO userVisibilityTypeDTO = new UserVisibilityTypeDTO(
                user.getUserVisibilityType().getId(),
                user.getUserVisibilityType().getType()
        );

        UserDTO userDTO = convertToDTO(user, userVisibilityTypeDTO);
        return userDTO;

    }

    private UserDTO convertToDTO(User user, UserVisibilityTypeDTO userVisibilityTypeDTO) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setGuid(user.getGuid());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUserVisibilityType(userVisibilityTypeDTO);

        List<UserDTO> followingDTOList = convertToDTOList(user.getFollowing());
        userDTO.setFollowing(followingDTOList);

        return userDTO;
    }

    @Override
    public UserResponseDTO GetAllCollectionsForUser(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (!userOpt.isPresent())
            throw new PinwayError("Not found User with id = " + id);

        User user = userOpt.get();

        List<CollectionDTO> collectionDTOS = collectionService.GetAllCollectionsForUser(id);
        UserVisibilityTypeDTO userVisibilityTypeDTO = new UserVisibilityTypeDTO(user.getUserVisibilityType().getId(), user.getUserVisibilityType().getType());

        List<UserDTO> followingDTOList = convertToDTOList(user.getFollowing());


        UserDTO userDTO = new UserDTO(user.getId(), user.getGuid(), user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), user.getPassword(), user.getCreatedAt(), userVisibilityTypeDTO, followingDTOList);

        UserResponseDTO userResponseDTO = new UserResponseDTO(userDTO, collectionDTOS);

        return userResponseDTO;
    }

    private List<UserDTO> convertToDTOList(List<User> userList) {
        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setGuid(user.getGuid());
            userDTO.setName(user.getName());
            userDTO.setSurname(user.getSurname());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPassword(user.getPassword());
            userDTO.setCreatedAt(user.getCreatedAt());

            UserVisibilityTypeDTO userVisibilityTypeDTO = new UserVisibilityTypeDTO(
                    user.getUserVisibilityType().getId(),
                    user.getUserVisibilityType().getType()
            );

            userDTO.setUserVisibilityType(userVisibilityTypeDTO);

            userDTOList.add(userDTO);
        }
        return userDTOList;
    }

    @Override
    public Iterable<CollectionDTO> GetSharedAndPublicCollectionsForUserFromUser(Integer actionUserId, Integer userId) {
        // actionUserId
        // userId
        // first get all collections from userId

        // then check the user_collection table to see if there is
        // a collection id inside with actionUserid binded

        //also if the collection is public save its id :) easy peasy lemon squeezy
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent())
            throw new PinwayError("Not found User with id = " + userId);

        Optional<User> actionUserOpt = userRepository.findById(actionUserId);
        if (!actionUserOpt.isPresent())
            throw new PinwayError("Not found User with id = " + actionUserId);

        List<CollectionDTO> collectionDTOS = collectionService.GetAllCollectionsForUser(userId);

        // public ones
        List<CollectionDTO> publicCollectionDTOS = collectionService.GetPublicCollectionsForUser(userId);

        for (CollectionDTO collectionDTO : collectionDTOS) {

            //now check the user_collection table
            List<UserCollection> userCollection = userCollectionRepository.getByCollectionId(collectionDTO.getId());
            for (UserCollection uc : userCollection) {
                if(uc.getUser().getId() == actionUserId)
                    publicCollectionDTOS.add(collectionDTO);
            }
        }

        return publicCollectionDTOS;
    }
}
