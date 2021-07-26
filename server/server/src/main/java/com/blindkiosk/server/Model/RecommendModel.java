package com.blindkiosk.server.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String menuId;
    private String recommendId;
}
