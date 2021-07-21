package com.blindkiosk.server.Model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Custom")
public class CustomModel {

    @Id
    private String _id;
    private String name;

    @Field("store_name")
    private String storeName;
    @Field("store_id")
    private String storeId;
    private List<String> type;
    private List<String> price;

}
