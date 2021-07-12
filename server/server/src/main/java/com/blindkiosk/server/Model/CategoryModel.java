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
@Document(collection = "Category")
public class CategoryModel {

    @Id
    private String _id;//고유번호

    @Field("store_id")
    private String storeId;
    private String name;

    //private Map<String,SubCategoryModel> subCategory=new HashMap<>();
}
