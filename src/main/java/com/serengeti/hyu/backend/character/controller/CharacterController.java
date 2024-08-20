package com.serengeti.hyu.backend.character.controller;

import com.serengeti.hyu.backend.character.dto.CharacterRequestDto;
import com.serengeti.hyu.backend.character.dto.CharacterResponseDto;
import com.serengeti.hyu.backend.character.service.CharacterService;
import com.serengeti.hyu.backend.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hue-test")
public class CharacterController {

    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return user.getUserId(); // User 객체에서 ID를 반환한다고 가정
        }
        throw new IllegalStateException("User not authenticated or invalid principal type.");
    }

    @PostMapping("/result")
    public ResponseEntity<CharacterResponseDto> submitCharacter(@RequestBody CharacterRequestDto characterRequestDTO) {
        Long userId = getAuthenticatedUserId();
        characterRequestDTO.setUserId(userId);
        CharacterResponseDto characterResponseDto = characterService.saveCharacter(characterRequestDTO);
        return ResponseEntity.ok(characterResponseDto);
    }

    @GetMapping("/result")
    public ResponseEntity<CharacterResponseDto> getCharacterResult() {
        Long userId = getAuthenticatedUserId();
        CharacterResponseDto characterResponseDto = characterService.getCharacterResult(userId);
        return ResponseEntity.ok(characterResponseDto);
    }
}
