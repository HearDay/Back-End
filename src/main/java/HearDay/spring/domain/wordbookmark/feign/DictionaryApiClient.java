package HearDay.spring.domain.wordbookmark.feign;

import HearDay.spring.global.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "dictionaryApiClient",
        url = "https://krdict.korean.go.kr/api",
        configuration = FeignConfig.class)
public interface DictionaryApiClient {

    @GetMapping("/search")
    DictionaryApiResponse searchWord(
            @RequestParam("key") String apiKey,
            @RequestParam("q") String word,
            @RequestParam("type1") String type1);
}
