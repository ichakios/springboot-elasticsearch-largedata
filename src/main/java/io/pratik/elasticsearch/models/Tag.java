package io.pratik.elasticsearch.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@Builder
@Document(indexName = "tagindex")
public class Tag {

    public Tag(String id, String user, String tag, Date tagTime, String contentType, String contentId) {
        this.id = id;
        this.user = user;
        this.tag = tag;
        this.tagTime = tagTime;
        this.contentType = contentType;
        this.contentId = contentId;
    }

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "user")
    private String user;

    @Field(type = FieldType.Text, name = "tag")
    private String tag;

    @Field(type = FieldType.Date, name = "tagTime",format = DateFormat.basic_date)
    private Date tagTime;

    @Field(type = FieldType.Text, name = "contentType")
    private String contentType;

    @Field(type = FieldType.Text, name = "contentId")
    private String contentId;

}
