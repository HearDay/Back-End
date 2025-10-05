package HearDay.spring.domain.wordbookmark.feign;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "channel")
public class DictionaryApiResponse {

    private String title;
    private String link;
    private String description;

    @JacksonXmlProperty(localName = "lastBuildDate")
    private String lastBuildDate;

    private Integer total;
    private Integer start;
    private Integer num;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "item")
    private List<Item> item;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        @JacksonXmlProperty(localName = "target_code")
        private Integer targetCode;

        private String word;

        @JacksonXmlProperty(localName = "sup_no")
        private Integer supNo;

        private String origin;
        private String pronunciation;

        @JacksonXmlProperty(localName = "word_grade")
        private String wordGrade;

        private String pos;
        private String link;

        private Sense sense;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Sense {
        private String definition;
    }

}