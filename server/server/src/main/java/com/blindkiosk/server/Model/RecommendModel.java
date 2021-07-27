package com.blindkiosk.server.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Recommend")
public class RecommendModel {

    @Id
    private String _id;//고유번호

    @Field("MenuId")
    private String menuId;

    @Field("RecommendId")
    private String recommendId;

    @Field("Percent")
    private String percent;
}
