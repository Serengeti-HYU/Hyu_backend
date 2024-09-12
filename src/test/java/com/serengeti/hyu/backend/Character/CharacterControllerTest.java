package com.serengeti.hyu.backend.Character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serengeti.hyu.backend.character.controller.CharacterController;
import com.serengeti.hyu.backend.character.dto.CharacterRequestDto;
import com.serengeti.hyu.backend.character.dto.CharacterResponseDto;
import com.serengeti.hyu.backend.character.enums.Question;
import com.serengeti.hyu.backend.character.enums.ResponseOption;
import com.serengeti.hyu.backend.character.enums.ResultType;
import com.serengeti.hyu.backend.character.service.CharacterService;
import com.serengeti.hyu.backend.user.entity.User;
import com.serengeti.hyu.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CharacterController.class)
public class CharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CharacterService characterService;

//    @InjectMocks
//    private CharacterController characterController;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("John Doe");
        testUser.setUsername("johndoe");
        testUser.setPassword("password");
        testUser.setEmail("john.doe@example.com");

        // Setting up mock behavior for the UserRepository
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(testUser));
    }

    @Test
    @DisplayName("Submit character results and get character result")
    public void testSubmitAndRetrieveCharacter() throws Exception {
        // Setting up the CharacterRequestDto
        Map<Question, ResponseOption> responses = new HashMap<>();
        responses.put(Question.QUESTION_1, ResponseOption.STRONGLY_AGREE);
        responses.put(Question.QUESTION_2, ResponseOption.AGREE);

        CharacterRequestDto requestDto = new CharacterRequestDto(1L, responses);

        // Mocking CharacterResponseDto
        CharacterResponseDto responseDto = new CharacterResponseDto(1L, ResultType.RESULT_2);

        // Mocking the service methods
        when(characterService.saveCharacter(any(CharacterRequestDto.class))).thenReturn(responseDto);
        when(characterService.getCharacterResult(anyLong())).thenReturn(responseDto);

        // Test POST /hue-test/result
        mockMvc.perform(MockMvcRequestBuilders.post("/hue-test/result")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))) // objectMapper 사용
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.resultType").value(ResultType.RESULT_2.name()))
                .andExpect(jsonPath("$.typeName").value(ResultType.RESULT_2.getTypeName()))
                .andExpect(jsonPath("$.description").value(ResultType.RESULT_2.getDescription()));

        // Test GET /hue-test/result/{userId}
        mockMvc.perform(MockMvcRequestBuilders.get("/hue-test/result/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.resultType").value(ResultType.RESULT_2.name()))
                .andExpect(jsonPath("$.typeName").value(ResultType.RESULT_2.getTypeName()))
                .andExpect(jsonPath("$.description").value(ResultType.RESULT_2.getDescription()));
    }
}
