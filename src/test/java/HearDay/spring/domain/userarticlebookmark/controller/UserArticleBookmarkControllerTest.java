package HearDay.spring.domain.userarticlebookmark.controller;

import HearDay.spring.global.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserArticleBookmarkControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtTokenProvider jwtTokenProvider;

    private String token;

    @BeforeEach
    void setUp() {
        // ✅ login_id = testuser1 기준으로 JWT 생성
        token = jwtTokenProvider.generateToken("testuser1");
    }

    @Test
    @DisplayName("북마크 목록 조회 API")
    void getMyBookmarks() throws Exception {
        mockMvc.perform(
                        get("/api/article-bookmarks")
                                .param("page", "0")
                                .param("size", "10")
                                .header("Authorization", "Bearer " + token)) // ✅ 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 추가 API")
    void addBookmark() throws Exception {
        Long articleId = 1L;

        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", articleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)) // ✅ 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 삭제 API")
    void removeBookmark() throws Exception {
        Long articleId = 1L;

        mockMvc.perform(
                        delete("/api/article-bookmarks/{articleId}", articleId)
                                .header("Authorization", "Bearer " + token)) // ✅ 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 목록 조회 - 페이징 테스트")
    void getMyBookmarksWithPaging() throws Exception {
        mockMvc.perform(
                        get("/api/article-bookmarks")
                                .param("page", "0")
                                .param("size", "5")
                                .param("sort", "createdAt,desc")
                                .header("Authorization", "Bearer " + token)) // ✅ 토큰 추가
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 추가 - 중복 테스트")
    void addBookmarkDuplicate() throws Exception {
        Long articleId = 1L;

        // 첫 번째 요청
        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", articleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());

        // 두 번째 중복 요청
        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", articleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 삭제 - 존재하지 않는 북마크")
    void removeNonExistentBookmark() throws Exception {
        Long nonExistentArticleId = 99999L;

        mockMvc.perform(
                        delete("/api/article-bookmarks/{articleId}", nonExistentArticleId)
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("북마크 추가 - 존재하지 않는 게시글")
    void addBookmarkWithNonExistentArticle() throws Exception {
        Long nonExistentArticleId = 99999L;

        mockMvc.perform(
                        post("/api/article-bookmarks/{articleId}", nonExistentArticleId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
