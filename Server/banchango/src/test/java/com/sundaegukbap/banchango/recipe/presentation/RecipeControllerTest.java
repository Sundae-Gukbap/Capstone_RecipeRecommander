package com.sundaegukbap.banchango.recipe.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sundaegukbap.banchango.recipe.application.RecipeService;
import com.sundaegukbap.banchango.recipe.domain.Difficulty;
import com.sundaegukbap.banchango.recipe.dto.RecipeDetailResponse;
import com.sundaegukbap.banchango.support.CustomWebMvcTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@CustomWebMvcTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class RecipeControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RecipeService recipeService;

    RecipeDetailResponse recipeDetailResponse;
    @BeforeEach
    void setUp() {
        recipeDetailResponse =
                new RecipeDetailResponse(
                        1L,
                        "간장계란볶음밥",
                        "달짝지근함",
                        "test.png",
                        "test.link",
                        new ArrayList<>(
                                List.of("간장", "밥")
                        ),
                        new ArrayList<>(
                                List.of("계란", "당근")
                        ),
                        1,
                        30,
                        Difficulty.아무나
                );
    }
    @Nested
    @DisplayName("/api/recipe/")
    class 레시피_조회 {
        @Test
        @DisplayName("/recommand/{userId}")
        void 추천_레시피_조회() throws Exception {
            //given
            List<RecipeDetailResponse> expected = new ArrayList<>(
                    List.of(recipeDetailResponse)
            );

            given(recipeService.getRecommandedRecipes(anyLong()))
                    .willReturn(expected);

            // when
            String content = mockMvc.perform(get("/api/recipe/recommand/{userId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);
            List<RecipeDetailResponse> actual = objectMapper.readValue(content, new TypeReference<List<RecipeDetailResponse>>() {
            });

            //then
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("/{userId}/{recipeId}")
        void 상세_레시피_조회() throws Exception {
            //given
            RecipeDetailResponse expected = recipeDetailResponse;

            given(recipeService.getRecipe(anyLong(), anyLong()))
                    .willReturn(expected);

            // when
            String content = mockMvc.perform(get("/api/recipe/{userId}/{recipeId}", 1L, 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString(StandardCharsets.UTF_8);
            RecipeDetailResponse actual = objectMapper.readValue(content, new TypeReference<RecipeDetailResponse>() {});

            //then
            assertThat(actual).isEqualTo(expected);
        }
    }
}
