package com.serengeti.hyu.backend.character.service;

import com.serengeti.hyu.backend.character.dto.CharacterRequestDto;
import com.serengeti.hyu.backend.character.dto.CharacterResponseDto;
import com.serengeti.hyu.backend.character.entity.Character;
import com.serengeti.hyu.backend.character.enums.Question;
import com.serengeti.hyu.backend.character.enums.ResponseOption;
import com.serengeti.hyu.backend.character.enums.ResultType;
import com.serengeti.hyu.backend.character.repository.CharacterRepository;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;

    @Autowired
    public CharacterService(CharacterRepository characterRepository, UserRepository userRepository) {
        this.characterRepository = characterRepository;
        this.userRepository = userRepository;
    }

    public CharacterResponseDto getCharacterResult(Long userId) {
        Character character = findCharacterByUserId(userId);
        Map<Question, ResponseOption> responses = character.getResponses();
        ResultType resultType = calculateResultType(responses);

        return new CharacterResponseDto(userId, resultType);
    }

    public CharacterResponseDto saveCharacter(CharacterRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 응답을 기반으로 ResultType 계산
        Map<Question, ResponseOption> responses = requestDto.getResponses();
        ResultType resultType = calculateResultType(responses);

        // 기존 Character가 존재하는지 확인
        Character existingCharacter = characterRepository.findByUser(user).orElse(null);

        if (existingCharacter != null) {
            // 기존 Character가 있을 경우 업데이트
            existingCharacter.setResponses(responses);
            existingCharacter.setResultType(resultType);
            characterRepository.save(existingCharacter);
        } else {
            // 새로운 Character 엔티티 생성
            Character newCharacter = new Character(user, responses);
            newCharacter.setResultType(resultType);
            characterRepository.save(newCharacter);
        }

        // 업데이트 또는 새로 생성한 Character의 resultType을 포함한 ResponseDto 생성
        return new CharacterResponseDto(requestDto.getUserId(), resultType);
    }

    private ResultType calculateResultType(Map<Question, ResponseOption> responses) {
        // 각 ResultType의 빈도수를 저장할 맵
        Map<ResultType, Integer> resultCounts = new HashMap<>();

        // 모든 문항에 대해 빈도수 카운팅
        for (Map.Entry<Question, ResponseOption> entry : responses.entrySet()) {
            Question question = entry.getKey();
            ResponseOption response = entry.getValue();
            ResultType resultType = ResultMapping.getResultType(question, response);

            resultCounts.put(resultType, resultCounts.getOrDefault(resultType, 0) + 1);
        }

        // 가장 많이 나온 ResultType을 찾음
        return resultCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(ResultType.RESULT_1); // 기본값 (실제 구현 시 변경 필요)
    }

    public Character findCharacterByUserId(Long userId) {
        // User 엔티티를 userId로 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // User 객체를 기반으로 Character 엔티티 조회
        return characterRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Character not found"));
    }
}
