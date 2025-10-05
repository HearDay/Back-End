// DictionaryService.java
package HearDay.spring.domain.wordbookmark.service;

import HearDay.spring.domain.wordbookmark.feign.DictionaryApiClient;
import HearDay.spring.domain.wordbookmark.feign.DictionaryApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryService {

    private final DictionaryApiClient dictionaryApiClient;

    @Value("${dictionary.api.key}")
    private String apiKey;

    public List<String> searchWord(String word) {
        try {

            DictionaryApiResponse response = dictionaryApiClient.searchWord(
                    apiKey,
                    word,
                    "word"     // word: 표제어 검색
            );
            return parseResponse(word, response);

        } catch (Exception e) {
            return List.of("검색 중 오류가 발생했습니다.");

        }
    }

    private List<String> parseResponse(String word, DictionaryApiResponse response) {
        List<String> definitions = new ArrayList<>();

        if (response == null || response.getItem() == null || response.getItem().isEmpty()) {
            definitions.add("검색 결과가 없습니다.");
        } else if(response.getItem().size() ==1){
            definitions.add(response.getItem().get(0).getSense().getDefinition());
        }else {
            int i =1;
            for (DictionaryApiResponse.Item item : response.getItem()) {
                if (item.getSense() != null && item.getSense().getDefinition() != null && item.getWord().equals(word)) {
                    StringBuilder definition = new StringBuilder(removeHtmlTags(item.getSense().getDefinition()));
                    if (!definition.isEmpty()) {
                        definition.insert(0, i++ + ". ");
                        definitions.add(definition.toString());
                    }
                }
            }

            if (definitions.isEmpty()) {
                definitions.add("검색 결과가 없습니다.");
            }
        }
        return definitions;
    }

    private String removeHtmlTags(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.replaceAll("<[^>]*>", "")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&")
                .replaceAll("&quot;", "\"")
                .replaceAll("&#x27;", "'")
                .replaceAll("\\^", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}