package com.serengeti.hyu.backend.character.controller;

import com.serengeti.hyu.backend.character.dto.CharacterRequestDto;
import com.serengeti.hyu.backend.character.dto.CharacterResponseDto;
import com.serengeti.hyu.backend.character.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hue-test")
public class CharacterController {

    private final CharacterService characterService;

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/result")
    public ResponseEntity<CharacterResponseDto> submitCharacter(@RequestBody CharacterRequestDto characterRequestDTO) {
        CharacterResponseDto characterResponseDto = characterService.saveCharacter(characterRequestDTO);
        return ResponseEntity.ok(characterResponseDto);
    }

    @GetMapping("/result/{userId}")
    public ResponseEntity<CharacterResponseDto> getCharacterResult(@PathVariable Long userId) {
        CharacterResponseDto characterResponseDto = characterService.getCharacterResult(userId);
        return ResponseEntity.ok(characterResponseDto);
    }
}
