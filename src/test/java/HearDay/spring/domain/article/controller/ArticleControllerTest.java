package HearDay.spring.domain.article.controller;

import HearDay.spring.common.TestConfig;
import HearDay.spring.common.enums.CategoryEnum;
import HearDay.spring.domain.article.dto.ArticleSearchDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleControllerTest extends TestConfig {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 목록 조회 API")
    void getAllArticles() throws Exception {
        // given
        ArticleSearchDto searchDto = new ArticleSearchDto(null, null);

        // when & then
        mockMvc.perform(
                        post("/api/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("page", "0")
                                .param("size", "10")
                                .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(10)))
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].title").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 상세 조회 API")
    void getArticle() throws Exception {
        // given
        Long articleId = 1L;

        // when & then
        mockMvc.perform(get("/api/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(articleId))
                .andExpect(jsonPath("$.data.title").exists())
                .andExpect(jsonPath("$.data.detail.content").exists())
                .andExpect(jsonPath("$.data.detail.ttsUrl").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 검색 API - 제목")
    void searchArticlesByTitle() throws Exception {
        // given
        ArticleSearchDto searchDto = new ArticleSearchDto(null, "코레일");

        // when & then
        mockMvc.perform(
                        post("/api/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title").value(containsString("코레일")))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 검색 API - 카테고리")
    void searchArticlesByCategory() throws Exception {
        // given
        ArticleSearchDto searchDto = new ArticleSearchDto(List.of(CategoryEnum.경제), null);

        // when & then
        mockMvc.perform(
                        post("/api/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].category").value(containsString("경제")))
                .andDo(print());
    }
}
