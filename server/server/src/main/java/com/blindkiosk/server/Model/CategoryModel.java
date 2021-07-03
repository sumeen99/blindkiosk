package com.blindkiosk.server.Model;

import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class CategoryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//고유번호

    @OneToMany(mappedBy="subCategoryModel")
    private Map<String,SubCategoryModel> subCategory=new HashMap<>();
}
