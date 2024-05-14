package org.fanaticups.fanaticupsBack.controllers;

import org.fanaticups.fanaticupsBack.dao.entities.CupEntity;
import org.fanaticups.fanaticupsBack.models.CupDTO;
import org.fanaticups.fanaticupsBack.security.dao.UserEntity;

import org.fanaticups.fanaticupsBack.services.CupService;
import org.fanaticups.fanaticupsBack.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CupService cupService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "/users/favorites")
    public ResponseEntity<Void> updateFavoriteList(@RequestParam("userId") Long userId,
                                                   @RequestParam("cupId") Long cupId) {
        Optional<UserEntity> optionalUserEntity = this.userService.findUserById(userId);
        Optional<CupDTO> optionalCupEntity = this.cupService.findCupById(cupId);
        if (optionalCupEntity.isEmpty() || optionalUserEntity.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        this.userService.setCupToFavorites(optionalUserEntity.get(), optionalCupEntity.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204 response
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(value = "/users/favorites/{userId}")
    public ResponseEntity<Page<CupDTO>> getFavoriteList(@PathVariable("userId") Long userId,
                                                        @PageableDefault(sort = "id",
                                                                direction = Sort.Direction.DESC,
                                                                page = 0,
                                                                size = 12) Pageable pageable) {
//        List<CupDTO> cupDTOList = this.userService.getFavoriteCupList(userId);
        Page<CupDTO> pageCupDTO = this.userService.getFavoriteCupList(userId, pageable);
        return ResponseEntity.ok(pageCupDTO);
    }

}
