package HearDay.spring.domain.userarticlebookmark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserArticleBookmarkControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @Test
    @DisplayName("북마크 목록 조회 API")
    void getMyBookmarks() throws Exception {
        // when & then
        mockMvc.perform(get("/api/article-bookmarks").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 추가 API")
    void addBookmark() throws Exception {
        // given
        Long articleId = 1L;

        // when & then
        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", articleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 삭제 API")
    void removeBookmark() throws Exception {
        // given
        Long articleId = 1L;

        // when & then
        mockMvc.perform(delete("/api/article-bookmarks/{articleId}", articleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 목록 조회 - 페이징 테스트")
    void getMyBookmarksWithPaging() throws Exception {
        // when & then
        mockMvc.perform(
                        get("/api/article-bookmarks")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "createdAt,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 추가 - 중복 테스트")
    void addBookmarkDuplicate() throws Exception {
        // given
        Long articleId = 1L;

        // 첫 번째 북마크 추가
        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", articleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // when & then - 중복 추가 시도
        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", articleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 삭제 - 존재하지 않는 북마크")
    void removeNonExistentBookmark() throws Exception {
        // given
        Long nonExistentArticleId = 99999L;

        // when & then
        mockMvc.perform(delete("/api/article-bookmarks/{articleId}", nonExistentArticleId))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 추가 - 존재하지 않는 게시글")
    void addBookmarkWithNonExistentArticle() throws Exception {
        // given
        Long nonExistentArticleId = 99999L;

        // when & then
        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", nonExistentArticleId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
