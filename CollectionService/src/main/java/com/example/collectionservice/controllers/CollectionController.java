package com.example.collectionservice.controllers;

import com.example.collectionservice.dto.CollectionDTO;
import com.example.collectionservice.dto.CollectionPostCreateDTO;
import com.example.collectionservice.dto.CollectionResponseDTO;
import com.example.collectionservice.dto.CollectionVisibilityTypeOnlyTypeDTO;
import com.example.collectionservice.models.Collection;
import com.example.collectionservice.models.CollectionVisibilityType;
import com.example.collectionservice.services.CollectionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller // This means that this class is a Controller
public class CollectionController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private CollectionService collectionService;

    @PostMapping(path="/api/collections") // Map ONLY POST Requests
    public @ResponseBody ResponseEntity AddCollection (@Valid @RequestBody Collection requestBody) {
        Collection collection = collectionService.Create(requestBody);
        return ResponseEntity.status(201).body(collection);

    }

//    @GetMapping(path="/collections")
//    public @ResponseBody ResponseEntity GetAllCollections() {
//        Iterable<CollectionResponseDTO> collectionList = collectionService.List();
//        return ResponseEntity.status(200).body(collectionList);
//    }

    @GetMapping(path="/api/collections/{id}")
    public @ResponseBody ResponseEntity GetDetails( @PathVariable("id") Integer id) {
        Collection collection = collectionService.Details(id);
        return ResponseEntity.status(200).body(collection);
    }

    @GetMapping(path="/api/collections")
    public @ResponseBody ResponseEntity GetCollections( @NotNull @DecimalMin("0") @RequestParam Set<Integer> ids) {
        Iterable<Collection> collections = collectionService.FindAllByIds(ids);
        return  ResponseEntity.status(200).body(collections);
    }

    @DeleteMapping(path="/api/collections/{id}")
    public @ResponseBody ResponseEntity Delete(@PathVariable("id") Integer id) {
        collectionService.Delete(id);
        return ResponseEntity.status(204).build();

    }

//    @PutMapping("/api/collections/{id}")
//    public @ResponseBody ResponseEntity Update(@PathVariable("id") Integer id, @Valid @RequestBody Collection requestBody) {
//        Collection updated = collectionService.Update(id, requestBody);
//        return ResponseEntity.status(200).body(updated);
//
//    }

//    @PutMapping("/api/collections/{id}/{visibilityId}")
//    public @ResponseBody ResponseEntity UpdateVisibilityType(@PathVariable("id") Integer id, @PathVariable("visibilityId") Integer visibilityId) {
//        Collection updated = collectionService.ChangeVisibilityType(id, visibilityId);
//        return ResponseEntity.status(200).body(updated);
//
//    }

    @PutMapping("/api/collections/{id}")
    public @ResponseBody ResponseEntity UpdateVisibilityType2(@PathVariable("id") Integer id, @RequestBody CollectionVisibilityTypeOnlyTypeDTO collectionVisibilityTypeOnlyTypeDTO) {
        Collection updated = collectionService.ChangeVisibilityType(id, collectionVisibilityTypeOnlyTypeDTO);
        return ResponseEntity.status(200).body(updated);

    }

    @GetMapping(path="/api/collectionTypes")
    public @ResponseBody ResponseEntity GetAllCollectionTypes() {
        Iterable<CollectionVisibilityType> collectionVisibilityTypes = collectionService.ListVisibilityTypes();
        return ResponseEntity.status(200).body(collectionVisibilityTypes);

    }
}